package com.bbbdem.koha.module.my_account.purchase_suggestions

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bbbdem.koha.R
import com.bbbdem.koha.module.my_account.purchase_suggestions.model.ItemResponseModel
import com.bbbdem.koha.module.my_account.purchase_suggestions.model.SuggestionListResponseModel
import com.bbbdem.koha.module.my_account.purchase_suggestions.model.SuggestionModel
import com.bbbdem.koha.module.registration.model.AllLibraryResponseModel
import com.bbbdem.koha.network.Resource
import com.bbbdem.koha.utils.Utils
import kotlinx.coroutines.launch
import retrofit2.Response

class SuggestionViewModel(var app: Application) : ViewModel() {
    @SuppressLint("StaticFieldLeak")
    private var mContext: Context = app.applicationContext
    private val repository = SuggestionRepository()
    private var mSuggestionListResponseModel = MutableLiveData<Resource<List<SuggestionListResponseModel>>>()
    var suggestionListResponseModel: LiveData<Resource<List<SuggestionListResponseModel>>> = mSuggestionListResponseModel
    private var mAllLibraryResponseModel = MutableLiveData<Resource<List<AllLibraryResponseModel>>>()
    var allLibraryResponseModel: LiveData<Resource<List<AllLibraryResponseModel>>> = mAllLibraryResponseModel
    private var mItemResponseModel = MutableLiveData<Resource<List<ItemResponseModel>>>()
    var itemResponseModel: LiveData<Resource<List<ItemResponseModel>>> = mItemResponseModel
    private var mSuggestionModel = MutableLiveData<Resource<SuggestionModel>>()
    var suggestionModel: LiveData<Resource<SuggestionModel>> = mSuggestionModel
    private var mDeleteSuggestionModel = MutableLiveData<Resource<String>>()
    var deleteSuggestionModel: LiveData<Resource<String>> = mDeleteSuggestionModel


    fun getSuggestions(suggestedBy:String?) {
        if (Utils.hasInternetConnection(mContext)) {
            mSuggestionListResponseModel.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = repository.getSuggestions(suggestedBy)
                mSuggestionListResponseModel.value = response?.let { handleResetPasswordResponse(it) }
            }
        } else mSuggestionListResponseModel.value =
            Resource.Error(app.resources.getString(R.string.no_internet))
    }

    private fun handleResetPasswordResponse(response: Response<List<SuggestionListResponseModel>>): Resource<List<SuggestionListResponseModel>>? {
            response.body()?.let {
                return when (response.code()) {
                    200 -> Resource.Success("Success",it)
                    else -> Resource.Error(response.message())
                }
            }
        return Resource.Error(response.message())
    }

    fun addSuggestions(suggestionModel: SuggestionModel) {
        if (Utils.hasInternetConnection(mContext)) {
            mSuggestionModel.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = repository.addSuggestions(suggestionModel)
                mSuggestionModel.value = response?.let { handleAddSuggestionsResponse(it) }
            }
        } else mSuggestionModel.value =
            Resource.Error(app.resources.getString(R.string.no_internet))
    }

    private fun handleAddSuggestionsResponse(response: Response<SuggestionModel>): Resource<SuggestionModel>? {
        response.body()?.let {
            return when (response.code()) {
                201 -> Resource.Success("Success",it)
                else -> Resource.Error(response.message())
            }
        }
        return Resource.Error(response.message())
    }

    fun getLibraries() {
        if (Utils.hasInternetConnection(mContext)) {
            mAllLibraryResponseModel.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = repository.getLibraries()
                mAllLibraryResponseModel.value = response?.let { handleLibrariesResponse(it) }
            }
        } else mAllLibraryResponseModel.value =
            Resource.Error(app.resources.getString(R.string.no_internet))
    }

    private fun handleLibrariesResponse(response: Response<List<AllLibraryResponseModel>>): Resource<List<AllLibraryResponseModel>>? {
        response.body()?.let {
            return when (response.code()) {
                200 -> Resource.Success("Success",it)
                else -> Resource.Error(app.resources.getString(R.string.some_thing_went_wrong))
            }
        }
        return Resource.Error(response.message())
    }

    fun getItem() {
        if (Utils.hasInternetConnection(mContext)) {
            mItemResponseModel.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = repository.getItem()
                mItemResponseModel.value = response?.let { handleGetItemResponse(it) }
            }
        } else mItemResponseModel.value =
            Resource.Error(app.resources.getString(R.string.no_internet))
    }

    private fun handleGetItemResponse(response: Response<List<ItemResponseModel>>): Resource<List<ItemResponseModel>>? {
        response.body()?.let {
            return when (response.code()) {
                200 -> Resource.Success("Success",it)
                else -> Resource.Error(app.resources.getString(R.string.some_thing_went_wrong))
            }
        }
        return Resource.Error(response.message())
    }

    fun deleteSuggestions(suggestionId: List<Int>) {
        if (Utils.hasInternetConnection(mContext)) {
            mDeleteSuggestionModel.postValue(Resource.Loading())
            viewModelScope.launch {
                suggestionId.forEach {
                    val response = repository.deleteSuggestions(it)
                    mDeleteSuggestionModel.value = response?.let { handleDeleteSuggestionResponse(it) }
                }
            }
        } else mDeleteSuggestionModel.value =
            Resource.Error(app.resources.getString(R.string.no_internet))
    }

    private fun handleDeleteSuggestionResponse(response: Response<String>): Resource<String> {
            return when (response.code()) {
                204 -> Resource.Success("Success",response.body())
                else -> Resource.Error(app.resources.getString(R.string.some_thing_went_wrong))
            }
    }

}