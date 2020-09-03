package com.ji.apihelper.helper

import android.content.Context
import com.ji.apihelper.request.Request
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

object ExecuteHelper {

    @JvmStatic
    fun <R> execute(context: Context, request: Request<R>): Disposable {
        return request.getApi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess { request.handleSuccess() }
                .doOnSubscribe {
                    if (request.option.isShowProgress) {
                        DialogHelper.showProgress(context)
                    }
                }
                .doAfterTerminate {
                    if (request.option.isShowProgress) {
                        DialogHelper.hideProgress()
                    }
                }
                .subscribe({ response ->
                    request.handleResponse(response)
                }, { throwable ->
                    request.handleError(context, throwable)
                })
    }
}