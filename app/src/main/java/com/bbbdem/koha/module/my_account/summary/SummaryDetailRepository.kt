package com.bbbdem.koha.module.my_account.summary

import com.bbbdem.koha.network.ApiClient
import com.bbbdem.koha.network.RetrofitInstance


/**
 *Created by Gaurav Kumar on 7/28/2022
 * QUYTECH
 */
class SummaryDetailRepository {
    suspend fun getCheckout(patronId: String?) = RetrofitInstance.apiService?.getCheckout(patronId)
    suspend fun checkRenewal(checkoutId: Int) = RetrofitInstance.apiService?.checkRenewal(checkoutId)
    suspend fun getItemDetail(itemId: Int) = RetrofitInstance.apiService?.getItemDetail(itemId)
    suspend fun getBookDetail(biblioId: Int) = RetrofitInstance.apiService?.getBookDetail(biblioId)
    suspend fun getBookDetail(isbnNumber: String) = ApiClient.apiService?.getBookImageDetail(isbnNumber)
    suspend fun renewal(checkoutId: Int) = RetrofitInstance.apiService?.renewal(checkoutId)
    suspend fun getHolds(patronId: String?) = RetrofitInstance.apiService?.getHolds(patronId)
    suspend fun cancelHoldItem(holdsId: Int) = RetrofitInstance.apiService?.cancelHoldItem(holdsId)
    suspend fun getCharges(patronId: Int) = RetrofitInstance.apiService?.getCharges(patronId)
}