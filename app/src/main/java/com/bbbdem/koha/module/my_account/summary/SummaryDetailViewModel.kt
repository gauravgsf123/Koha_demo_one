package com.bbbdem.koha.module.my_account.summary

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bbbdem.koha.R
import com.bbbdem.koha.module.my_account.charges.ChargesResponseModel
import com.bbbdem.koha.module.my_account.summary.model.CheckoutResponseModel
import com.bbbdem.koha.module.my_account.summary.model.HoldsResponseModel
import com.bbbdem.koha.module.my_account.summary.model.RenewalResponseModel
import com.bbbdem.koha.network.Resource
import com.bbbdem.koha.utils.Utils
import kotlinx.coroutines.launch
import retrofit2.Response

class SummaryDetailViewModel(var app: Application) : ViewModel() {
    private var mContext: Context = app.applicationContext
    private val repository = SummaryDetailRepository()
    private var mCheckoutResponseModel = MutableLiveData<Resource<List<CheckoutResponseModel>>>()
    var checkoutResponseModel: LiveData<Resource<List<CheckoutResponseModel>>> = mCheckoutResponseModel
    private var mRenewalResponseModel = MutableLiveData<Resource<RenewalResponseModel>>()
    var renewalResponseModel: LiveData<Resource<RenewalResponseModel>> = mRenewalResponseModel
    private var mHoldsResponseModel = MutableLiveData<Resource<List<HoldsResponseModel>>>()
    var holdsResponseModel: LiveData<Resource<List<HoldsResponseModel>>> = mHoldsResponseModel
    private var mCancelHoldResponseModel = MutableLiveData<Resource<String>>()
    var cancelHoldResponseModel: LiveData<Resource<String>> = mCancelHoldResponseModel
    private var mChargesResponseModel = MutableLiveData<Resource<List<ChargesResponseModel>>>()
    var chargesResponseModel: LiveData<Resource<List<ChargesResponseModel>>> = mChargesResponseModel

    fun getCheckout(patrons:String) {
        if (Utils.hasInternetConnection(mContext)) {
            mCheckoutResponseModel.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = repository.getCheckout(patrons)
                if(response?.body()?.isNotEmpty() == true){
                    response?.body()?.forEach { it ->
                        val renewalResponse = repository.checkRenewal(it.checkoutId!!)
                        it.renewalResponseModel = renewalResponse?.body()
                        val itemResponse = repository.getItemDetail(it.itemId!!)
                        it.itemDetailResponseModel = itemResponse
                        val bookResponse =repository.getBookDetail(itemResponse?.biblioId!!)
                        it.bookDetailResponseModel = bookResponse
                        if(!bookResponse?.isbn.isNullOrEmpty()) {
                            val bookImageResponse =
                                repository.getBookDetail("isbn:${bookResponse?.isbn!!}")
                            it.bookDetailModel = bookImageResponse
                        }

                        mCheckoutResponseModel.value = response?.let { handleCheckoutResponse(it) }

                    }
                }else mCheckoutResponseModel.value = response?.let { handleCheckoutResponse(it) }

            }
        } else mCheckoutResponseModel.value =
            Resource.Error(app.resources.getString(R.string.no_internet))
    }

    private fun handleCheckoutResponse(response: Response<List<CheckoutResponseModel>>): Resource<List<CheckoutResponseModel>>? {
        response.body()?.let {
            return when (response.code()) {
                200 -> Resource.Success("Success",it)
                else -> Resource.Error(app.resources.getString(R.string.some_thing_went_wrong))
            }
        }
        return Resource.Error(response.message())
    }

    fun renewal(libraryId: Int) {
        if (Utils.hasInternetConnection(mContext)) {
            mRenewalResponseModel.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = repository.renewal(libraryId)
                mRenewalResponseModel.value = response?.let { handleRenewalResponse(it) }
            }
        } else mRenewalResponseModel.value =
            Resource.Error(app.resources.getString(R.string.no_internet))
    }

    private fun handleRenewalResponse(response: Response<RenewalResponseModel>): Resource<RenewalResponseModel>? {
        response.body()?.let {
            return when (response.code()) {
                201 -> Resource.Success("Success",it)
                else -> Resource.Error(app.resources.getString(R.string.some_thing_went_wrong))
            }
        }
        return Resource.Error(response.message())
    }

    fun getHolds(patrons:String) {
        if (Utils.hasInternetConnection(mContext)) {
            //mHoldsResponseModel.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = repository.getHolds(patrons)
                response?.body()?.forEach {
                    val bookResponse =repository.getBookDetail(it?.biblioId!!)
                    it.bookDetailResponseModel = bookResponse
                    if(!bookResponse?.isbn.isNullOrEmpty()) {
                        val bookImageResponse = repository.getBookDetail("isbn:${bookResponse?.isbn!!}")
                        it.bookDetailModel = bookImageResponse
                    }
                }
                mHoldsResponseModel.value = response?.let { handleHoldsResponse(it) }
            }
        } else mHoldsResponseModel.value =
            Resource.Error(app.resources.getString(R.string.no_internet))
    }

    private fun handleHoldsResponse(response: Response<List<HoldsResponseModel>>): Resource<List<HoldsResponseModel>>? {
        response.body()?.let {
            return when (response.code()) {
                200 -> Resource.Success("Success",it)
                else -> Resource.Error(app.resources.getString(R.string.some_thing_went_wrong))
            }
        }
        return Resource.Error(response.message())
    }

    fun cancelHoldItem(libraryId: Int) {
        if (Utils.hasInternetConnection(mContext)) {
            mCancelHoldResponseModel.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = repository.cancelHoldItem(libraryId)
                mCancelHoldResponseModel.value = response?.let { handleCancelHoldItemResponse(it) }
            }
        } else mCancelHoldResponseModel.value =
            Resource.Error(app.resources.getString(R.string.no_internet))
    }

    private fun handleCancelHoldItemResponse(response: Response<String>): Resource<String>? {
        //response.body()?.let {
            return when (response.code()) {
                204 -> Resource.Success("Success",response.body())
                else -> Resource.Error(app.resources.getString(R.string.some_thing_went_wrong))
            }
        //}
        //return Resource.Error(response.message())
    }

    fun getCharges(patronId: Int) {
        if (Utils.hasInternetConnection(mContext)) {
            mChargesResponseModel.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = repository.getCharges(patronId)
                mChargesResponseModel.value = response?.let { handleGetChargesResponse(it) }
            }
        } else mChargesResponseModel.value =
            Resource.Error(app.resources.getString(R.string.no_internet))
    }

    private fun handleGetChargesResponse(response: Response<List<ChargesResponseModel>>): Resource<List<ChargesResponseModel>>? {
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