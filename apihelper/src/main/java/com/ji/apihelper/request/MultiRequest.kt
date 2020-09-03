package com.ji.apihelper.request

import com.ji.apihelper.entity.RequestOption
import io.reactivex.Single

abstract class MultiRequest<R : Any>(option: RequestOption = RequestOption()) :
    Request<Map<Class<*>, R>>(option) {

    enum class Type {
        CONCAT, ZIP, MERGE
    }

    override fun getApi(): Single<Map<Class<*>, R>> {
        return when (getType()) {
            Type.ZIP -> {
                Single.zip(getApis()) { array ->
                    array.associateBy { it::class.java } as Map<Class<*>, R>
                }
            }

            Type.CONCAT -> {
                Single.concat(getApis())
                    .toMap { it::class.java }
            }

            Type.MERGE -> {
                Single.merge(getApis())
                    .toMap { it::class.java }
            }

            else -> throw IllegalStateException("Error request!")
        }
    }

    abstract fun getApis(): List<Single<R>>

    abstract fun getType(): Type
}