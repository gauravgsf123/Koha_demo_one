package com.bbbdem.koha.module.forgot

import com.bbbdem.koha.network.RetrofitInstance


/**
 *Created by Gaurav Kumar on 7/28/2022
 * QUYTECH
 */
class ForgotRepository {
    suspend fun getUserDetailByEmail(query: String) = RetrofitInstance.apiService?.getUserDetailByEmail(query)
}