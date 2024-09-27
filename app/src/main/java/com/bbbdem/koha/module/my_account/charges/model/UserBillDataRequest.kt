package com.bbbdem.koha.module.my_account.charges.model

import com.google.gson.annotations.SerializedName

data class UserBillDataRequest(@SerializedName("token"           ) var token           : String?          = null,
                               @SerializedName("timestamp"       ) var timestamp       : String?          = null,
                               @SerializedName("mobile"          ) var mobile          : String?          = null,
                               @SerializedName("user_id"         ) var userId          : String?          = null,
                               @SerializedName("Bill_no"         ) var BillNo          : String?          = null,
                               @SerializedName("Balance"         ) var Balance         : String?          = null,
                               @SerializedName("Bill_due_date"   ) var BillDueDate     : String?          = null,
                               @SerializedName("agent_id"        ) var agentId         : String?          = null,
                               @SerializedName("update_payment"  ) var updatePayment   : String?          = null,
                               @SerializedName("type"            ) var type            : String?          = null,
                               @SerializedName("ekycStatus"      ) var ekycStatus      : String?          = null,
                               @SerializedName("public_key"      ) var publicKey       : String?          = null,
                               @SerializedName("merchant_id"     ) var merchantId      : String?          = null,
                               @SerializedName("caf_number"      ) var cafNumber       : String?          = null,
                               @SerializedName("userInfo"        ) var userInfo        : UserInfo?        = UserInfo(),
                               @SerializedName("billDescription" ) var billDescription : BillDescription? = BillDescription()
){
    data class UserInfo (

        @SerializedName("state"          ) var state        : String? = null,
        @SerializedName("email"          ) var email        : String? = null,
        @SerializedName("gender"         ) var gender       : String? = null,
        @SerializedName("dob"            ) var dob          : String? = null,
        @SerializedName("city"           ) var city         : String? = null,
        @SerializedName("pincode"        ) var pincode      : String? = null,
        @SerializedName("status"         ) var status       : String? = null,
        @SerializedName("first_name"     ) var firstName    : String? = null,
        @SerializedName("last_name"      ) var lastName     : String? = null,
        @SerializedName("address_line_1" ) var addressLine1 : String? = null,
        @SerializedName("address_line_2" ) var addressLine2 : String? = null,
        @SerializedName("plan_name"      ) var planName     : String? = null

    )

    data class BillDescription (

        @SerializedName("Invoice Number" ) var InvoiceNumber : String? = null,
        @SerializedName("Invoice Date"   ) var InvoiceDate   : String? = null,
        @SerializedName("Invoice Amount" ) var InvoiceAmount : String? = null

    )
}
