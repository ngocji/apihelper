package com.x.y.z.apihelper

import com.ji.apihelper.helper.ApiBuilder

object ApiManager {
    val api = ApiBuilder.create("http://google.com", ApiInterface::class.java)
}