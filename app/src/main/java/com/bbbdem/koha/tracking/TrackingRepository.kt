package com.bbbdem.koha.tracking

import com.bbbdem.koha.network.RetrofitInstance

class TrackingRepository {
    suspend fun trackUser(body:Map<String,String>) = RetrofitInstance.apiService?.trackUser(body)
}