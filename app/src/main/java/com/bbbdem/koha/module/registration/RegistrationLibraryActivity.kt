package com.bbbdem.koha.module.registration

import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bbbdem.koha.R
import com.bbbdem.koha.app.MVVMBindingActivity
import com.bbbdem.koha.common.Constant
import com.bbbdem.koha.databinding.ActivityRegistrationLibraryBinding
import com.bbbdem.koha.login.LoginActivity
import com.bbbdem.koha.module.registration.model.AllCategoryResponseModel
import com.bbbdem.koha.module.registration.model.AllLibraryResponseModel
import com.bbbdem.koha.module.registration.model.RegisterUserRequestModel
import com.bbbdem.koha.network.Resource
import com.bbbdem.koha.network.ViewModelFactoryClass
import com.bbbdem.koha.utils.ProgressDialog
import java.util.*
import kotlin.collections.ArrayList

class RegistrationLibraryActivity : MVVMBindingActivity<ActivityRegistrationLibraryBinding>(){
    private lateinit var viewModel:RegistrationViewModel
    private var libraryList = ArrayList<String>()
    private var categoryList = ArrayList<String>()
    private var library = ""
    private var category = ""
    override fun initializeView() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactoryClass(application)
        )[RegistrationViewModel::class.java]
        viewModel.getLibraries()
        binding?.btnRegister?.setOnClickListener(this)
        setObserver()
    }

    private fun setObserver() {
        viewModel.allLibraryResponseModel.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    ProgressDialog.hideProgressBar()
                    viewModel.getCategory()
                    setupSpinnerLibrary(response.data)
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

        viewModel.allCategoryResponseModel.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    ProgressDialog.hideProgressBar()
                    setupSpinnerCategory(response.data)
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
                    if(response?.data?.isNotEmpty() == true) {
                        showToast("Email id is already register")
                    }else register()
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

        viewModel.registerUserRequestModel.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    ProgressDialog.hideProgressBar()
                    showToast(getString(R.string.registration_successful))
                    startNewActivity(LoginActivity())
                    finishAffinity()
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

    private fun register(){
        binding?.run {
            var requestModel = RegisterUserRequestModel()
            requestModel.run {
                firstname = intent.getStringExtra(Constant.FIRST_NAME)
                surname = intent.getStringExtra(Constant.LAST_NAME)
                dateOfBirth = intent.getStringExtra(Constant.DOB)
                address = intent.getStringExtra(Constant.ADDRESS)
                gender = intent.getStringExtra(Constant.GENDER)
                cardnumber = etCardNumber.text.toString()
                email = etEmail.text.toString()
                mobile = etMobile.text.toString()
                libraryId = library
                categoryId = category
            }
            ProgressDialog.hideProgressBar()
            RegistrationOtpFragment.newInstance(requestModel,
                object : RegistrationOtpFragment.OnActionCompleteListener {
                    override fun onActionComplete(registerUserRequestModel: RegisterUserRequestModel) {
                        viewModel.registerUser(requestModel)
                    }

                }).show(supportFragmentManager, "OTPFragment")
        }
    }

    private fun setupSpinnerCategory(list: List<AllCategoryResponseModel>?) {
        categoryList.clear()
        categoryList.add(getString(R.string.select_category))
        list?.forEach {
            it.description?.let { it1 -> categoryList.add(it1) }
        }
        var arrayAdapter = ArrayAdapter(this@RegistrationLibraryActivity,android.R.layout.simple_dropdown_item_1line,categoryList)
        binding?.spinnerCategory?.adapter = arrayAdapter
        binding?.spinnerCategory?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SuspiciousIndentation")
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if(position>0)
                category = list?.get(position-1)?.value ?: ""
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    private fun setupSpinnerLibrary(list: List<AllLibraryResponseModel>?) {
        libraryList.clear()
        libraryList.add(getString(R.string.select_library))
        list?.forEach {
            it.name?.let { it1 -> libraryList.add(it1) }
        }
        var arrayAdapter = ArrayAdapter(this@RegistrationLibraryActivity,android.R.layout.simple_dropdown_item_1line,libraryList)
        binding?.spinnerLibrary?.adapter = arrayAdapter
        binding?.spinnerLibrary?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SuspiciousIndentation")
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if(position>0)
                library = list?.get(position-1)?.libraryId ?: ""
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    @SuppressLint("CheckResult")
    private fun validate(){
        binding?.run {
            if(TextUtils.isEmpty(etCardNumber.text.toString())){
                Toast.makeText(this@RegistrationLibraryActivity,"Please enter card number", Toast.LENGTH_LONG).show()
            }else if(TextUtils.isEmpty(etEmail.text.toString())){
                Toast.makeText(this@RegistrationLibraryActivity,"Please enter email id", Toast.LENGTH_LONG).show()
            }else if(TextUtils.isEmpty(etMobile.text.toString())){
                Toast.makeText(this@RegistrationLibraryActivity,"Please enter mobile number", Toast.LENGTH_LONG).show()
            }else {
                var query = "{\"email\":{\"-like\":\"${etEmail.text.trim()}\"}}"
                viewModel.getUserDetailByEmail(query)
            }
        }
    }

    private fun getOTP():String{
        val r = Random()
        return String.format("%04d", Integer.valueOf(r.nextInt(1001)))
    }

    override fun provideViewResource(): Int {
        return R.layout.activity_registration_library
    }

    override fun onClick(view: View?) {
        when(view?.id){
            binding?.btnRegister?.id->{
                hideKeyboard(view!!)
                validate()
            }
        }
    }
}