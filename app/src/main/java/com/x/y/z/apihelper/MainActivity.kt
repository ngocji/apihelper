package com.x.y.z.apihelper

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ji.apihelper.entity.DialogOption
import com.ji.apihelper.helper.DialogHelper
import com.ji.apihelper.helper.ExecuteHelper.execute
import com.ji.apihelper.request.Request
import io.reactivex.Single

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            DialogHelper.init(DialogOption(messageDialogClass = TestError::class.java))
            execute(this, object : Request<Boolean>() {
                override fun getApi(): Single<Boolean> {
                    return ApiManager.api.test()
                }

                override fun handleResponse(data: Boolean) {
                }
            })
        }
    }
}