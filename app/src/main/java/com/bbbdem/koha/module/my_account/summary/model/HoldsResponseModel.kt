package com.bbbdem.koha.module.my_account.summary.model

import com.google.gson.annotations.SerializedName

data class HoldsResponseModel(@SerializedName("biblio_id"           ) var biblioId           : Int?     = null,
                              @SerializedName("cancellation_date"   ) var cancellationDate   : String?  = "",
                              @SerializedName("cancellation_reason" ) var cancellationReason : String?  = "",
                              @SerializedName("desk_id"             ) var deskId             : String?  = "",
                              @SerializedName("expiration_date"     ) var expirationDate     : String?  = "",
                              @SerializedName("hold_date"           ) var holdDate           : String?  = "",
                              @SerializedName("hold_id"             ) var holdId             : Int?     = null,
                              @SerializedName("item_group_id"       ) var itemGroupId        : String?  = "",
                              @SerializedName("item_id"             ) var itemId             : String?  = "",
                              @SerializedName("item_level"          ) var itemLevel          : Boolean? = null,
                              @SerializedName("item_type"           ) var itemType           : String?  = "",
                              @SerializedName("lowest_priority"     ) var lowestPriority     : Boolean? = null,
                              @SerializedName("non_priority"        ) var nonPriority        : Boolean? = null,
                              @SerializedName("notes"               ) var notes              : String?  = "",
                              @SerializedName("patron_id"           ) var patronId           : Int?     = null,
                              @SerializedName("pickup_library_id"   ) var pickupLibraryId    : String?  = "",
                              @SerializedName("priority"            ) var priority           : Int?     = null,
                              @SerializedName("status"              ) var status             : String?  = "",
                              @SerializedName("suspended"           ) var suspended          : Boolean? = null,
                              @SerializedName("suspended_until"     ) var suspendedUntil     : String?  = "",
                              @SerializedName("timestamp"           ) var timestamp          : String?  = "",
                              @SerializedName("waiting_date"        ) var waitingDate        : String?  = "",
                              var bookDetailResponseModel: BookDetailResponseModel?=null,
                              var bookDetailModel: BookDetailModel?=null)
