package com.ji.apihelper.header

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

interface IHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val builder = request.newBuilder()
        initHeader(builder)
        request = builder.build()
        return chain.proceed(request)
    }


    fun initHeader(requestBuilder: Request.Builder)
}