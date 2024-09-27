package com.bbbdem.koha.module.otp

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bbbdem.koha.R
import com.bbbdem.koha.network.Resource
import com.bbbdem.koha.utils.Utils
import kotlinx.coroutines.launch
import retrofit2.Response

class OTPViewModel(var app: Application) : ViewModel() {
    @SuppressLint("StaticFieldLeak")
    private var mContext: Context = app.applicationContext
    private val repository = OTPRepository()
    private var mOTPResponse = MutableLiveData<Resource<OTPResponse>>()
    var otpResponse: LiveData<Resource<OTPResponse>> = mOTPResponse


    fun sendOTP(url:String,body:Map<String,String>) {
        if (Utils.hasInternetConnection(mContext)) {
            mOTPResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = repository.sendOTP(url, body)
                mOTPResponse.value = response?.let { handleSendOTPResponse(it) }
            }
        } else mOTPResponse.value =
            Resource.Error(app.resources.getString(R.string.no_internet))
    }

    private fun handleSendOTPResponse(response: Response<OTPResponse>): Resource<OTPResponse>? {
        response.body()?.let {
            return when (response.code()) {
                200 -> Resource.Success("OTP send successfully",it)
                else -> Resource.Error(app.getString(R.string.some_thing_went_wrong))
            }
        }
        return Resource.Error(response.message())
    }

}