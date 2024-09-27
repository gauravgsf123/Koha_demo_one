package com.bbbdem.koha.module.my_account.charges.model

import com.google.gson.annotations.SerializedName

data class UserBillDataResponse(@SerializedName("message" ) var message : String?  = null,
                                @SerializedName("error"   ) var error   : Boolean? = null,
                                @SerializedName("data"    ) var data    : String?  = null)
