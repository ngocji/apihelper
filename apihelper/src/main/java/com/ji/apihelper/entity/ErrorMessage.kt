package com.ji.apihelper.entity

data class ErrorMessage(
        val message: String,
        val action: (() -> Unit)? = null
)