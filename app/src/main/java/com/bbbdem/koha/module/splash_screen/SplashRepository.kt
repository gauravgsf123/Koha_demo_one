package com.bbbdem.koha.module.splash_screen

import com.bbbdem.koha.module.splash_screen.model.LibraryRequestModel
import com.bbbdem.koha.module.splash_screen.model.RequestModel
import com.bbbdem.koha.network.RetrofitInstance
import com.bbbdem.koha.network.RetrofitInstanceForGlobal

class SplashRepository {
    suspend fun getToken() = RetrofitInstance.apiService?.getToken()
    suspend fun getLibraryDetail(requestModel: LibraryRequestModel) = RetrofitInstanceForGlobal.apiService?.getLibraryDetail(requestModel)
    suspend fun getLibraryFeature(requestModel: RequestModel) = RetrofitInstanceForGlobal.apiService?.getLibraryFeature(requestModel)
    suspend fun getAboutLibraryDetail(requestModel: RequestModel) = RetrofitInstanceForGlobal.apiService?.getAboutLibraryDetail(requestModel)

}