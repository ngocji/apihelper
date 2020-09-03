package com.x.y.z.apihelper

import android.os.Bundle
import com.ji.apihelper.dialog.IErrorMessage
import com.ji.apihelper.entity.ErrorMessage
import com.jibase.anotation.ViewInflate
import com.jibase.ui.dialog.BaseDialog
import kotlinx.android.synthetic.main.dialog_test.*

@ViewInflate(layout = R.layout.dialog_test)
class TestError : BaseDialog(), IErrorMessage {
    override fun onViewReady(savedInstanceState: Bundle?) {
        super.onViewReady(savedInstanceState)
        val data = getProperty<ErrorMessage>("error")

        message.text = data?.message

        button.setOnClickListener {
            dismiss()
            data?.action?.invoke()
        }
    }

    override fun setError(data: ErrorMessage) {
        addProperty("error", data)
    }
}
