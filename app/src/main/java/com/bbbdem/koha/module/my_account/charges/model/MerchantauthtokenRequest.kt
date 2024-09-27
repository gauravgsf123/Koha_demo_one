package com.bbbdem.koha.module.my_account.charges.model

import com.google.gson.annotations.SerializedName

data class MerchantauthtokenRequest(
    @SerializedName("endpoint"    ) var endpoint   : String? = null,
    @SerializedName("key"         ) var key        : String? = null,
    @SerializedName("merchant_id" ) var merchantId : String? = null
)
