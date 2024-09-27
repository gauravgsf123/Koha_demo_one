package com.bbbdem.koha.module.my_account.change_password

import com.bbbdem.koha.network.RetrofitInstance


/**
 *Created by Gaurav Kumar on 7/28/2022
 * QUYTECH
 */
class ChangePasswordRepository {
    suspend fun resetPassword(patronId: Int?,changePasswordRequest: ChangePasswordRequest) = RetrofitInstance.apiService?.resetPassword(patronId,changePasswordRequest)
}