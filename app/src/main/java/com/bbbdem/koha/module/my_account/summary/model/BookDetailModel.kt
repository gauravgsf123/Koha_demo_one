package com.bbbdem.koha.module.my_account.summary.model

import com.google.gson.annotations.SerializedName

data class BookDetailModel(@SerializedName("kind"       ) var kind       : String?          = "",
                           @SerializedName("totalItems" ) var totalItems : Int?             = null,
                           @SerializedName("items"      ) var items      : ArrayList<Items> = arrayListOf()){
    data class Items (
        @SerializedName("kind"       ) var kind       : String?     = "",
        @SerializedName("id"         ) var id         : String?     = "",
        @SerializedName("etag"       ) var etag       : String?     = "",
        @SerializedName("selfLink"   ) var selfLink   : String?     = "",
        @SerializedName("volumeInfo" ) var volumeInfo : VolumeInfo? = VolumeInfo(),
        @SerializedName("saleInfo"   ) var saleInfo   : SaleInfo?   = SaleInfo(),
        @SerializedName("accessInfo" ) var accessInfo : AccessInfo? = AccessInfo(),
        @SerializedName("searchInfo" ) var searchInfo : SearchInfo? = SearchInfo()
    )

    data class VolumeInfo (
        @SerializedName("title"               ) var title               : String?                        = "",
        @SerializedName("authors"             ) var authors             : ArrayList<String>              = arrayListOf(),
        @SerializedName("publisher"           ) var publisher           : String?                        = "",
        @SerializedName("publishedDate"       ) var publishedDate       : String?                        = "",
        @SerializedName("description"         ) var description         : String?                        = "",
        @SerializedName("industryIdentifiers" ) var industryIdentifiers : ArrayList<IndustryIdentifiers> = arrayListOf(),
        @SerializedName("readingModes"        ) var readingModes        : ReadingModes?                  = ReadingModes(),
        @SerializedName("pageCount"           ) var pageCount           : Int?                           = null,
        @SerializedName("printType"           ) var printType           : String?                        = "",
        @SerializedName("categories"          ) var categories          : ArrayList<String>              = arrayListOf(),
        @SerializedName("maturityRating"      ) var maturityRating      : String?                        = "",
        @SerializedName("allowAnonLogging"    ) var allowAnonLogging    : Boolean?                       = null,
        @SerializedName("contentVersion"      ) var contentVersion      : String?                        = "",
        @SerializedName("panelizationSummary" ) var panelizationSummary : PanelizationSummary?           = PanelizationSummary(),
        @SerializedName("imageLinks"          ) var imageLinks          : ImageLinks?                    = ImageLinks(),
        @SerializedName("language"            ) var language            : String?                        = "",
        @SerializedName("previewLink"         ) var previewLink         : String?                        = "",
        @SerializedName("infoLink"            ) var infoLink            : String?                        = "",
        @SerializedName("canonicalVolumeLink" ) var canonicalVolumeLink : String?                        = ""
    )

    data class SaleInfo (
        @SerializedName("country"     ) var country     : String?  = "",
        @SerializedName("saleability" ) var saleability : String?  = "",
        @SerializedName("isEbook"     ) var isEbook     : Boolean? = null)

    data class AccessInfo (
        @SerializedName("country"                ) var country                : String?  = "",
        @SerializedName("viewability"            ) var viewability            : String?  = "",
        @SerializedName("embeddable"             ) var embeddable             : Boolean? = null,
        @SerializedName("publicDomain"           ) var publicDomain           : Boolean? = null,
        @SerializedName("textToSpeechPermission" ) var textToSpeechPermission : String?  = "",
        @SerializedName("epub"                   ) var epub                   : Epub?    = Epub(),
        @SerializedName("pdf"                    ) var pdf                    : Pdf?     = Pdf(),
        @SerializedName("webReaderLink"          ) var webReaderLink          : String?  = "",
        @SerializedName("accessViewStatus"       ) var accessViewStatus       : String?  = "",
        @SerializedName("quoteSharingAllowed"    ) var quoteSharingAllowed    : Boolean? = null
    )

    data class SearchInfo (@SerializedName("textSnippet" ) var textSnippet : String? = "")

    data class IndustryIdentifiers (
        @SerializedName("type"       ) var type       : String? = "",
        @SerializedName("identifier" ) var identifier : String? = ""
    )

    data class ReadingModes (
        @SerializedName("text"  ) var text  : Boolean? = null,
        @SerializedName("image" ) var image : Boolean? = null
    )

    data class PanelizationSummary (
        @SerializedName("containsEpubBubbles"  ) var containsEpubBubbles  : Boolean? = null,
        @SerializedName("containsImageBubbles" ) var containsImageBubbles : Boolean? = null
    )

    data class ImageLinks (
        @SerializedName("smallThumbnail" ) var smallThumbnail : String? = "",
        @SerializedName("thumbnail"      ) var thumbnail      : String? = ""
    )

    data class Epub (@SerializedName("isAvailable" ) var isAvailable : Boolean? = null)

    data class Pdf (@SerializedName("isAvailable" ) var isAvailable : Boolean? = null)
}
