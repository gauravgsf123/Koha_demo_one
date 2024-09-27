package com.bbbdem.koha.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.bbbdem.koha.common.Constant
import com.bbbdem.koha.common.SharedPreference
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

open class AuthInterceptorGlobal (val context: Context) : Interceptor {
    protected lateinit var sharedPreference: SharedPreference
    override fun intercept(chain: Interceptor.Chain): Response {
        if(!isConnected()) {
            throw NoConnectivityException()
        }
        //var authToken = "MTcwMDAyNDk2MC05NDg0NDItMC44OTQ0NzI1MzYyMzcxMTctZHdmd1gxRVBVaXhWZ1RVTTFxcEhzMjFCVTRPTFBu"
        //var authToken = SharedPreference(context).getValueString(Constant.ACCESS_TOKEN)
        var credentials: String = Credentials.basic(Constant.USERID, Constant.PASSWORD)
        var request = chain.request()
        request = request.newBuilder()
            .header("Content-Type", "Content-Type:application/x-www-form-urlencoded")
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .header("Authorization", credentials)
            //.header("Authorization","Bearer $authToken")
            .build()

        return try {
            chain.proceed(request)
        } catch (e:Exception) {
            chain.proceed(chain.request().newBuilder().build())
        }

    }

    private fun isConnected(): Boolean {
        val connectivityManager: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }
}

class NoConnectivityException : IOException() {
    // You can send any message whatever you want from here.
    override val message: String
        get() = "No Internet Connection"
    // You can send any message whatever you want from here.
}