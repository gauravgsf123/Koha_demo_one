package com.bbbdem.koha.module.my_account.personal_detail

import com.google.gson.annotations.SerializedName

data class PersonalDetailRequestModel(@SerializedName("address"         ) var address        : String? = null,
                                      @SerializedName("address2"        ) var address2       : String? = null,
                                      @SerializedName("cardnumber"      ) var cardnumber     : String? = null,
                                      @SerializedName("category_id"     ) var categoryId     : String? = null,
                                      @SerializedName("city"            ) var city           : String? = null,
                                      @SerializedName("country"         ) var country        : String? = null,
                                      @SerializedName("date_enrolled"   ) var dateEnrolled   : String? = null,
                                      @SerializedName("date_of_birth"   ) var dateOfBirth    : String? = null,
                                      @SerializedName("date_renewed"    ) var dateRenewed    : String? = null,
                                      @SerializedName("email"           ) var email          : String? = null,
                                      @SerializedName("expiry_date"     ) var expiryDate     : String? = null,
                                      @SerializedName("firstname"       ) var firstname      : String? = null,
                                      @SerializedName("gender"          ) var gender         : String? = null,
                                      @SerializedName("library_id"      ) var libraryId      : String? = null,
                                      @SerializedName("middle_name"     ) var middleName     : String? = null,
                                      @SerializedName("phone"           ) var phone          : String? = null,
                                      @SerializedName("postal_code"     ) var postalCode     : String? = null,
                                      @SerializedName("privacy"         ) var privacy        : Int?    = null,
                                      @SerializedName("secondary_email" ) var secondaryEmail : String? = null,
                                      @SerializedName("secondary_phone" ) var secondaryPhone : String? = null,
                                      @SerializedName("state"           ) var state          : String? = null,
                                      @SerializedName("surname"         ) var surname        : String? = null,
                                      @SerializedName("title"           ) var title          : String? = null,
                                      @SerializedName("userid"          ) var userid         : String? = null)
