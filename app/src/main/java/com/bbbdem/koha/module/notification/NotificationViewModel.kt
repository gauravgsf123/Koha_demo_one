package com.bbbdem.koha.module.notification

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bbbdem.koha.R
import com.bbbdem.koha.module.notification.model.NotificationRequestModel
import com.bbbdem.koha.module.notification.model.NotificationResponseModel
import com.bbbdem.koha.network.Resource
import com.bbbdem.koha.utils.Utils
import kotlinx.coroutines.launch
import retrofit2.Response

class NotificationViewModel(var app: Application) : ViewModel() {
    @SuppressLint("StaticFieldLeak")
    private var mContext: Context = app.applicationContext
    private val repository = NotificationRepository()
    private var mNotificationRequestModel = MutableLiveData<Resource<List<NotificationResponseModel.Data>>>()
    var notificationRequestModel: LiveData<Resource<List<NotificationResponseModel.Data>>> = mNotificationRequestModel


    fun getNotification(notificationRequestModel: NotificationRequestModel) {
        if (Utils.hasInternetConnection(mContext)) {
            mNotificationRequestModel.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = repository.getNotification(notificationRequestModel)
                mNotificationRequestModel.value = response?.let { handleGetNotificationResponse(it) }
            }
        } else mNotificationRequestModel.value =
            Resource.Error(app.resources.getString(R.string.no_internet))
    }

    private fun handleGetNotificationResponse(response: Response<NotificationResponseModel>): Resource<List<NotificationResponseModel.Data>>? {
        response.body()?.let {
            return when (response.code()) {
                200 -> Resource.Success("Success",it.data)
                else -> Resource.Error(response.message())
            }
        }
        return Resource.Error(response.message())
    }

}