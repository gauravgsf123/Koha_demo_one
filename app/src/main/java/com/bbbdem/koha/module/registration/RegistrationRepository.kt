package com.bbbdem.koha.module.registration

import com.bbbdem.koha.module.registration.model.RegisterUserRequestModel
import com.bbbdem.koha.network.RetrofitInstance


/**
 *Created by Gaurav Kumar on 7/28/2022
 * QUYTECH
 */
class RegistrationRepository {
    suspend fun registerUser(requestModel: RegisterUserRequestModel) = RetrofitInstance.apiService?.registerUser(requestModel)
    suspend fun getLibraries() = RetrofitInstance.apiService?.getLibraries()
    suspend fun getCategory() = RetrofitInstance.apiService?.getCategory()
    suspend fun getUserDetailByEmail(query: String) = RetrofitInstance.apiService?.getUserDetailByEmail(query)
}