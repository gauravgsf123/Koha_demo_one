package com.bbbdem.koha.module.my_account.charges.model

import com.google.gson.annotations.SerializedName

data class MerchantauthtokenResponse(@SerializedName("message" ) var message : String?  = null,
                                     @SerializedName("error"   ) var error   : Boolean? = null,
                                     @SerializedName("data"    ) var data    : Data?    = Data()
){
    data class Data (
        @SerializedName("token"     ) var token     : String? = null,
        @SerializedName("timestamp" ) var timestamp : String? = null,
        @SerializedName("ip"        ) var ip        : String? = null)
}
