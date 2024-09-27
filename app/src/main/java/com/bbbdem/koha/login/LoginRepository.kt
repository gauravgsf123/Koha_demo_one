package com.bbbdem.koha.login

import com.bbbdem.koha.login.model.ValidateUserRequestModel
import com.bbbdem.koha.network.RetrofitInstance


/**
 *Created by Gaurav Kumar on 7/28/2022
 * QUYTECH
 */
class LoginRepository {
    suspend fun validateUser(validateUserRequestModel: ValidateUserRequestModel) = RetrofitInstance.apiService?.validateUser(validateUserRequestModel)
    suspend fun getUserDetail(patronId: Int?) = RetrofitInstance.apiService?.getUserDetail(patronId)
    suspend fun getLibraryDetail(libraryId: String?) = RetrofitInstance.apiService?.getLibraryDetail(libraryId)
    suspend fun register(body:Map<String,String>) = RetrofitInstance.apiService?.register(body)
}