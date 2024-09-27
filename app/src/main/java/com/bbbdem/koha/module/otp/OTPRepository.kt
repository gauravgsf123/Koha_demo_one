package com.bbbdem.koha.module.otp

import com.bbbdem.koha.network.RetrofitInstance


/**
 *Created by Gaurav Kumar on 7/28/2022
 * QUYTECH
 */
class OTPRepository {
    suspend fun sendOTP(url: String,body:Map<String,String>) = RetrofitInstance.apiService?.sendOTP(url,body)
}