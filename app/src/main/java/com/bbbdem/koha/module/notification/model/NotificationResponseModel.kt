package com.bbbdem.koha.module.notification.model

import com.google.gson.annotations.SerializedName

data class NotificationResponseModel(@SerializedName("status" ) var status : String?         = null,
                                     @SerializedName("data"   ) var data   : ArrayList<Data> = arrayListOf()){
    data class Data (
        @SerializedName("id"         ) var id        : String? = null,
        @SerializedName("library_id" ) var libraryId : String? = null,
        @SerializedName("user_id"    ) var userId    : String? = null,
        @SerializedName("book_id"    ) var bookId    : String? = null,
        @SerializedName("title"      ) var title     : String? = null,
        @SerializedName("message"    ) var message   : String? = null,
        @SerializedName("date_time"  ) var dateTime  : String? = null,
        @SerializedName("status"     ) var status    : String? = null,
        @SerializedName("count"      ) var count     : String? = null

    )
}
