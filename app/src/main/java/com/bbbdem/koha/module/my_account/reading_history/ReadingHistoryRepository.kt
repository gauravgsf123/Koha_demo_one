package com.bbbdem.koha.module.my_account.reading_history

import com.bbbdem.koha.network.ApiClient
import com.bbbdem.koha.network.RetrofitInstance


/**
 *Created by Gaurav Kumar on 7/28/2022
 * QUYTECH
 */
class ReadingHistoryRepository {
    suspend fun getCheckoutHistory(patronId: String?,page: Int, perPage: Int) = RetrofitInstance.apiService?.getCheckoutHistory(patronId,true,page,perPage)
    suspend fun checkRenewal(checkoutId: Int) = RetrofitInstance.apiService?.checkRenewal(checkoutId)
    suspend fun getItemDetail(itemId: Int) = RetrofitInstance.apiService?.getItemDetail(itemId)
    suspend fun getBookDetail(biblioId: Int) = RetrofitInstance.apiService?.getBookDetail(biblioId)
    suspend fun getBookImageDetail(isbnNumber: String) = ApiClient.apiService?.getBookImageDetail(isbnNumber)
}