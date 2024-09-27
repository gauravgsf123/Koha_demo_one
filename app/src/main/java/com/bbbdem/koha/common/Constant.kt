package com.bbbdem.koha.common

import android.Manifest
import android.os.Build

/**
 *Created by Gaurav Kumar on 7/1/2024
 */
object Constant {
    const val  LOCATION_SERVICE_NOTIF_ID = 1001
    const val BASE_URL ="https://demo.bestbookbuddies.com/api/v1/"
    const val GLOBAL_BASE_URL ="https://openlx.com/koha_app/"
    const val GRANT_TYPE ="client_credentials"
    const val CLIENT_ID ="a47c93f6-1a30-4edb-8ad1-9cfc6b640aa4"//"dc457ada-085d-4800-81a5-2411bed75009"
    const val CLIENT_SECRET ="a7bae359-b418-4952-8315-dfba81a2ee5b"//"53c139ae-6d38-436f-98cd-ececf05cce5a"
    const val PREFS_NAME = "koha"
    const val MAC_ADDRESS = "mac_address"
    const val REQUEST_PERMISION =101
    const val PERMISSIONS_REQUEST_CODE = 102
    const val MOBILE ="mobile"
    const val VEHICLE_NO ="vehicle_no"
    const val IS_LOGIN ="is_login"
    const val ACCESS_TOKEN ="access_token"
    const val TOKEN_TYPE ="token_type"
    const val EXPIRES_IN ="expires_in"
    const val FIRST_NAME ="first_name"
    const val LAST_NAME ="last_name"
    const val ADDRESS ="address"
    const val DOB ="dob"
    const val GENDER ="gender"
    const val USER_PASSWORD ="password"
    const val PATRON_ID ="patron"
    const val LIBRARY_ID ="libraryId"
    const val LIBRARY_NAME ="library_name"
    const val USER_DETAIL ="user_detail"
    const val OTP ="otp"
    const val EMAIL ="bbb.mobapp@gmail.com"
    const val GMAIL_PASSWORD ="jvlrakhnsqvpifnb"
    const val NEW_ARRIVALS ="new_arrival"
    const val TOP_CIRCULATING ="top_circulating"
    const val RECENTLY_BORROWED ="recently_borrowed"
    const val TOTAL_BOOK ="total_book"
    const val TOTAL_PATRON ="total_patron"
    const val CURRENT_DATE ="current_date"
    const val NOTIFICATION_COUNT ="notification_count"

    const val LIBRARY ="library"
    const val CATEGORY ="category"
    const val SEARCH_BY ="search_by"
    const val SEARCH ="search"
    const val DATE_RANGE ="date_range"
    const val IS_AVAILABLE ="is_available"
    const val FRAGMENT_TYPE ="fragment_type"
    const val START_PAGE ="start_page"
    const val TOTAL_AMOUNT ="total_amount"
    const val VERIFICATION_TYPE ="verification_type"
    const val ID ="id"
    const val WARRING_DAYS ="warring_days"

    //Global database setup
    const val USERID ="koha@bbb.com"
    const val PASSWORD ="openlx@321"
    const val ID_LIBRARY ="2"

    //mobilPay
    const val END_POINT = "getUserBillData"
    //const val KEY = "1bb842b608261053c2993ae70ebb607cdb36bbf874159e8c8e6237eff5d758d5"
    const val KEY = "1bb842b608261053c2993ae70ebb607cdb36bbf874159e8c8e6237eff5d75"
    const val MERCHANT_ID = "B10001"//"V10004"
    const val PUBLIC_KEY = "791E14FF-6243-4A73-A6E1"
    const val MERCHANT_AUTH_URL = "https://pay.mobilpay.in/index.php/getmerchantauthtoken"
    const val BILL_DETAIL = "https://mobylpe.com/index.php/getUserBillData"

    //MSG91
    const val MSG_URL = "https://control.msg91.com/api/v5/otp"
    const val AUTH_KEY = "344904AF5EqBN463202df4P1"
    const val TEMPLATE_ID = "63201e81d6fc05328e7839d2"
    const val SENDER_ID = "BESTBB"
    const val COUNTRY_CODE = "91"

    //Library Detail
    const val LIBRARY_NAME_GLOBAL = "library_name_global"
    const val LIBRARY_PHONE = "library_phone"
    const val LIBRARY_EMAIL = "library_email"
    const val LIBRARY_LINK = "library_link"
    const val LIBRARY_LATITUDE = 30.346247033173736
    const val LIBRARY_LONGITUDE = 78.00188275264247

    //app feature
    const val SMS = "sms"
    const val PAYMENT_GATEWAY = "payment_gateway"
    const val DISCHARGE = "discharge"
    const val RENEW = "renew"
    const val HOLD = "hold"

    //About Library
    const val LOGO = "logo"
    const val PRIMARY_COLOR = "primary_color"
    const val SECONDARY_COLOR = "secondary_color"
    const val OPTIONAL_COLOR = "optional_color"
    const val ABOUT_US = "about_us"
    const val LIBRARY_RULE = "library_rule"
    const val CONTACT_US = "contact_us"
    const val LATITUDE = "latitude"
    const val LONGITUDE = "longitude"

    object FragmentType{
        const val BARCODE_SCAN = 1
        const val FILTER = 2
        const val ADVANCE_SEARCH = 3
        const val SEARCH = 4
    }

    object BookListType{
        const val NEW_ARRIVAL = 1
        const val TOP_CIRCULATING = 2
        const val BORROWED_BOOK = 3
    }

    object StartScreen{
        const val LIBRARY_HOME = 1
        const val USER_SUMMARY = 2
    }

    object VerificationType{
        const val EMAIL = "Email"
        const val MOBILE = "Mobile"
    }

    object UserType{
        const val ADMIN = 1
        const val RETAILER = 2
        const val DISTRIBUTOR = 3
        const val MECHANIC = 4
        const val VEHICLE_OWNER = 5
    }

    object OTPVerificationIntentType{
        const val FORGOT_ACTIVITY = "forgot_activity"
        const val REGISTRATION_ACTIVITY = "registration_activity"
    }

    val PERMISSIONS = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
    } else {
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
    }

}