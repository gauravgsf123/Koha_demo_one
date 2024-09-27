package com.bbbdem.koha.module.splash_screen.model

import com.google.gson.annotations.SerializedName

data class LibraryFeatureResponseModel(@SerializedName("id"              ) var id             : String? = null,
                                       @SerializedName("library_id"      ) var libraryId      : String? = null,
                                       @SerializedName("sms"             ) var sms            : String? = null,
                                       @SerializedName("payment_gateway" ) var paymentGateway : String? = null,
                                       @SerializedName("discharge"       ) var discharge      : String? = null,
                                       @SerializedName("renew"           ) var renew          : String? = null,
                                       @SerializedName("hold"            ) var hold           : String? = null)
