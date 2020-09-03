package com.ji.apihelper.entity

import androidx.fragment.app.DialogFragment
import com.ji.apihelper.dialog.SimpleProgressDialog

data class DialogOption(
        val progressDialogClass: Class<out DialogFragment> = SimpleProgressDialog::class.java,
        val messageDialogClass: Class<out DialogFragment>? = null
)