package com.bbbdem.koha.module.set_new_password

import com.bbbdem.koha.network.RetrofitInstance


/**
 *Created by Gaurav Kumar on 7/28/2022
 * QUYTECH
 */
class SetNewPasswordRepository {
    suspend fun setNewPassword(patronId: Int?,resetPasswordRequest: SetNewPasswordRequest) = RetrofitInstance.apiService?.setNewPassword(patronId,resetPasswordRequest)
}