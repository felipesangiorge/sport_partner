package com.find_sport_partner.find_sport_partner.core

import androidx.lifecycle.LiveData
import com.find_sport_partner.find_sport_partner.data.network.BaseResponse
import retrofit2.*
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

class ApiLiveDataCallAdapter<R>(
    private val responseType: Type,
    private val retrofit: Retrofit
) : CallAdapter<BaseResponse<R>, LiveData<ApiResponse<R>>> {

    override fun adapt(call: Call<BaseResponse<R>>): LiveData<ApiResponse<R>> {
        return object : LiveData<ApiResponse<R>>() {
            private var start = AtomicBoolean(false)
            override fun onActive() {
                super.onActive()
                if (start.compareAndSet(false, true)) {
                    call.enqueue(object : Callback<BaseResponse<R>> {
                        override fun onFailure(call: Call<BaseResponse<R>>, t: Throwable) {
                            postValue(
                                try {
                                    ApiResponse.create(t, call, responseType, retrofit)
                                } catch (e: Exception) {
                                    ErrorResponse<R>(e.message.orEmpty())
                                }
                            )
                        }

                        override fun onResponse(call: Call<BaseResponse<R>>, response: Response<BaseResponse<R>>) {
                            postValue(
                                try {
                                    ApiResponse.create(response, responseType, retrofit)
                                } catch (e: Exception) {
                                    try {
                                        ApiResponse.create(e, call, responseType, retrofit)
                                    } catch (ex: Exception) {
                                        ApiErrorResponse<R>(ex.message.orEmpty())
                                    }
                                }
                            )
                        }
                    })
                }
            }
        }
    }

    override fun responseType(): Type {
        return responseType
    }
}