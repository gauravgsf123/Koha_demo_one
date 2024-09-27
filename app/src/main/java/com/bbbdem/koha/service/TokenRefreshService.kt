package com.bbbdem.koha.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.bbbdem.koha.common.Constant
import com.bbbdem.koha.common.SharedPreference
import com.bbbdem.koha.network.RetrofitInstance
import kotlinx.coroutines.*

class TokenRefreshService : Service() {
    private lateinit var sharedPreference: SharedPreference
    override fun onCreate() {
        super.onCreate()
        sharedPreference = SharedPreference(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
            refreshToken()
        GlobalScope.launch {
            timer()
        }

        return START_NOT_STICKY
    }

    private fun refreshToken() {
        GlobalScope.launch {
            val response = RetrofitInstance.apiService?.getToken()//repository.register(body)
            var result = response?.body()
            Log.d("result","${result?.accessToken}")
            sharedPreference.save(Constant.ACCESS_TOKEN, result?.accessToken!!)
            sharedPreference.save(Constant.TOKEN_TYPE, result?.tokenType!!)
            sharedPreference.save(Constant.EXPIRES_IN, result?.expiresIn!!)
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    var timer:  Int = 500000  // Declared globally
    private suspend fun timer(){
        withContext(Dispatchers.Main) {
            while (timer > 1){
                timer--
                Log.e("timer","$timer")
                delay(1000 * 30 *1)
                refreshToken()
            }
        }
    }
}