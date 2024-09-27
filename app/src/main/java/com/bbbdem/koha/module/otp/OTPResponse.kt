package com.bbbdem.koha.module.otp

import com.google.gson.annotations.SerializedName

data class OTPResponse(@SerializedName("request_id" ) var requestId : String? = null,
                       @SerializedName("type"       ) var type      : String? = null)
