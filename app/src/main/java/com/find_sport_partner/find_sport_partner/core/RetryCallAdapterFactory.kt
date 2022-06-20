package com.find_sport_partner.find_sport_partner.core

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.urmomenz.urmomenz.core.Retry
import okhttp3.Request
import okio.Timeout
import retrofit2.*
import java.io.IOException
import java.lang.reflect.Type
import java.util.*
import java.util.concurrent.TimeoutException
import java.util.concurrent.atomic.AtomicInteger


/**
 * When you need something to be tried once more, with this adapter and {@see Retry} annotation, you can retry.
 *
 *
 * NOTE THAT: You can only retry asynchronous {@see Call} or {@see java.util.concurrent.CompletableFuture} implementations.
 *
 * @author dtodt
 */
class RetryCallAdapterFactory : CallAdapter.Factory() {
    /**
     * You can setup a default max retry count for all connections.
     */
    private val maxRetries = 0

    @RequiresApi(Build.VERSION_CODES.N)
    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *> {
        var itShouldRetry = maxRetries
        val retry: Retry? = getRetry(annotations)
        if (retry != null) {
            itShouldRetry = retry.max
        }
        Log.d("TIMEOUT", "Starting a CallAdapter with {} retries. $itShouldRetry")
        return RetryCallAdapter(
            retrofit.nextCallAdapter(this, returnType, annotations) as CallAdapter<Any, Any>?,
            itShouldRetry
        )
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getRetry(annotations: Array<Annotation>): Retry? {
        return Arrays.stream(annotations).parallel()
            .filter { annotation: Annotation? -> annotation is Retry }
            .map { annotation: Annotation? -> annotation as Retry? }
            .findFirst()
            .orElse(null)
    }

    private inner class RetryCallAdapter<R, T>(delegate: CallAdapter<R, T>?, maxRetries: Int) : CallAdapter<R, T> {
        private val delegated: CallAdapter<R, T>? = delegate
        private val maxRetries = maxRetries
        override fun responseType(): Type {
            return delegated!!.responseType()
        }

        override fun adapt(call: Call<R>): T {
            return delegated!!.adapt(if (maxRetries > 0) RetryingCall(call, maxRetries) else call)
        }
    }

    private inner class RetryingCall<R>(delegate: Call<R>, maxRetries: Int) : Call<R> {
        private val delegated: Call<R>? = delegate
        private val maxRetries = maxRetries

        override fun timeout(): Timeout {
            return Timeout()
        }

        @Throws(IOException::class)
        override fun execute(): Response<R> {
            return delegated!!.execute()
        }

        override fun enqueue(callback: Callback<R>) {
            delegated!!.enqueue(RetryCallback(delegated, callback, maxRetries))
        }

        override fun isExecuted(): Boolean {
            return delegated!!.isExecuted
        }

        override fun cancel() {
            delegated!!.cancel()
        }

        override fun isCanceled(): Boolean {
            return delegated!!.isCanceled
        }

        override fun clone(): Call<R> {
            return RetryingCall(delegated!!.clone(), maxRetries)
        }

        override fun request(): Request {
            return delegated!!.request()
        }
    }

    private inner class RetryCallback<T>(delegate: Call<T>, callback: Callback<T>, maxRetries: Int) : Callback<T> {
        private val call: Call<T>? = delegate
        private val callback: Callback<T>? = callback
        private val maxRetries = maxRetries
        private val retryCount = AtomicInteger(0)
        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (!response.isSuccessful && retryCount.incrementAndGet() <= maxRetries) {
                Log.d("TIMEOUT", "${response.code()}")
                retryCall()
            } else {
                callback!!.onResponse(call, response)
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            Log.d("TIMEOUT:", t.localizedMessage, t)
            if (retryCount.incrementAndGet() <= maxRetries) {
                retryCall()
            } else if (maxRetries > 0) {
                Log.d("TIMEOUT", "No retries left sending timeout up.")
                callback!!.onFailure(call, TimeoutException(String.format("No retries left after %s attempts.", maxRetries)))
            } else {
                callback!!.onFailure(call, t)
            }
        }

        private fun retryCall() {
            Log.d("TIMEOUT", "${retryCount.get()}, $maxRetries")
            call!!.clone().enqueue(this)
        }
    }
}