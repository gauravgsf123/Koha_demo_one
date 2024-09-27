package com.bbbdem.koha.module.registration.model

import com.google.gson.annotations.SerializedName

data class RegisterUserResponseModel(@SerializedName("address"                 ) var address               : String?  = null,
                                     @SerializedName("anonymized"              ) var anonymized            : Boolean? = null,
                                     @SerializedName("autorenew_checkouts"     ) var autorenewCheckouts    : Boolean? = null,
                                     @SerializedName("cardnumber"              ) var cardnumber            : String?  = null,
                                     @SerializedName("category_id"             ) var categoryId            : String?  = null,
                                     @SerializedName("date_enrolled"           ) var dateEnrolled          : String?  = null,
                                     @SerializedName("date_of_birth"           ) var dateOfBirth           : String?  = null,
                                     @SerializedName("date_renewed"            ) var dateRenewed           : String?  = null,
                                     @SerializedName("email"                   ) var email                 : String?  = null,
                                     @SerializedName("expiry_date"             ) var expiryDate            : String?  = null,
                                     @SerializedName("firstname"               ) var firstname             : String?  = null,
                                     @SerializedName("gender"                  ) var gender                : String?  = null,
                                     @SerializedName("incorrect_address"       ) var incorrectAddress      : Boolean? = null,
                                     @SerializedName("last_seen"               ) var lastSeen              : String?  = null,
                                     @SerializedName("library_id"              ) var libraryId             : String?  = null,
                                     @SerializedName("mobile"                  ) var mobile                : String?  = null,
                                     @SerializedName("patron_card_lost"        ) var patronCardLost        : Boolean? = null,
                                     @SerializedName("patron_id"               ) var patronId              : Int?     = null,
                                     @SerializedName("privacy"                 ) var privacy               : Int?     = null,
                                     @SerializedName("privacy_guarantor_fines" ) var privacyGuarantorFines : Boolean? = null,
                                     @SerializedName("restricted"              ) var restricted            : Boolean? = null,
                                     @SerializedName("surname"                 ) var surname               : String?  = null,
                                     @SerializedName("updated_on"              ) var updatedOn             : String?  = null,
                                     @SerializedName("userid"                  ) var userid                : String?  = null)
