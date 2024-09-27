package com.bbbdem.koha.module.dashboard

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bbbdem.koha.R
import com.bbbdem.koha.login.model.UserDetailResponseModel
import com.bbbdem.koha.module.dashboard.model.CirculatingBooksResponseModel
import com.bbbdem.koha.module.dashboard.model.ItemListOfBookResponseModel
import com.bbbdem.koha.module.dashboard.model.PlaceHoldRequestModel
import com.bbbdem.koha.module.dashboard.model.PlaceHoldResponseModel
import com.bbbdem.koha.module.my_account.purchase_suggestions.model.ItemResponseModel
import com.bbbdem.koha.module.my_account.summary.model.BookDetailResponseModel
import com.bbbdem.koha.module.my_account.summary.model.CheckoutResponseModel
import com.bbbdem.koha.module.notification.model.NotificationAddResponseModel
import com.bbbdem.koha.module.notification.model.NotificationModel
import com.bbbdem.koha.module.registration.model.AllCategoryResponseModel
import com.bbbdem.koha.module.registration.model.AllLibraryResponseModel
import com.bbbdem.koha.network.Resource
import com.bbbdem.koha.utils.Utils
import kotlinx.coroutines.launch
import retrofit2.Response

class DashboardViewModel(var app: Application) : ViewModel() {
    @SuppressLint("StaticFieldLeak")
    private var mContext: Context = app.applicationContext
    private val repository = DashboardRepository()
    private var mBooksListResponseModel = MutableLiveData<Resource<List<BookDetailResponseModel>>>()
    var booksListResponseModel: LiveData<Resource<List<BookDetailResponseModel>>> = mBooksListResponseModel
    private var mCirculatingBooksResponseModel = MutableLiveData<Resource<List<CirculatingBooksResponseModel>>>()
    var circulatingBooksResponseModel: LiveData<Resource<List<CirculatingBooksResponseModel>>> = mCirculatingBooksResponseModel
    private var mCheckoutResponseModel = MutableLiveData<Resource<List<CheckoutResponseModel>>>()
    var checkoutResponseModel: LiveData<Resource<List<CheckoutResponseModel>>> = mCheckoutResponseModel
    private var mItemListOfBookResponseModel = MutableLiveData<Resource<List<ItemListOfBookResponseModel>>>()
    var itemListOfBookResponseModel: LiveData<Resource<List<ItemListOfBookResponseModel>>> = mItemListOfBookResponseModel
    private var mAllLibraryResponseModel = MutableLiveData<Resource<List<AllLibraryResponseModel>>>()
    var allLibraryResponseModel: LiveData<Resource<List<AllLibraryResponseModel>>> = mAllLibraryResponseModel
    private var mAllCategoryResponseModel = MutableLiveData<Resource<List<AllCategoryResponseModel>>>()
    var allCategoryResponseModel: LiveData<Resource<List<AllCategoryResponseModel>>> = mAllCategoryResponseModel
    private var mPlaceHoldResponseModel = MutableLiveData<Resource<PlaceHoldResponseModel>>()
    var placeHoldResponseModel: LiveData<Resource<PlaceHoldResponseModel>> = mPlaceHoldResponseModel
    private var mSearchBookList = MutableLiveData<Resource<List<BookDetailResponseModel>>?>()
    var searchBookList: MutableLiveData<Resource<List<BookDetailResponseModel>>?> = mSearchBookList
    private var bookList : MutableList<BookDetailResponseModel> = mutableListOf()
    private var mSearchBookItem = MutableLiveData<Resource<List<CirculatingBooksResponseModel>>>()
    var searchBookItem: LiveData<Resource<List<CirculatingBooksResponseModel>>> = mSearchBookItem
    private var mItemResponseModel = MutableLiveData<Resource<List<ItemResponseModel>>>()
    var itemResponseModel: LiveData<Resource<List<ItemResponseModel>>> = mItemResponseModel
    private var mCheckoutOfBiblioResponseModel = MutableLiveData<Resource<List<CheckoutResponseModel>>>()
    var checkoutOfBiblioResponseModel: LiveData<Resource<List<CheckoutResponseModel>>> = mCheckoutOfBiblioResponseModel
    private var mTotalPatrons = MutableLiveData<Resource<List<UserDetailResponseModel>>>()
    var totalPatrons: LiveData<Resource<List<UserDetailResponseModel>>> = mTotalPatrons
    private var mNotificationAddResponseModel = MutableLiveData<Resource<NotificationAddResponseModel>>()
    var notificationAddResponseModel: LiveData<Resource<NotificationAddResponseModel>> = mNotificationAddResponseModel



    fun getBookList(orderBy: String,page: Int, perPage: Int) {
        if (Utils.hasInternetConnection(mContext)) {
            mBooksListResponseModel.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = repository.getBookList(orderBy,page,perPage)
                response?.body()?.forEach { it ->
                   /* val itemList = repository.getItemListForBook(it.biblioId!!,1,100)
                    if (itemList != null) {
                        it.itemListOfBookResponseModel = itemList.body()
                        it.itemListOfBookResponseModel?.forEach {item->
                            val checkoutResponseModel = repository.getCheckoutOfBiblio(item.biblioId!!)
                            if(checkoutResponseModel?.isNotEmpty() == true) item.checkoutResponseModel = checkoutResponseModel[0]
                            //item.checkoutResponseModel = checkoutResponseModel?.get(0)
                        }
                    }*/
                    if(!it.isbn.isNullOrEmpty()) {
                        val bookImageResponse =repository.getBookImageDetail("isbn:${Utils.getNumber(it?.isbn!!)}")
                        it.bookDetailModel = bookImageResponse
                    }
                }
                mBooksListResponseModel.value = response?.let { handleBookListResponse(it) }
            }
        } else mBooksListResponseModel.value =
            Resource.Error(app.resources.getString(R.string.no_internet))
    }

    private fun handleBookListResponse(response: Response<List<BookDetailResponseModel>>): Resource<List<BookDetailResponseModel>>? {
            response.body()?.let {
                return when (response.code()) {
                    200 -> Resource.Success("Success",it)
                    else -> Resource.Error(response.message())
                }
            }
        return Resource.Error(response.message())
    }

    fun getCirculatingBookList(orderBy: String,page: Int, perPage: Int) {
        if (Utils.hasInternetConnection(mContext)) {
            mCirculatingBooksResponseModel.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = repository.getCirculatingBookList(orderBy,page,perPage)
                response?.body()?.forEach { it ->
                    val bookResponse =repository.getBookDetail(it.biblioId!!)
                    it.bookDetailResponseModel = bookResponse
                    if(!bookResponse?.isbn.isNullOrEmpty()) {
                        val bookImageResponse =
                            repository.getBookImageDetail("isbn:${Utils.getNumber(bookResponse?.isbn!!)}")
                        it.bookDetailResponseModel?.bookDetailModel = bookImageResponse
                    }
                }
                mCirculatingBooksResponseModel.value = response?.let { handleCirculatingBookListResponse(it) }
            }
        } else mCirculatingBooksResponseModel.value =
            Resource.Error(app.resources.getString(R.string.no_internet))
    }

    private fun handleCirculatingBookListResponse(response: Response<List<CirculatingBooksResponseModel>>): Resource<List<CirculatingBooksResponseModel>>? {
        response.body()?.let {
            return when (response.code()) {
                200 -> Resource.Success(response.headers()["X-Total-Count"],it)
                else -> Resource.Error(response.message())
            }
        }
        return Resource.Error(response.message())
    }

    fun getBorrowedBook(patronId: Int?,orderBy: String,checkedIn: Boolean,page: Int, perPage: Int) {
        if (Utils.hasInternetConnection(mContext)) {
            mCheckoutResponseModel.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = repository.getBorrowedBook(patronId,orderBy,checkedIn,page,perPage)
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

    fun getItemListForBook(biblioId: Int?, page: Int, perPage: Int) {
        if (Utils.hasInternetConnection(mContext)) {
            mItemListOfBookResponseModel.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = repository.getItemListForBook(biblioId!!,page,perPage)
                response?.body()?.forEach { it ->
                    val itemResponse = repository.getItemDetail(it.itemId!!)
                    it.itemDetailResponseModel = itemResponse
                    val checkoutResponseModel = repository.getCheckoutOfBiblio(itemResponse?.biblioId!!)
                    if(checkoutResponseModel?.isNotEmpty() == true) it.checkoutResponseModel = checkoutResponseModel[0]
                }
                mItemListOfBookResponseModel.value = response?.let { handleItemListResponse(it) }
            }
        } else mItemListOfBookResponseModel.value =
            Resource.Error(app.resources.getString(R.string.no_internet))
    }

    private fun handleItemListResponse(response: Response<List<ItemListOfBookResponseModel>>): Resource<List<ItemListOfBookResponseModel>>? {
        response.body()?.let {
            return when (response.code()) {
                200 -> Resource.Success("Success",it)
                else -> Resource.Error(response.message())
            }
        }
        return Resource.Error(response.message())
    }

    fun getLibraries() {
        if (Utils.hasInternetConnection(mContext)) {
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

    fun getCategory() {
        if (Utils.hasInternetConnection(mContext)) {
            viewModelScope.launch {
                val response = repository.getCategory()
                mAllCategoryResponseModel.value = response?.let { handleCategoryResponse(it) }
            }
        } else mAllCategoryResponseModel.value =
            Resource.Error(app.resources.getString(R.string.no_internet))
    }

    private fun handleCategoryResponse(response: Response<List<AllCategoryResponseModel>>): Resource<List<AllCategoryResponseModel>>? {
        response.body()?.let {
            return when (response.code()) {
                200 -> Resource.Success("Success",it)
                else -> Resource.Error(app.resources.getString(R.string.some_thing_went_wrong))
            }
        }
        return Resource.Error(response.message())
    }


    fun placeHold(placeHoldRequestModel: PlaceHoldRequestModel) {
        if (Utils.hasInternetConnection(mContext)) {
            mPlaceHoldResponseModel.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = repository.placeHold(placeHoldRequestModel)
                mPlaceHoldResponseModel.value = response?.let { handlePlaceHoldResponse(it) }
            }
        } else mPlaceHoldResponseModel.value =
            Resource.Error(app.resources.getString(R.string.no_internet))
    }

    private fun handlePlaceHoldResponse(response: Response<PlaceHoldResponseModel>): Resource<PlaceHoldResponseModel>? {
        response.body()?.let {
            return when (response.code()) {
                201 -> Resource.Success("Success",it)
                else -> Resource.Error(response.message())
            }
        }
        return Resource.Error(response.message())
    }

    fun searchBookList(query: String, library: String?, category: String?,page: Int, perPage: Int) {
        if (Utils.hasInternetConnection(mContext)) {
            mSearchBookList.postValue(null)
            bookList.clear()
            mSearchBookList.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = repository.searchBookList(query,page,perPage)
                response?.body()?.forEach { it ->
                    //val query = "{\"-and\":[{\"home_library_id\":{\"-like\":\"%${library}%\"}},{\"item_type_id\":{\"-like\":\"%${category}%\"}}]}"
                    if(!library.isNullOrEmpty() && !category.isNullOrEmpty()){
                        val query = "{\"home_library_id\":\"${library}\",\"item_type_id\":\"${category}\"}"
                        Log.d("searchBookList",query)
                        val itemResponse = repository.getItemOfBiblio(it.biblioId!!,query)
                        if(itemResponse?.body()?.isNotEmpty() == true){
                            bookList.add(it)
                            it.itemListOfBookResponseModel = itemResponse.body()
                            if(!it.isbn.isNullOrEmpty()) {
                                val bookImageResponse =repository.getBookImageDetail("isbn:${Utils.getNumber(it.isbn!!)}")
                                it.bookDetailModel = bookImageResponse
                            }
                        }
                    }else if(!library.isNullOrEmpty() && category.isNullOrEmpty()){
                        val query = "{\"home_library_id\":\"${library}\"}"
                        Log.d("searchBookList",query)
                        val itemResponse = repository.getItemOfBiblio(it.biblioId!!,query)
                        if(itemResponse?.body()?.isNotEmpty() == true){
                            bookList.add(it)
                            it.itemListOfBookResponseModel = itemResponse.body()
                            if(!it.isbn.isNullOrEmpty()) {
                                val bookImageResponse =repository.getBookImageDetail("isbn:${Utils.getNumber(it.isbn!!)}")
                                it.bookDetailModel = bookImageResponse
                            }
                        }
                    }else if(library.isNullOrEmpty() && !category.isNullOrEmpty()){
                        val query = "{\"item_type_id\":\"${category}\"}"
                        Log.d("searchBookList",query)
                        val itemResponse = repository.getItemOfBiblio(it.biblioId!!,query)
                        if(itemResponse?.body()?.isNotEmpty() == true){
                            bookList.add(it)
                            it.itemListOfBookResponseModel = itemResponse.body()
                            if(!it.isbn.isNullOrEmpty()) {
                                val bookImageResponse =repository.getBookImageDetail("isbn:${Utils.getNumber(it.isbn!!)}")
                                it.bookDetailModel = bookImageResponse
                            }
                        }
                    }else {
                        bookList.add(it)
                        if(!it.isbn.isNullOrEmpty()) {
                            val bookImageResponse =repository.getBookImageDetail("isbn:${Utils.getNumber(it.isbn!!)}")
                            it.bookDetailModel = bookImageResponse
                        }
                    }
                }
                /*bookList.forEach {
                    val itemList = repository.getItemListForBook(it.biblioId!!,1,100)
                    if (itemList != null) {
                        it.itemListOfBookResponseModel = itemList.body()
                        it.itemListOfBookResponseModel?.forEach {item->
                            val checkoutResponseModel = repository.getCheckoutOfBiblio(item.biblioId!!)
                            if(checkoutResponseModel?.isNotEmpty() == true) item.checkoutResponseModel = checkoutResponseModel[0]
                        }
                    }
                }*/
                mSearchBookList.value = response?.let { handleSearchBookListResponse(bookList) }
            }
        } else mSearchBookList.value =
            Resource.Error(app.resources.getString(R.string.no_internet))
    }

    private fun handleSearchBookListResponse(response: List<BookDetailResponseModel>): Resource<List<BookDetailResponseModel>> {
        return if(response.isNotEmpty()){
            Resource.Success("Success",response)
        }else Resource.Error(app.resources.getString(R.string.no_data_found))
    }

    fun searchBookItem(query: String,page: Int, perPage: Int) {
        if (Utils.hasInternetConnection(mContext)) {
            mSearchBookList.postValue(null)
            bookList.clear()
            mSearchBookList.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = repository.searchBookItem(query,page,perPage)
                response?.body()?.forEach { it ->
                    val bookResponse =repository.getBookDetail(it.biblioId!!)
                    it.bookDetailResponseModel = bookResponse
                    if(!bookResponse?.isbn.isNullOrEmpty()) {
                        val bookImageResponse =
                            repository.getBookImageDetail("isbn:${Utils.getNumber(bookResponse?.isbn!!)}")
                        bookResponse.bookDetailModel = bookImageResponse
                    }
                    bookList.add(bookResponse!!)
                }
                /*bookList.forEach {
                    val itemList = repository.getItemListForBook(it.biblioId!!,1,100)
                    if (itemList != null) {
                        it.itemListOfBookResponseModel = itemList.body()
                        it.itemListOfBookResponseModel?.forEach {item->
                            val checkoutResponseModel = repository.getCheckoutOfBiblio(item.biblioId!!)
                            if(checkoutResponseModel?.isNotEmpty() == true) item.checkoutResponseModel = checkoutResponseModel[0]
                        }
                    }
                }*/
                mSearchBookList.value = response?.let { handleSearchBookListResponse(bookList) }
            }
        } else mSearchBookList.value =
            Resource.Error(app.resources.getString(R.string.no_internet))
    }

    fun getAllPatrons() {
        if (Utils.hasInternetConnection(mContext)) {
            mTotalPatrons.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = repository.getAllPatrons()
                mTotalPatrons.value = response?.let { handleAllPatronsResponse(it) }
            }
        } else mTotalPatrons.value =
            Resource.Error(app.resources.getString(R.string.no_internet))
    }

    private fun handleAllPatronsResponse(response: Response<List<UserDetailResponseModel>>): Resource<List<UserDetailResponseModel>>? {
        //if (response.isSuccessful) {
        response.body()?.let {
            return when (response.code()) {
                200 -> Resource.Success(response.headers()["X-Total-Count"],it)
                else -> Resource.Error(app.getString(R.string.some_thing_went_wrong))
            }
        }
        //}
        return Resource.Error(response.message())
    }

    fun getCheckout(patrons:String) {
        if (Utils.hasInternetConnection(mContext)) {
            mCheckoutResponseModel.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = repository.getCheckout(patrons)
                response?.body()?.forEach { it ->
                    val itemResponse = repository.getItemDetail(it.itemId!!)
                    it.itemDetailResponseModel = itemResponse
                    val bookResponse =repository.getBookDetail(itemResponse?.biblioId!!)
                    it.bookDetailResponseModel = bookResponse

                }
                mCheckoutResponseModel.value = response?.let { handleCheckoutResponse(it) }
            }
        } else mCheckoutResponseModel.value =
            Resource.Error(app.resources.getString(R.string.no_internet))
    }

    fun addNotification(notificationRequestModel: NotificationModel) {
        if (Utils.hasInternetConnection(mContext)) {
            mNotificationAddResponseModel.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = repository.addNotification(notificationRequestModel)
                mNotificationAddResponseModel.value = response?.let { handleAddNotificationResponse(it) }
            }
        } else mNotificationAddResponseModel.value =
            Resource.Error(app.resources.getString(R.string.no_internet))
    }

    private fun handleAddNotificationResponse(response: Response<NotificationAddResponseModel>): Resource<NotificationAddResponseModel>? {
        response.body()?.let {
            return when (response.code()) {
                200 -> Resource.Success("Success",it)
                else -> Resource.Error(response.message())
            }
        }
        return Resource.Error(response.message())
    }



}