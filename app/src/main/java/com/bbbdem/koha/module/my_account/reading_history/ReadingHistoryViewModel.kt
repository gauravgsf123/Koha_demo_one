package com.bbbdem.koha.module.my_account.reading_history

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bbbdem.koha.R
import com.bbbdem.koha.module.my_account.summary.model.CheckoutResponseModel
import com.bbbdem.koha.network.Resource
import com.bbbdem.koha.utils.Utils
import kotlinx.coroutines.launch
import retrofit2.Response

class ReadingHistoryViewModel(var app: Application) : ViewModel() {
    private var mContext: Context = app.applicationContext
    private val repository = ReadingHistoryRepository()
    private var mCheckoutResponseModel = MutableLiveData<Resource<List<CheckoutResponseModel>>>()
    var checkoutResponseModel: LiveData<Resource<List<CheckoutResponseModel>>> = mCheckoutResponseModel

    fun getCheckoutHistory(patrons: String, page: Int, perPage: Int) {
        if (Utils.hasInternetConnection(mContext)) {
            mCheckoutResponseModel.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = repository.getCheckoutHistory(patrons,page,perPage)
                response?.body()?.forEach { it ->
                    val itemResponse = repository.getItemDetail(it.itemId!!)
                    it.itemDetailResponseModel = itemResponse
                    val bookResponse =repository.getBookDetail(itemResponse?.biblioId!!)
                    it.bookDetailResponseModel = bookResponse
                    if(!bookResponse?.isbn.isNullOrEmpty()) {
                        val bookImageResponse =
                            repository.getBookImageDetail("isbn:${Utils.getNumber(bookResponse?.isbn!!)}")
                        it.bookDetailModel = bookImageResponse
                    }
                }
                mCheckoutResponseModel.value = response?.let { handleCheckoutResponse(it) }
            }
        } else mCheckoutResponseModel.value =
            Resource.Error(app.resources.getString(R.string.no_internet))
    }

    private fun handleCheckoutResponse(response: Response<List<CheckoutResponseModel>>): Resource<List<CheckoutResponseModel>>? {
        response.body()?.let {
            return when (response.code()) {
                200 -> Resource.Success(response.headers()["X-Total-Count"],it)
                else -> Resource.Error(app.resources.getString(R.string.some_thing_went_wrong))
            }
        }
        return Resource.Error(response.message())
    }

}