package com.bbbdem.koha.module.dashboard.model

import com.google.gson.annotations.SerializedName

data class PlaceHoldRequestModel(@SerializedName("patron_id"         ) var patronId        : Int?     = 0,
                                 @SerializedName("biblio_id"         ) var biblioId        : Int?     = null,
                                 @SerializedName("pickup_library_id" ) var pickupLibraryId : String?  = "",
                                 @SerializedName("expiration_date"   ) var expirationDate  : String?  = null)
