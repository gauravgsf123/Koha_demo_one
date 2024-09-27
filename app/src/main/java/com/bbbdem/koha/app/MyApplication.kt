package com.bbbdem.koha.app

import android.app.Application
import com.msg91.sendotpandroid.library.internal.SendOTP

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        application = this
        SendOTP.initializeApp(this,"344904AF5EqBN463202df4P1" ,"63201e81d6fc05328e7839d2");
    }

    companion object {
        private lateinit var application: Application
        fun getInstance(): Application {
            return application
        }
    }
}