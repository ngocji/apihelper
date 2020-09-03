package com.ji.apihelper

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Function
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.HttpException
import retrofit2.Retrofit
import java.io.IOException
import java.lang.reflect.Type

class RxCallAdapterWrapper<R>(
        private val retrofit: Retrofit?,
        private val wrapped: CallAdapter<R, Any>) : CallAdapter<R, Any> {

    override fun responseType(): Type {
        return wrapped.responseType()
    }

    override fun adapt(call: Call<R>): Any {
        return when (val result = wrapped.adapt(call)) {
            is Single<*> -> result.onErrorResumeNext { throwable ->
                Single.error(asRetrofitException(throwable))
            }

            is Observable<*> -> result.onErrorResumeNext(Function { throwable ->
                Observable.error(asRetrofitException(throwable))
            })

            is Flowable<*> -> result.onErrorResumeNext(Function { throwable ->
                Flowable.error(asRetrofitException(throwable))
            })

            is Completable -> result.onErrorResumeNext { throwable ->
                Completable.error(asRetrofitException(throwable))
            }

            else -> result
        }
    }

    private fun asRetrofitException(throwable: Throwable): RetrofitException {
        return when (throwable) {
            is HttpException -> {
                // We had non-200 http error
                val response = throwable.response()
                return RetrofitException.httpError(response, retrofit)
            }

            // A network error happened
            is IOException -> RetrofitException.networkError(throwable)

            // We don't know what happened. We need to simply convert to an unknown error
            else -> RetrofitException.unExpectedError(throwable)
        }
    }
}