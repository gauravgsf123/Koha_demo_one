package com.bbbdem.koha.dashboard

data class TripSheetResponse(var IsSuccess : Boolean? = null,
                             var Message   : String?  = null,
                             var TripsheetDocketCartonList : ArrayList<TripSheetDocketDetail> = arrayListOf()){

    data class TripSheetDocketDetail (
         var CNoteNo     : String? = null,
         var BarCodeNo   : String? = null,
         var TotalBox    : Int?    = null,
         var Shortcode   : String? = null,
         var Destination : String? = null
    )

    data class DocketCartonList (
        var CartonNo : String? = null
    )
}
