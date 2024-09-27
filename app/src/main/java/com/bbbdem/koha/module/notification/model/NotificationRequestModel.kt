package com.bbbdem.koha.module.notification.model

import com.google.gson.annotations.SerializedName

data class NotificationRequestModel(@SerializedName("user_id"          ) var userId        : String?  = null,
                                    @SerializedName("library_id"          ) var libraryId        : String?  = null)