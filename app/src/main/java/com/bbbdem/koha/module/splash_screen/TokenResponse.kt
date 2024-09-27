package com.bbbdem.koha.module.splash_screen

import com.google.gson.annotations.SerializedName

data class TokenResponse(@SerializedName("access_token" ) var accessToken : String? = null,
                         @SerializedName("expires_in"   ) var expiresIn   : Int?    = null,
                         @SerializedName("token_type"   ) var tokenType   : String? = null)
