package com.bbbdem.koha.module.reset_password

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bbbdem.koha.network.Resource

class ResetPasswordViewModel(var app: Application) : ViewModel() {
    @SuppressLint("StaticFieldLeak")
    private var mContext: Context = app.applicationContext
    private val repository = ResetPasswordRepository()
    private var mResetPasswordResponse = MutableLiveData<Resource<String>>()
    var resetPasswordResponse: LiveData<Resource<String>> = mResetPasswordResponse


    /*fun resetPassword(patronId: Int?,resetPasswordRequest: ResetPasswordRequest) {
        if (Utils.hasInternetConnection(mContext)) {
            mResetPasswordResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = repository.resetPassword(patronId,resetPasswordRequest)
                mResetPasswordResponse.value = response?.let { handleResetPasswordResponse(it) }
            }
        } else mResetPasswordResponse.value =
            Resource.Error(app.resources.getString(R.string.no_internet))
    }

    private fun handleResetPasswordResponse(response: Response<String>): Resource<String>? {
        //if (response.isSuccessful) {
            response.body()?.let {
                return when (response.code()) {
                    200 -> Resource.Success("Success",it)
                    else -> Resource.Error(response.message())
                }
            }
       // }
        return Resource.Error(response.message())
    }*/

}