package com.bbbdem.koha.module.forgot

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bbbdem.koha.R
import com.bbbdem.koha.login.model.UserDetailResponseModel
import com.bbbdem.koha.network.Resource
import com.bbbdem.koha.utils.Utils
import kotlinx.coroutines.launch
import retrofit2.Response

class ForgotViewModel(var app: Application) : ViewModel() {
    @SuppressLint("StaticFieldLeak")
    private var mContext: Context = app.applicationContext
    private val repository = ForgotRepository()
    private var mUserDetailResponseModel = MutableLiveData<Resource<List<UserDetailResponseModel>>>()
    var userDetailResponseModel: LiveData<Resource<List<UserDetailResponseModel>>> = mUserDetailResponseModel


    fun getUserDetailByEmail(query:String) {
        if (Utils.hasInternetConnection(mContext)) {
            mUserDetailResponseModel.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = repository.getUserDetailByEmail(query)
                mUserDetailResponseModel.value = response?.let { handleUserDetailResponse(it) }
            }
        } else mUserDetailResponseModel.value =
            Resource.Error(app.resources.getString(R.string.no_internet))
    }

    private fun handleUserDetailResponse(response: Response<List<UserDetailResponseModel>>): Resource<List<UserDetailResponseModel>>? {
        //if (response.isSuccessful) {
        response.body()?.let {
            return when (response.code()) {
                200 -> Resource.Success("Success",it)
                else -> Resource.Error(app.getString(R.string.some_thing_went_wrong))
            }
        }
        //}
        return Resource.Error(response.message())
    }

}