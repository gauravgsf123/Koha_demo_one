package com.bbbdem.koha.module.my_account.summary.model

import com.google.gson.annotations.SerializedName

data class AllowRenewalResponseModel(@SerializedName("allows_renewal"   ) var allowsRenewal   : Boolean? = null,
                                     @SerializedName("current_renewals" ) var currentRenewals : Int?     = null,
                                     @SerializedName("error"            ) var error           : String?  = "",
                                     @SerializedName("max_renewals"     ) var maxRenewals     : Int?     = null,
                                     @SerializedName("unseen_renewals"  ) var unseenRenewals  : Int?     = null)
