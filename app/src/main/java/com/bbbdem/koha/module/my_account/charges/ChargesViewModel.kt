package com.bbbdem.koha.module.my_account.charges

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bbbdem.koha.R
import com.bbbdem.koha.module.my_account.charges.model.MerchantauthtokenRequest
import com.bbbdem.koha.module.my_account.charges.model.MerchantauthtokenResponse
import com.bbbdem.koha.module.my_account.charges.model.PaymentCreditRequest
import com.bbbdem.koha.module.my_account.charges.model.PaymentCreditResponse
import com.bbbdem.koha.module.my_account.charges.model.UserBillDataRequest
import com.bbbdem.koha.module.my_account.charges.model.UserBillDataResponse
import com.bbbdem.koha.network.Resource
import com.bbbdem.koha.utils.Utils
import kotlinx.coroutines.launch
import retrofit2.Response

class ChargesViewModel(var app: Application) : ViewModel() {
    @SuppressLint("StaticFieldLeak")
    private var mContext: Context = app.applicationContext
    private val repository = ChargesRepository()
    private var mChargesResponseModel = MutableLiveData<Resource<List<ChargesResponseModel>>>()
    var chargesResponseModel: LiveData<Resource<List<ChargesResponseModel>>> = mChargesResponseModel
    private var mPaymentCreditResponse = MutableLiveData<Resource<PaymentCreditResponse>>()
    var paymentCreditResponse: LiveData<Resource<PaymentCreditResponse>> = mPaymentCreditResponse
    private var mMerchantauthtokenResponse = MutableLiveData<Resource<MerchantauthtokenResponse>>()
    var merchantauthtokenResponse: LiveData<Resource<MerchantauthtokenResponse>> = mMerchantauthtokenResponse
    private var mUserBillDataResponse = MutableLiveData<Resource<UserBillDataResponse>>()
    var userBillDataResponse: LiveData<Resource<UserBillDataResponse>> = mUserBillDataResponse


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

    fun paymentCredit(patronId: Int,paymentCreditRequest: PaymentCreditRequest) {
        if (Utils.hasInternetConnection(mContext)) {
            mPaymentCreditResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = repository.paymentCredit(patronId,paymentCreditRequest)
                mPaymentCreditResponse.value = response?.let { handlePaymentCreditResponse(it) }
            }
        } else mPaymentCreditResponse.value =
            Resource.Error(app.resources.getString(R.string.no_internet))
    }

    private fun handlePaymentCreditResponse(response: Response<PaymentCreditResponse>): Resource<PaymentCreditResponse>? {
        response.body()?.let {
            return when (response.code()) {
                201 -> Resource.Success("Success",it)
                else -> Resource.Error(response.message())
            }
        }
        return Resource.Error(response.message())
    }

    fun getMerchantAuthToken(url :String, merchantAuthTokenRequest: MerchantauthtokenRequest) {
        if (Utils.hasInternetConnection(mContext)) {
            mMerchantauthtokenResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = repository.getMerchantAuthToken(url,merchantAuthTokenRequest)
                mMerchantauthtokenResponse.value = response?.let { handleMerchantAuthTokenResponse(it) }
            }
        } else mMerchantauthtokenResponse.value = Resource.Error(app.resources.getString(R.string.no_internet))
    }

    private fun handleMerchantAuthTokenResponse(response: Response<MerchantauthtokenResponse>): Resource<MerchantauthtokenResponse>? {
        response.body()?.let {
            return when (response.code()) {
                200 -> Resource.Success("Success",it)
                else -> Resource.Error(response.message())
            }
        }
        return Resource.Error(response.message())
    }

    fun getUserBillData(url :String, userBillDataRequest: UserBillDataRequest) {
        if (Utils.hasInternetConnection(mContext)) {
            mUserBillDataResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = repository.getUserBillData(url,userBillDataRequest)
                mUserBillDataResponse.value = response?.let { handleUserBillDataResponse(it) }
            }
        } else mUserBillDataResponse.value =
            Resource.Error(app.resources.getString(R.string.no_internet))
    }

    private fun handleUserBillDataResponse(response: Response<UserBillDataResponse>): Resource<UserBillDataResponse>? {
        response.body()?.let {
            return when (response.code()) {
                200 -> Resource.Success("Success",it)
                else -> Resource.Error(response.message())
            }
        }
        return Resource.Error(response.message())
    }

}