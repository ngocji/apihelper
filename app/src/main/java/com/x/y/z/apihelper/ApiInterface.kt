package com.x.y.z.apihelper

import io.reactivex.Single
import retrofit2.http.GET

interface ApiInterface {
    @GET("/test")
    fun test(): Single<Boolean>
}