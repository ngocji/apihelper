package com.ji.apihelper.helper

import android.content.Context
import androidx.annotation.MainThread
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.ji.apihelper.dialog.IErrorMessage
import com.ji.apihelper.entity.DialogOption
import com.ji.apihelper.entity.ErrorMessage

object DialogHelper {
    private var dialogOption: DialogOption = DialogOption()

    private var progressDialog: DialogFragment? = null
    private var progressCounter = 0

    fun init(option: DialogOption) {
        dialogOption = option
    }

    @MainThread
    fun showProgress(context: Context) {
        if (progressDialog == null) {
            // create newInstance dialog
            progressCounter = 0
            progressDialog = dialogOption.progressDialogClass.newInstance() ?: return
            if (context is FragmentActivity && context.supportFragmentManager.findFragmentByTag(dialogOption.progressDialogClass.name) == null) {
                progressDialog?.show(context.supportFragmentManager, dialogOption.progressDialogClass.name)
            }
        } else {
            progressCounter++
        }
    }

    @MainThread
    fun hideProgress() {
        if (progressDialog != null) {
            progressCounter--
            if (progressCounter <= 0) {
                progressCounter = 0
                progressDialog?.dismissAllowingStateLoss()
                progressDialog = null
            }
        }
    }

    fun showMessage(context: Context, code: Int, message: String, action: (() -> Unit)? = null) {
        if (dialogOption.messageDialogClass != null && context is FragmentActivity) {
            val instance = dialogOption.messageDialogClass?.newInstance() ?: return
            if (instance is IErrorMessage) {
                instance.setError(ErrorMessage(message, action))
                instance.show(context.supportFragmentManager, dialogOption.messageDialogClass?.name)
            } else {
                throw NullPointerException("Message dialog need implement IMessageDialog")
            }
        } else {
            AlertDialog.Builder(context)
                    .setTitle("Oops $code!")
                    .setMessage(message)
                    .setPositiveButton("Ok") { _, _ ->
                        action?.invoke()
                    }
                    .create()
                    .show()
        }
    }
}