package com.bbbdem.koha.tracking

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bbbdem.koha.R
import com.bbbdem.koha.network.Resource
import com.bbbdem.koha.utils.Utils
import kotlinx.coroutines.launch
import retrofit2.Response

class TrackingViewModel(var app: Application) : ViewModel()  {
    private var repository = TrackingRepository()
    private var mContext: Context = app.applicationContext
    private var mLoginResponseModel = MutableLiveData<Resource<MobileTrackResponseModel>>()

    fun trackUser(body:Map<String,String>) {
        if (Utils.hasInternetConnection(mContext)) {
            viewModelScope.launch {
                val response = repository.trackUser(body)
                mLoginResponseModel.value = response?.let { handleLoginResponse(it) }
            }
        } else mLoginResponseModel.value =
            Resource.Error(app.resources.getString(R.string.no_internet))
    }

    private fun handleLoginResponse(response: Response<List<MobileTrackResponseModel>>): Resource<MobileTrackResponseModel>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return when (response.code()) {
                    200 -> Resource.Success(it[0].Response, it[0])
                    else -> Resource.Error(it[0].Response)
                }
            }
        }
        return Resource.Error(response.message())
    }
}