package com.bbbdem.koha.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.bbbdem.koha.module.dashboard.model.ItemListOfBookResponseModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Random
import java.util.concurrent.TimeUnit


@SuppressLint("SimpleDateFormat")
object Utils {

    fun hasInternetConnection(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }

        return false
    }

    open fun getDateTime(format:String): String {
        val sdf = SimpleDateFormat(format)
        return sdf.format(Date());
    }

    fun checkDate(dateStr:String):Boolean{
        var date = dateStr.substring(0,18)
        var enteredDate: Date? = null
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            enteredDate = sdf.parse(date)
        } catch (ex: Exception) {
            // enteredDate will be null if date="287686";
        }
        val currentDate = Date()
        return enteredDate?.after(currentDate) == true
    }

    fun changeDateFormat(dateStr:String):String{
        var oldString = dateStr.substring(0,18)
        var date:Date?=null
        try {
            date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(oldString)
        } catch (ex: Exception) {
            // enteredDate will be null if date="287686";
        }
        return SimpleDateFormat("yyyy-MM-dd").format(date)
    }

    fun getNameInitials(name: String): String {
        if (name.isNotEmpty()) {
            val names = name.split(" ")
            val stringBuilder = StringBuilder()
            names.forEach {
                if (it.isNotEmpty())
                    stringBuilder.append(it[0].toUpperCase())
            }

            return stringBuilder.toString()
        }
        return ""
    }

    fun getNumber(string: String): String {
        return if(string.length>13){
            val isbnNumber = string.replace(Regex("[^0-9]"), "")
            if(isbnNumber.length>10){
                isbnNumber.substring(0,13)
            }else isbnNumber

        }else string
    }

    fun daysCount(dateStr : String):Long{
        val oldString = dateStr.substring(0,18)
        var date:Date?=null
        date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(oldString)!!
        val millionSeconds = date.time - Calendar.getInstance().timeInMillis
        val days = TimeUnit.MILLISECONDS.toDays(millionSeconds).toString() + "days"
        return TimeUnit.MILLISECONDS.toDays(millionSeconds)//(day[Calendar.DAY_OF_MONTH] - (calCurr[Calendar.DAY_OF_MONTH]))
    }

    fun isSubscription(dateStr : String):Long{
        var date:Date?=null
        date = SimpleDateFormat("yyyy-MM-dd").parse(dateStr)!!
        val millionSeconds = date.time - Calendar.getInstance().timeInMillis
        val days = TimeUnit.MILLISECONDS.toDays(millionSeconds).toString() + "days"
        Log.d("days",days)
        return TimeUnit.MILLISECONDS.toDays(millionSeconds)//(day[Calendar.DAY_OF_MONTH] - (calCurr[Calendar.DAY_OF_MONTH]))
    }

    fun getInvoiceNumber():String{
        var sdf = SimpleDateFormat("yyyyMMddHHmmss")
        val millionSeconds = sdf.format(Date())//- Calendar.getInstance().timeInMillis
        //val days = TimeUnit.MILLISECONDS.toDays(millionSeconds).toString() + "days"
        return "koha_$millionSeconds"

    }

    fun getAvailableCount(data: List<ItemListOfBookResponseModel>):Int{
        var countMinus = 0
        data.forEachIndexed { index, item ->
            if(item.itemId==item.checkoutResponseModel?.itemId){
                countMinus++
            }
            /*if(item.itemDetailResponseModel?.lostStatus!! >1){
                countMinus++
            }
            if(item.itemDetailResponseModel?.damagedStatus!! >1){
                countMinus++
            }
            if(item.itemDetailResponseModel?.withdrawn!! >1){
                countMinus++
            }
            if(item.itemDetailResponseModel?.notForLoanStatus!! >1){
                countMinus++
            }*/
        }
        return (data.size-countMinus)
    }

    fun getOTP():String{
        val r = Random()
        return String.format("%04d", Integer.valueOf(r.nextInt(1001)))
    }

    fun getIssuedCount(data: List<ItemListOfBookResponseModel>):Int{
        var issuedCount = 0
        data.forEachIndexed { index, item ->
            if(item.itemId==item.checkoutResponseModel?.itemId){
                issuedCount++
            }

        }
        return issuedCount
    }
}