package com.bbbdem.koha.module.my_account.purchase_suggestions.model

import com.google.gson.annotations.SerializedName

data class ItemResponseModel(@SerializedName("authorised_value_id" ) var authorisedValueId : Int?    = null,
                             @SerializedName("category_name"       ) var categoryName      : String? = null,
                             @SerializedName("description"         ) var description       : String? = null,
                             @SerializedName("image_url"           ) var imageUrl          : String? = null,
                             @SerializedName("opac_description"    ) var opacDescription   : String? = null,
                             @SerializedName("value"               ) var value             : String? = null)
