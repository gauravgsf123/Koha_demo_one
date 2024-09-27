package com.bbbdem.koha.module.dashboard.model

import com.google.gson.annotations.SerializedName

data class PlaceHoldResponseModel(@SerializedName("biblio_id"           ) var biblioId           : Int?     = null,
                                  @SerializedName("cancellation_date"   ) var cancellationDate   : String?  = null,
                                  @SerializedName("cancellation_reason" ) var cancellationReason : String?  = null,
                                  @SerializedName("desk_id"             ) var deskId             : String?  = null,
                                  @SerializedName("expiration_date"     ) var expirationDate     : String?  = null,
                                  @SerializedName("hold_date"           ) var holdDate           : String?  = null,
                                  @SerializedName("hold_id"             ) var holdId             : Int?     = null,
                                  @SerializedName("item_group_id"       ) var itemGroupId        : String?  = null,
                                  @SerializedName("item_id"             ) var itemId             : String?  = null,
                                  @SerializedName("item_level"          ) var itemLevel          : Boolean? = null,
                                  @SerializedName("item_type"           ) var itemType           : String?  = null,
                                  @SerializedName("lowest_priority"     ) var lowestPriority     : Boolean? = null,
                                  @SerializedName("non_priority"        ) var nonPriority        : Boolean? = null,
                                  @SerializedName("notes"               ) var notes              : String?  = null,
                                  @SerializedName("patron_id"           ) var patronId           : Int?     = null,
                                  @SerializedName("pickup_library_id"   ) var pickupLibraryId    : String?  = null,
                                  @SerializedName("priority"            ) var priority           : Int?     = null,
                                  @SerializedName("status"              ) var status             : String?  = null,
                                  @SerializedName("suspended"           ) var suspended          : Boolean? = null,
                                  @SerializedName("suspended_until"     ) var suspendedUntil     : String?  = null,
                                  @SerializedName("timestamp"           ) var timestamp          : String?  = null,
                                  @SerializedName("waiting_date"        ) var waitingDate        : String?  = null)
