package com.bbbdem.koha.module.my_account.personal_detail

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

class PersonalDetailViewModel(var app: Application) : ViewModel() {
    private var mContext: Context = app.applicationContext
    private val repository = PersonalDetailRepository()
    private var mPersonalDetailResponseModel = MutableLiveData<Resource<PersonalDetailResponseModel>>()
    var personalDetailResponseModel: LiveData<Resource<PersonalDetailResponseModel>> = mPersonalDetailResponseModel

    fun updatePersonalDetail(patrons:Int,requestModel: PersonalDetailRequestModel) {
        if (Utils.hasInternetConnection(mContext)) {
            mPersonalDetailResponseModel.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = repository.updatePersonalDetail(patrons,requestModel)
                mPersonalDetailResponseModel.value = response?.let { handleUpdatePersonalDetailResponse(it) }
            }
        } else mPersonalDetailResponseModel.value =
            Resource.Error(app.resources.getString(R.string.no_internet))
    }

    private fun handleUpdatePersonalDetailResponse(response: Response<PersonalDetailResponseModel>): Resource<PersonalDetailResponseModel>? {
        //if (response.isSuccessful) {
        response.body()?.let {
            return when (response.code()) {
                200 -> Resource.Success("Success",it)
                else -> Resource.Error(app.resources.getString(R.string.some_thing_went_wrong))
            }
        }
        // }
        return Resource.Error(response.message())
    }
}