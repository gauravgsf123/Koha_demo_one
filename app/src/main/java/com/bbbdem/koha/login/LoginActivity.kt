package com.bbbdem.koha.login

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bbbdem.koha.R
import com.bbbdem.koha.app.MVVMBindingActivity
import com.bbbdem.koha.common.Constant
import com.bbbdem.koha.databinding.ActivityLoginBinding
import com.bbbdem.koha.login.model.ValidateUserRequestModel
import com.bbbdem.koha.module.dashboard.DashboardActivity
import com.bbbdem.koha.module.forgot.ForgotFragment
import com.bbbdem.koha.module.registration.RegistrationPersonalActivity
import com.bbbdem.koha.network.Resource
import com.bbbdem.koha.network.ViewModelFactoryClass
import com.bbbdem.koha.utils.ProgressDialog
import com.google.gson.Gson


class LoginActivity : MVVMBindingActivity<ActivityLoginBinding>() {
    private lateinit var viewModel:LoginViewModel
    private var isPasswordVisible = false
    private val permissionList = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    override fun initializeView() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactoryClass(application)
        )[LoginViewModel::class.java]
        binding?.run {
            tvForgotPassword.setOnClickListener(this@LoginActivity)
            tvRegister.setOnClickListener(this@LoginActivity)
            tvContinueAsGuest.setOnClickListener(this@LoginActivity)
            btnLogin.setOnClickListener(this@LoginActivity)
            ivShowHidePassword.setOnClickListener(this@LoginActivity)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkNotificationPermission()
        }
        setObserver()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun checkNotificationPermission() {
        val permission = Manifest.permission.POST_NOTIFICATIONS
        when {
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED -> {
                // make your action here
            }
            shouldShowRequestPermissionRationale(permission) -> {
                //showPermissionRationaleDialog() // permission denied permanently
            }
            else -> {
                requestNotificationPermission.launch(permission)
            }
        }
    }

    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted->
            if (isGranted) // make your action here
            else showToast("Permission denied")
        }

    private fun validate() {
        binding?.run {
             if(TextUtils.isEmpty(etUsername.text.toString())){
                Toast.makeText(this@LoginActivity,getString(R.string.please_enter_username),Toast.LENGTH_LONG).show()
            }else if(TextUtils.isEmpty(etPassword.text.toString())){
                Toast.makeText(this@LoginActivity,getString(R.string.please_enter_password),Toast.LENGTH_LONG).show()
            }else {
                 var validateUser = ValidateUserRequestModel(etUsername.text.toString(),etPassword.text.toString())
                 viewModel.validateUser(validateUser)
            }
        }
    }

    private fun setObserver() {
        viewModel.validateUserResponseModel.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    ProgressDialog.hideProgressBar()
                    getUserDetail(response.data?.patronId)
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

        viewModel.userDetailResponseModel.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    ProgressDialog.hideProgressBar()
                    showToast(getString(R.string.login_successful))
                    val data = response.data!!
                    if(binding?.rbRememberMe?.isChecked == true) {
                        sharedPreference.save(Constant.IS_LOGIN, true)
                    }
                    sharedPreference.save(Constant.USER_DETAIL,Gson().toJson(data))
                    sharedPreference.save(Constant.PATRON_ID, data.patronId!!)
                    sharedPreference.save(Constant.LIBRARY_ID, data.libraryId!!)
                    sharedPreference.save(Constant.USER_PASSWORD, binding?.etPassword?.text.toString())
                    viewModel.getLibraryDetail(data.libraryId)
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
                    ProgressDialog.hideProgressBar()
                    val data = response.data!!
                    data.name?.let { sharedPreference.save(Constant.LIBRARY_NAME, it) }

                    startNewActivity(DashboardActivity())
                    finish()
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

    private fun getUserDetail(patronId: Int?){
        viewModel.getUserDetail(patronId)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun showHidePassword(){
        if (!isPasswordVisible) {
            binding?.etPassword?.transformationMethod = HideReturnsTransformationMethod.getInstance()
            binding?.ivShowHidePassword?.setImageDrawable(resources.getDrawable(R.drawable.ic_password_show))
            isPasswordVisible = true
        } else {
            binding?.etPassword?.transformationMethod = PasswordTransformationMethod.getInstance()
            binding?.ivShowHidePassword?.setImageDrawable(resources.getDrawable(R.drawable.ic_password_hide))
            isPasswordVisible = false
        }
    }


    override fun provideViewResource(): Int {
       return R.layout.activity_login
    }

    override fun onClick(view: View?) {
        binding?.run {
            when(view?.id){
                btnLogin.id->validate()
                tvRegister.id->startNewActivity(RegistrationPersonalActivity())
                tvContinueAsGuest.id->{startNewActivity(DashboardActivity());finish()}
                ivShowHidePassword.id->showHidePassword()
                tvForgotPassword.id->ForgotFragment().show(supportFragmentManager, "ForgotFragment")
                else -> {}
            }

        }
    }

}