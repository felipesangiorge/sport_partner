package com.find_sport_partner.find_sport_partner.core

import android.util.Log
import com.find_sport_partner.find_sport_partner.data.network.BaseResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import java.lang.reflect.Type
import java.net.SocketTimeoutException

@Suppress("unused")
sealed class ApiResponse<T> {

    companion object {
        fun <T> create(error: Throwable, call: Call<BaseResponse<T>>, responseType: Type, retrofit: Retrofit): ErrorResponse<T> {
            if (error is RuntimeException) {
                Log.e("ERROR MOSHI", "Exception : $error")
            } else if (error is SocketTimeoutException) {
                val timeoutError = Error("Timeout exception, Url: ${call.request().url}", error)
                Log.e("ERROR TIMEOUT", "Error -> $timeoutError")
            } else {
                Log.e("ERROR CREATE RESPONSE", "Error -> ${error.message}")
            }

            return when (error) {
                is IOException -> ErrorResponse(error.message.orEmpty())
                else -> ErrorResponse(error.message.orEmpty())
            }
        }

        fun <T> create(response: Response<BaseResponse<T>>, responseType: Type, retrofit: Retrofit): ApiResponse<T> {
            return if (response.isSuccessful) processResponse(response.body(), response)
            else return try {
                val body = response.errorBody()?.let {
                    retrofit.responseBodyConverter<BaseResponse<T>>(
                        responseType,
                        emptyArray()
                    ).convert(it)
                }
                processResponse(body, response)
            } catch (e: Exception) {
                processResponse(null, response)
            }
        }

        private fun <T> processResponse(body: BaseResponse<T>?, response: Response<BaseResponse<T>>): ApiResponse<T> = when {
            response.code() == 401 -> ApiErrorResponse(response.errorBody()?.string().orEmpty())
            response.code() == 403 -> {
                ErrorResponse("API:Call Limit Reached")
            }
            response.code() == 404 -> {
                Log.e("API ERROR 404", "CODE 404 -> ${response.errorBody()?.string()}")
                ErrorResponse(response.errorBody()?.string().orEmpty())
            }
            response.code() == 500 -> {
                Log.e("API ERROR 500", "CODE 500 -> ${response.errorBody()?.string()}")
                ErrorResponse(response.errorBody()?.string().orEmpty())
            }
            body == null -> ErrorResponse(response.errorBody()?.string().orEmpty())
            else -> {
                ApiSuccessResponse(data = body.items!!, null)
            }
        }
    }
}

data class ApiSuccessResponse<T>(
    val data: T,
    val meta: BaseResponse.Meta?
) : ApiResponse<T>()

data class ApiErrorResponse<T>(val message: String) : ApiResponse<T>()

data class ErrorResponse<T>(val message: String) : ApiResponse<T>()