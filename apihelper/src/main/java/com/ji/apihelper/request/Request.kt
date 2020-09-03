package com.ji.apihelper.request

import android.content.Context
import com.ji.apihelper.RetrofitException
import com.ji.apihelper.constance.ApiCode
import com.ji.apihelper.entity.RequestOption
import com.ji.apihelper.helper.DialogHelper
import io.reactivex.Single

abstract class Request<R>(val option: RequestOption = RequestOption()) {

    abstract fun getApi(): Single<R>

    abstract fun handleResponse(data: R)

    open fun handleSuccess() {}

    open fun handleError(context: Context, throwable: Throwable) {
        throwable.printStackTrace()
        if (!option.isShowError) return

        var code = ApiCode.UNKNOWN_EXCEPTION

        when (throwable) {
            is RetrofitException -> {
                val error: RetrofitException = throwable
                code = when (error.kind) {
                    RetrofitException.Kind.HTTP -> {
                        error.response?.code() ?: ApiCode.UNKNOWN_EXCEPTION
                    }

                    RetrofitException.Kind.NETWORK,
                    RetrofitException.Kind.UNEXPECTED -> {
                        ApiCode.NO_NETWORK
                    }
                }
            }
        }

        DialogHelper.showMessage(
                context,
                code,
                throwable.message ?: "",
                option.actionConfirmClicked
        )
    }
}