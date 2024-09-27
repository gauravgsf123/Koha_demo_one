package com.bbbdem.koha.module.dashboard

import com.bbbdem.koha.module.dashboard.model.PlaceHoldRequestModel
import com.bbbdem.koha.module.notification.model.NotificationModel
import com.bbbdem.koha.module.notification.model.NotificationRequestModel
import com.bbbdem.koha.network.ApiClient
import com.bbbdem.koha.network.RetrofitInstance
import com.bbbdem.koha.network.RetrofitInstanceForGlobal


/**
 *Created by Gaurav Kumar on 7/28/2022
 * QUYTECH
 */
class DashboardRepository {
    suspend fun getBookList(orderBy: String,page: Int, perPage: Int) = RetrofitInstance.apiService?.getBookList(orderBy,page,perPage)
    suspend fun getCirculatingBookList(orderBy: String,page: Int, perPage: Int) = RetrofitInstance.apiService?.getCirculatingBookList(orderBy,page,perPage)
    suspend fun getBookDetail(biblioId: Int) = RetrofitInstance.apiService?.getBookDetail(biblioId)
    suspend fun getBookImageDetail(isbnNumber: String) = ApiClient.apiService?.getBookImageDetail(isbnNumber)
    suspend fun getItemDetail(itemId: Int) = RetrofitInstance.apiService?.getItemDetail(itemId)
    suspend fun getItemListForBook(biblioId: Int, page: Int, perPage: Int) = RetrofitInstance.apiService?.getItemListForBook(biblioId,page,perPage)
    suspend fun getLibraries() = RetrofitInstance.apiService?.getLibraries()
    suspend fun getItem() = RetrofitInstance.apiService?.getItem()
    suspend fun getCategory() = RetrofitInstance.apiService?.getCategory()
    suspend fun placeHold(placeHoldRequestModel: PlaceHoldRequestModel) = RetrofitInstance.apiService?.placeHold(placeHoldRequestModel)
    suspend fun getBorrowedBook(patronId: Int?,orderBy: String,checkedIn: Boolean,page: Int, perPage: Int) = RetrofitInstance.apiService?.getBorrowedBook(patronId,orderBy,checkedIn,page,perPage)
    suspend fun searchBookList(query: String,page: Int, perPage: Int) = RetrofitInstance.apiService?.searchBookList(query,page,perPage)
    suspend fun getItemOfBiblio(biblioId: Int,query: String) = RetrofitInstance.apiService?.getItemOfBiblio(biblioId,query)
    suspend fun searchBookItem(query: String,page: Int, perPage: Int) = RetrofitInstance.apiService?.searchBookItem(query,page,perPage)
    suspend fun getCheckoutOfBiblio(biblioId: Int) = RetrofitInstance.apiService?.getCheckoutOfBiblio(biblioId)
    suspend fun getAllPatrons() = RetrofitInstance.apiService?.getAllPatrons()
    suspend fun getCheckout(patronId: String?) = RetrofitInstance.apiService?.getCheckout(patronId)
    suspend fun addNotification(notificationRequestModel: NotificationModel) = RetrofitInstanceForGlobal.apiService?.addNotification(notificationRequestModel)
    suspend fun getNotification(notificationRequestModel: NotificationRequestModel) = RetrofitInstanceForGlobal.apiService?.getNotification(notificationRequestModel)
}