package com.bbbdem.koha.module.my_account.charges

import com.bbbdem.koha.module.my_account.charges.model.MerchantauthtokenRequest
import com.bbbdem.koha.module.my_account.charges.model.PaymentCreditRequest
import com.bbbdem.koha.module.my_account.charges.model.UserBillDataRequest
import com.bbbdem.koha.network.RetrofitInstance


/**
 *Created by Gaurav Kumar on 7/28/2022
 * QUYTECH
 */
class ChargesRepository {
    suspend fun getCharges(patronId: Int) = RetrofitInstance.apiService?.getCharges(patronId)
    suspend fun paymentCredit(patronId: Int,paymentCreditRequest: PaymentCreditRequest) = RetrofitInstance.apiService?.paymentCredit(patronId,paymentCreditRequest)
    suspend fun getMerchantAuthToken(url :String, merchantAuthTokenRequest: MerchantauthtokenRequest) = RetrofitInstance.apiService?.getMerchantAuthToken(url,merchantAuthTokenRequest)
    suspend fun getUserBillData(url :String, userBillDataRequest: UserBillDataRequest) = RetrofitInstance.apiService?.getUserBillData(url,userBillDataRequest)
}