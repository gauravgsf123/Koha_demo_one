package com.bbbdem.koha.module.my_account.personal_detail

import com.bbbdem.koha.network.RetrofitInstance


/**
 *Created by Gaurav Kumar on 7/28/2022
 * QUYTECH
 */
class PersonalDetailRepository {
    suspend fun updatePersonalDetail(patronId: Int?,personalDetailRequestModel: PersonalDetailRequestModel) = RetrofitInstance.apiService?.updatePersonalDetail(patronId,personalDetailRequestModel)
}