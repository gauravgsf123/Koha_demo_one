package com.bbbdem.koha.module.set_new_password

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

class SetNewPasswordViewModel(var app: Application) : ViewModel() {
    @SuppressLint("StaticFieldLeak")
    private var mContext: Context = app.applicationContext
    private val repository = SetNewPasswordRepository()
    private var mSetNewPasswordResponse = MutableLiveData<Resource<String>>()
    var setNewPasswordResponse: LiveData<Resource<String>> = mSetNewPasswordResponse


    fun setNewPassword(patronId: Int?,setNewPasswordRequest: SetNewPasswordRequest) {
        if (Utils.hasInternetConnection(mContext)) {
            mSetNewPasswordResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = repository.setNewPassword(patronId,setNewPasswordRequest)
                mSetNewPasswordResponse.value = response?.let { handleResetPasswordResponse(it) }
            }
        } else mSetNewPasswordResponse.value =
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
    }

}