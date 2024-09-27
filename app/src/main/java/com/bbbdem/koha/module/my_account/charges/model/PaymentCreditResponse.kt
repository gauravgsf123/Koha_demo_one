package com.bbbdem.koha.module.my_account.charges.model

import com.google.gson.annotations.SerializedName

data class PaymentCreditResponse(@SerializedName("account_line_id") var accountLineId: String? = null,
                                 @SerializedName("amount") var amount: String? = null,
                                 @SerializedName("amount_outstanding") var amountOutstanding: String? = null,
                                 @SerializedName("cash_register_id") var cashRegisterId: String? = null,
                                 @SerializedName("checkout_id") var checkoutId: String? = null,
                                 @SerializedName("credit_number") var creditNumber: String? = null,
                                 @SerializedName("credit_type") var creditType: String? = null,
                                 @SerializedName("date") var dates: String? = null,
                                 @SerializedName("debit_type") var debitType: String? = null,
                                 @SerializedName("description") var description: String? = null,
                                 @SerializedName("interface") var interfaces: String? = null,
                                 @SerializedName("internal_note") var internalNote: String? = null,
                                 @SerializedName("item_id") var itemId: String? = null,
                                 @SerializedName("library_id") var libraryId: String? = null,
                                 @SerializedName("patron_id") var patronId: String? = null,
                                 @SerializedName("payment_type") var paymentType: String? = null,
                                 @SerializedName("status") var status: String? = null,
                                 @SerializedName("timestamp") var timestamp: String? = null,
                                 @SerializedName("user_id") var userId: String? = null,
    )
