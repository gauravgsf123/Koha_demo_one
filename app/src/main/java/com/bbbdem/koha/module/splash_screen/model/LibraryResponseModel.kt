package com.bbbdem.koha.module.splash_screen.model

import com.google.gson.annotations.SerializedName

data class LibraryResponseModel(@SerializedName("library_id"     ) var libraryId    : String? = null,
                                @SerializedName("library_name"   ) var libraryName  : String? = null,
                                @SerializedName("library_email"  ) var libraryEmail : String? = null,
                                @SerializedName("library_phone"  ) var libraryPhone : String? = null,
                                @SerializedName("opac_url"       ) var opacUrl      : String? = null,
                                @SerializedName("api_endpoint"   ) var apiEndpoint  : String? = null,
                                @SerializedName("api_client_id"  ) var apiClientId  : String? = null,
                                @SerializedName("api_secret_key" ) var apiSecretKey : String? = null,
                                @SerializedName("start_date"     ) var startDate    : String? = null,
                                @SerializedName("end_date"       ) var endDate      : String? = null)
