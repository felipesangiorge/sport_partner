package com.find_sport_partner.find_sport_partner.data.network

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.find_sport_partner.find_sport_partner.core.ApiLiveDataCallAdapterFactory
import com.find_sport_partner.find_sport_partner.BuildConfig
import com.find_sport_partner.find_sport_partner.core.RetryCallAdapterFactory
import com.squareup.moshi.Types
import okhttp3.OkHttpClient
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ServiceGenerator @Inject constructor() {

    companion object {
        const val X_APP_VERSION = "X-App-Version"
        const val X_PLATFORM = "android"
        const val X_PLATFORM_LABEL = "X-Platform"
        const val TIMEOUT_S = "TIMEOUT_SECONDS"
        const val ACCEPT_LANGUAGE = "Accept-Language"
        const val BASE_URL = "https://fake-poi-api.mytaxi.com/"
    }

    private val sportPartnerRetrofitApiBuilder by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addCallAdapterFactory(ApiLiveDataCallAdapterFactory())
            .addCallAdapterFactory(RetryCallAdapterFactory())
    }

    private fun httpClientCreator() = OkHttpClient.Builder()

    @RequiresApi(Build.VERSION_CODES.N)
    fun <S> serviceCreator(serviceClass: Class<S>, context: Context): S {
        val httpBuilder = httpClientCreator()

        httpBuilder.addInterceptor {
            it.proceed(
                it.request()
                    .newBuilder()
                    .addHeader(X_APP_VERSION, BuildConfig.VERSION_CODE.toString())
                    .addHeader(X_PLATFORM_LABEL, X_PLATFORM)
                    .build()
            )
        }

        httpBuilder.addInterceptor {
            val chainRequest = it.request()

            val newConnect = chainRequest.header(TIMEOUT_S)
            val timeoutConnection =
                if (!newConnect.isNullOrBlank()) {
                    newConnect.toInt()
                } else {
                    it.connectTimeoutMillis() / 1000
                }

            val builder = chainRequest.newBuilder()
            builder.removeHeader(TIMEOUT_S)

            it
                .withConnectTimeout(timeoutConnection, TimeUnit.SECONDS)
                .withReadTimeout(timeoutConnection, TimeUnit.SECONDS)
                .withWriteTimeout(timeoutConnection, TimeUnit.SECONDS)
                .proceed(builder.build())
        }

        httpBuilder.addInterceptor {
            val languages =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    context.resources.configuration.locales.let { l ->
                        (0 until l.size()).map {
                            l.get(it).toLanguageTag()
                        }
                    }
                } else {
                    listOf(context.resources.configuration.locales.get(0).language)
                }
            val formatedLanguages = languages.reduceRightIndexed { index, s, acc ->
                if (index > 0) "$acc, $s;q=${1 - index * 0.1}" else s
            }

            val request = it.request()
            it.proceed(
                request.newBuilder()
                    .addHeader(ACCEPT_LANGUAGE, formatedLanguages)
                    .build()
            )
        }

        httpBuilder.addInterceptor {
            val request = it.request()
            val response = it.proceed(request)

            if (response.code == 401) {
                val type = Types.newParameterizedType(BaseResponse::class.java, String::class.java)
                return@addInterceptor response.newBuilder().body(
                    "".toResponseBody(response.body?.contentType())
                ).build()
            }

            response
        }

        //DEBUG INTERCEPTOR
        httpBuilder.addInterceptor(
            HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    Log.d("OkHttp -->", " $message")
                }
            }).apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )

        val retrofit = sportPartnerRetrofitApiBuilder
            .client(httpBuilder.build())
            .build()

        return retrofit.create(serviceClass)
    }
}