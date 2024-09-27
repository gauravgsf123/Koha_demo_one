package com.bbbdem.koha.module.my_account.charges.model

import com.google.gson.annotations.SerializedName

data class PaymentCreditRequest(
    @SerializedName("credit_type") var creditType: String? = null,
    @SerializedName("amount") var amount: String? = null,
    @SerializedName("library_id") var libraryId: String? = null,
    @SerializedName("payment_type") var paymentType: String? = null,
    @SerializedName("date") var date: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("note") var note: String? = null
)
