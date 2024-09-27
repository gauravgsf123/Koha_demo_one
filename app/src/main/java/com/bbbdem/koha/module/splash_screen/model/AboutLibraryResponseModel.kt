package com.bbbdem.koha.module.splash_screen.model

import com.google.gson.annotations.SerializedName

data class AboutLibraryResponseModel(@SerializedName("id"            ) var id           : String? = null,
                                     @SerializedName("library_id"    ) var libraryId    : String? = null,
                                     @SerializedName("logo_url"      ) var logoUrl      : String? = null,
                                     @SerializedName("pri_color"     ) var priColor     : String? = null,
                                     @SerializedName("dark_color"    ) var darkColor    : String? = null,
                                     @SerializedName("opt_color"     ) var optColor     : String? = null,
                                     @SerializedName("about_us"      ) var aboutUs      : String? = null,
                                     @SerializedName("library_rules" ) var libraryRules : String? = null,
                                     @SerializedName("contact_us"    ) var contactUs    : String? = null,
                                     @SerializedName("latitude"      ) var latitude     : String? = null,
                                     @SerializedName("longitude"     ) var longitude    : String? = null)
