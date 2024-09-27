package com.bbbdem.koha.module.notification.model

import com.google.gson.annotations.SerializedName

data class NotificationAddResponseModel(@SerializedName("status"  ) var status  : String? = null,
                                        @SerializedName("message" ) var message : String? = null
)
