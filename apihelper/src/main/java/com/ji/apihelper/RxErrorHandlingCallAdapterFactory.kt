package com.ji.apihelper

import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.lang.reflect.Type

class RxErrorHandlingCallAdapterFactory : CallAdapter.Factory() {
    private val original: RxJava2CallAdapterFactory by lazy { RxJava2CallAdapterFactory.create() }

    companion object {
        fun create(): RxErrorHandlingCallAdapterFactory {
            return RxErrorHandlingCallAdapterFactory()
        }
    }

    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        return RxCallAdapterWrapper(retrofit, original[returnType, annotations, retrofit] as CallAdapter<Any, Any>)
    }
}