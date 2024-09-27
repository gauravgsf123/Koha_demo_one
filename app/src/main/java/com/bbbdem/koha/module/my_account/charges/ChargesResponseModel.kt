package com.bbbdem.koha.module.my_account.charges

import com.google.gson.annotations.SerializedName

data class ChargesResponseModel(@SerializedName("account_line_id"    ) var accountLineId     : Int?    = null,
                                @SerializedName("amount"             ) var amount            : Int?    = null,
                                @SerializedName("amount_outstanding" ) var amountOutstanding : Int?    = null,
                                @SerializedName("cash_register_id"   ) var cashRegisterId    : String? = null,
                                @SerializedName("checkout_id"        ) var checkoutId        : String? = null,
                                @SerializedName("date"               ) var date              : String? = null,
                                @SerializedName("description"        ) var description       : String? = null,
                                @SerializedName("interface"          ) var interfaces         : String? = null,
                                @SerializedName("internal_note"      ) var internalNote      : String? = null,
                                @SerializedName("item_id"            ) var itemId            : Int?    = null,
                                @SerializedName("library_id"         ) var libraryId         : String? = null,
                                @SerializedName("patron_id"          ) var patronId          : Int?    = null,
                                @SerializedName("payout_type"        ) var payoutType        : String? = null,
                                @SerializedName("status"             ) var status            : String? = null,
                                @SerializedName("timestamp"          ) var timestamp         : String? = null,
                                @SerializedName("type"               ) var type              : String? = null,
                                @SerializedName("user_id"            ) var userId            : Int?    = null)
