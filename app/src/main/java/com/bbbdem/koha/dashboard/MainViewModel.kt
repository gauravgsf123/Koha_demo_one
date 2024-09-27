package com.bbbdem.koha.dashboard

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel(var app: Application) : ViewModel(){

    var repository = MainRepository()
    var pickupResponse: MutableLiveData<TripSheetResponse?> = MutableLiveData()
    var scanLocationResponse: MutableLiveData<TripSheetResponse> = MutableLiveData()
    fun pickupRequest(body: Map<String, String>) {
        viewModelScope.launch() {
            try {
                val response = repository.pickupRequest(body)
                pickupResponse.value = response
            } catch (e: Exception) {
            }
        }
    }
}