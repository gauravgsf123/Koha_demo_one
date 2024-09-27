package com.bbbdem.koha.module.splash_screen

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bbbdem.koha.R
import com.bbbdem.koha.module.splash_screen.model.AboutLibraryResponseModel
import com.bbbdem.koha.module.splash_screen.model.LibraryFeatureResponseModel
import com.bbbdem.koha.module.splash_screen.model.LibraryRequestModel
import com.bbbdem.koha.module.splash_screen.model.LibraryResponseModel
import com.bbbdem.koha.module.splash_screen.model.RequestModel
import com.bbbdem.koha.network.Resource
import com.bbbdem.koha.utils.Utils
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class SplashViewModel(var app: Application) : ViewModel(){
    private var mContext: Context = app.applicationContext
    var repository = SplashRepository()
    var tokenResponse: MutableLiveData<TokenResponse?> = MutableLiveData()
    private var mLibraryResponseModel = MutableLiveData<Resource<LibraryResponseModel>>()
    var libraryResponseModel: LiveData<Resource<LibraryResponseModel>> = mLibraryResponseModel
    private var mLibraryFeatureResponseModel = MutableLiveData<Resource<LibraryFeatureResponseModel>>()
    var libraryFeatureResponseModel: LiveData<Resource<LibraryFeatureResponseModel>> = mLibraryFeatureResponseModel
    private var mLibraryDetailResponseModel = MutableLiveData<Resource<AboutLibraryResponseModel>>()
    var libraryDetailResponseModel: LiveData<Resource<AboutLibraryResponseModel>> = mLibraryDetailResponseModel
    fun getToken() {
        viewModelScope.launch() {
            try {
                val response = repository.getToken()
                tokenResponse.value = response?.body()
            } catch (e: Exception) {
            }
        }
    }

    fun getLibraryDetail(requestModel: LibraryRequestModel) {
        if (Utils.hasInternetConnection(mContext)) {
            mLibraryResponseModel.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = repository.getLibraryDetail(requestModel)
                mLibraryResponseModel.value = response?.let { handleLibraryDetailResponse(it) }
            }
        } else mLibraryResponseModel.value =
            Resource.Error(app.resources.getString(R.string.no_internet))
    }

    private fun handleLibraryDetailResponse(response: Response<LibraryResponseModel>): Resource<LibraryResponseModel> {
        response.body()?.let {
            return when (response.code()) {
                200 -> Resource.Success("Success",it)
                else -> Resource.Error(mContext.resources.getString(R.string.some_thing_went_wrong))
            }
        }
        return Resource.Error(response.message())
    }

    fun getLibraryFeature(requestModel: RequestModel) {
        if (Utils.hasInternetConnection(mContext)) {
            mLibraryFeatureResponseModel.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = repository.getLibraryFeature(requestModel)
                mLibraryFeatureResponseModel.value = response?.let { handleLibraryFeatureResponse(it) }
            }
        } else mLibraryFeatureResponseModel.value =
            Resource.Error(app.resources.getString(R.string.no_internet))
    }

    private fun handleLibraryFeatureResponse(response: Response<LibraryFeatureResponseModel>): Resource<LibraryFeatureResponseModel> {
        response.body()?.let {
            return when (response.code()) {
                200 -> Resource.Success("Success",it)
                else -> Resource.Error(mContext.resources.getString(R.string.some_thing_went_wrong))
            }
        }
        return Resource.Error(response.message())
    }

    fun getAboutLibraryDetail(requestModel: RequestModel) {
        if (Utils.hasInternetConnection(mContext)) {
            mLibraryDetailResponseModel.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = repository.getAboutLibraryDetail(requestModel)
                mLibraryDetailResponseModel.value = response?.let { handleAboutLibraryDetailResponse(it) }
            }
        } else mLibraryDetailResponseModel.value =
            Resource.Error(app.resources.getString(R.string.no_internet))
    }

    private fun handleAboutLibraryDetailResponse(response: Response<AboutLibraryResponseModel>): Resource<AboutLibraryResponseModel> {
        response.body()?.let {
            return when (response.code()) {
                200 -> Resource.Success("Success",it)
                else -> Resource.Error(mContext.resources.getString(R.string.some_thing_went_wrong))
            }
        }
        return Resource.Error(response.message())
    }
}