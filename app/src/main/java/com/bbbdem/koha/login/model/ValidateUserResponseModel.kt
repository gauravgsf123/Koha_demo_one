package com.bbbdem.koha.login.model

import com.google.gson.annotations.SerializedName

data class ValidateUserResponseModel(@SerializedName("cardnumber" ) var cardnumber : String? = null,
                                     @SerializedName("patron_id"  ) var patronId   : Int?    = null,
                                     @SerializedName("userid"     ) var userid     : String? = null,
                                     @SerializedName("error"     ) var error     : String? = null)
