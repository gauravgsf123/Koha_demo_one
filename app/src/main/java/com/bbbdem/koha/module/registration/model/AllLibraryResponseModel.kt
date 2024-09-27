package com.bbbdem.koha.module.registration.model

import com.google.gson.annotations.SerializedName

data class AllLibraryResponseModel(@SerializedName("address1"          ) var address1        : String?  = null,
                                   @SerializedName("address2"          ) var address2        : String?  = null,
                                   @SerializedName("address3"          ) var address3        : String?  = null,
                                   @SerializedName("city"              ) var city            : String?  = null,
                                   @SerializedName("country"           ) var country         : String?  = null,
                                   @SerializedName("email"             ) var email           : String?  = null,
                                   @SerializedName("fax"               ) var fax             : String?  = null,
                                   @SerializedName("geolocation"       ) var geolocation     : String?  = null,
                                   @SerializedName("illemail"          ) var illemail        : String?  = null,
                                   @SerializedName("ip"                ) var ip              : String?  = null,
                                   @SerializedName("library_id"        ) var libraryId       : String?  = null,
                                   @SerializedName("marc_org_code"     ) var marcOrgCode     : String?  = null,
                                   @SerializedName("name"              ) var name            : String?  = null,
                                   @SerializedName("notes"             ) var notes           : String?  = null,
                                   @SerializedName("phone"             ) var phone           : String?  = null,
                                   @SerializedName("pickup_location"   ) var pickupLocation  : Boolean? = null,
                                   @SerializedName("postal_code"       ) var postalCode      : String?  = null,
                                   @SerializedName("public"            ) var public          : Boolean? = null,
                                   @SerializedName("reply_to_email"    ) var replyToEmail    : String?  = null,
                                   @SerializedName("return_path_email" ) var returnPathEmail : String?  = null,
                                   @SerializedName("state"             ) var state           : String?  = null,
                                   @SerializedName("url"               ) var url             : String?  = null)
