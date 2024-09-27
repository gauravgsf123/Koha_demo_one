package com.bbbdem.koha.module.splash_screen

import android.app.ActivityManager
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bbbdem.koha.R
import com.bbbdem.koha.app.MVVMBindingActivity
import com.bbbdem.koha.common.Constant
import com.bbbdem.koha.databinding.ActivitySplashBinding
import com.bbbdem.koha.login.LoginActivity
import com.bbbdem.koha.module.dashboard.DashboardActivity
import com.bbbdem.koha.module.splash_screen.model.LibraryRequestModel
import com.bbbdem.koha.module.splash_screen.model.RequestModel
import com.bbbdem.koha.network.Resource
import com.bbbdem.koha.network.ViewModelFactoryClass
import com.bbbdem.koha.service.TokenRefreshService
import com.bbbdem.koha.utils.ProgressDialog
import com.bbbdem.koha.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class SplashActivity : MVVMBindingActivity<ActivitySplashBinding>() {
    private lateinit var viewModel: SplashViewModel
    var c: Date? = null
    var df: SimpleDateFormat? = null
    override fun initializeView() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactoryClass(application)
        )[SplashViewModel::class.java]
        setObserver()
        checkCurrentDate()
    }

    override fun provideViewResource(): Int {
        return R.layout.activity_splash
    }

    private fun checkCurrentDate(){
        df = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        c = Calendar.getInstance().time
        val currentDate = df!!.format(c)
        Log.d("current_date",currentDate+sharedPreference.getValueString(Constant.CURRENT_DATE))
        if(currentDate!=sharedPreference.getValueString(Constant.CURRENT_DATE)){
            var request = LibraryRequestModel(Constant.ID_LIBRARY)
            viewModel.getLibraryDetail(request)
        }else viewModel.getToken()
        sharedPreference.save(Constant.CURRENT_DATE,currentDate)

    }

    override fun onClick(p0: View?) {
    }

    private fun setObserver() {
        viewModel.tokenResponse.observe(this, Observer {
            it?.let { it1 ->
                sharedPreference.save(Constant.ACCESS_TOKEN, it1.accessToken!!)
                sharedPreference.save(Constant.TOKEN_TYPE, it1.tokenType!!)
                sharedPreference.save(Constant.EXPIRES_IN, it1.expiresIn!!)
            }
            CoroutineScope(Dispatchers.Main).launch {
                delay(100)
                if (sharedPreference.getValueBoolean(Constant.IS_LOGIN, false)) {
                    startActivity(Intent(this@SplashActivity, DashboardActivity::class.java))
                    finish()
                } else {
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                    finish()
                }
            }
        })

        viewModel.libraryResponseModel.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    ProgressDialog.hideProgressBar()
                    var endDate = response?.data?.endDate
                    var isSubscription = endDate?.let { Utils.isSubscription(it) }
                    if (isSubscription != null && isSubscription<1) {
                        showConfirmation()
                    }else{
                        response.data?.let {it1->
                            sharedPreference.save(Constant.LIBRARY_NAME_GLOBAL, it1.libraryName!!)
                            sharedPreference.save(Constant.LIBRARY_EMAIL, it1.libraryEmail!!)
                            sharedPreference.save(Constant.LIBRARY_PHONE, it1.libraryPhone!!)
                            sharedPreference.save(Constant.LIBRARY_LINK, it1.opacUrl!!)
                        }
                        var request = RequestModel(Constant.ID_LIBRARY)
                        viewModel.getLibraryFeature(request)
                    }

                }
                is Resource.Loading -> {
                    ProgressDialog.showProgressBar(this)
                }
                is Resource.Error -> {
                    ProgressDialog.hideProgressBar()
                    response.message?.let { showToast(it) }
                }
                else -> {
                    ProgressDialog.hideProgressBar()
                    response.message?.let { showToast(it) }
                }
            }
        }
        viewModel.libraryFeatureResponseModel.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {it1->
                        sharedPreference.save(Constant.DISCHARGE, it1.discharge?.toInt()!!)
                        sharedPreference.save(Constant.HOLD, it1.hold?.toInt()!!)
                        sharedPreference.save(Constant.PAYMENT_GATEWAY, it1.paymentGateway?.toInt()!!)
                        sharedPreference.save(Constant.SMS, it1.sms?.toInt()!!)
                        sharedPreference.save(Constant.RENEW, it1.renew?.toInt()!!)
                    }

                    var request = RequestModel(Constant.ID_LIBRARY)
                    viewModel.getAboutLibraryDetail(request)
                }
                is Resource.Loading -> {
                    ProgressDialog.showProgressBar(this)
                }
                is Resource.Error -> {
                    ProgressDialog.hideProgressBar()
                    response.message?.let { showToast(it) }
                }
                else -> {
                    ProgressDialog.hideProgressBar()
                    response.message?.let { showToast(it) }
                }
            }
        }

        viewModel.libraryDetailResponseModel.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    viewModel.getToken()
                    response.data?.let {it1->
                        sharedPreference.save(Constant.LOGO, it1.logoUrl!!)
                        sharedPreference.save(Constant.PRIMARY_COLOR, it1.priColor!!)
                        sharedPreference.save(Constant.SECONDARY_COLOR, it1.darkColor!!)
                        sharedPreference.save(Constant.OPTIONAL_COLOR, it1.optColor!!)
                        sharedPreference.save(Constant.ABOUT_US, it1.aboutUs!!)
                        sharedPreference.save(Constant.LIBRARY_RULE, it1.libraryRules!!)
                        sharedPreference.save(Constant.CONTACT_US, it1.contactUs!!)
                        sharedPreference.save(Constant.LATITUDE, it1.latitude!!)
                        sharedPreference.save(Constant.LONGITUDE, it1.longitude!!)
                    }

                }
                is Resource.Loading -> {
                    ProgressDialog.showProgressBar(this)
                }
                is Resource.Error -> {
                    ProgressDialog.hideProgressBar()
                    response.message?.let { showToast(it) }
                }
                else -> {
                    ProgressDialog.hideProgressBar()
                    response.message?.let { showToast(it) }
                }
            }
        }
    }


    private fun showConfirmation() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Your Koha APP subscription has expired. Please contact the library for more detail. Thank you.")
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, id ->
                finishAffinity()
            }
        val alert = builder.create()
        alert.show()
    }

    override fun onStop() {
        super.onStop()
        stopService()
    }

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({
            //if(!LocationService().isInstanceCreated()){
            if(!isMyServiceRunning(TokenRefreshService::class.java)){
                stopService()
                startService(Intent(this,TokenRefreshService::class.java))
            }
        }, 2000)
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager: ActivityManager =
            getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    private fun stopService(){
        stopService(Intent(this,TokenRefreshService::class.java))
    }
}