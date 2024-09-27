package com.bbbdem.koha.dashboard

import com.bbbdem.koha.network.RetrofitInstance

class MainRepository {
    suspend fun pickupRequest(body:Map<String,String>): TripSheetResponse? = RetrofitInstance.apiService?.pickupRequest(body)
}