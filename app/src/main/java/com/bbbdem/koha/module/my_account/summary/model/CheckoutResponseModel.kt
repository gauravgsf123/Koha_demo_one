package com.bbbdem.koha.module.my_account.summary.model

import com.google.gson.annotations.SerializedName

data class CheckoutResponseModel(@SerializedName("auto_renew"        ) var autoRenew       : Boolean? = null,
                                 @SerializedName("auto_renew_error"  ) var autoRenewError  : String?  = "",
                                 @SerializedName("checkin_date"      ) var checkinDate     : String?  = "",
                                 @SerializedName("checkout_date"     ) var checkoutDate    : String?  = "",
                                 @SerializedName("checkout_id"       ) var checkoutId      : Int?     = null,
                                 @SerializedName("due_date"          ) var dueDate         : String?  = "",
                                 @SerializedName("issuer_id"         ) var issuerId        : String?  = "",
                                 @SerializedName("item_id"           ) var itemId          : Int?     = null,
                                 @SerializedName("last_renewed_date" ) var lastRenewedDate : String?  = "",
                                 @SerializedName("library_id"        ) var libraryId       : String?  = "",
                                 @SerializedName("note"              ) var note            : String?  = "",
                                 @SerializedName("note_date"         ) var noteDate        : String?  = "",
                                 @SerializedName("note_seen"         ) var noteSeen        : Boolean? = null,
                                 @SerializedName("onsite_checkout"   ) var onsiteCheckout  : Boolean? = null,
                                 @SerializedName("patron_id"         ) var patronId        : Int?     = null,
                                 @SerializedName("renewals_count"    ) var renewalsCount   : Int?     = null,
                                 @SerializedName("timestamp"         ) var timestamp       : String?  = "",
                                 @SerializedName("unseen_renewals"   ) var unseenRenewals  : Int?     = null,
                                 var amountOutstanding  : Int?     = null,
                                 var renewalResponseModel: AllowRenewalResponseModel?=null,
                                 var itemDetailResponseModel: ItemDetailResponseModel?=null,
                                 var bookDetailResponseModel: BookDetailResponseModel?=null,
                                 var bookDetailModel: BookDetailModel?=null)