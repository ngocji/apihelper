package com.ji.apihelper.entity

data class RequestOption(
        val isShowProgress: Boolean = true,
        val isShowError: Boolean = true,
        val actionConfirmClicked: (() -> Unit)? = null
)