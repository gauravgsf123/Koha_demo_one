package com.bbbdem.koha.module.my_account.change_password

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bbbdem.koha.R
import com.bbbdem.koha.app.BaseFragment
import com.bbbdem.koha.common.Constant
import com.bbbdem.koha.databinding.FragmentChangePasswordBinding
import com.bbbdem.koha.module.dashboard.DashboardActivity
import com.bbbdem.koha.network.Resource
import com.bbbdem.koha.network.ViewModelFactoryClass
import com.bbbdem.koha.utils.ProgressDialog

class ChangePasswordFragment : BaseFragment() {
    private lateinit var binding:FragmentChangePasswordBinding
    private lateinit var viewModel: ChangePasswordViewModel
    private var isCurrentPasswordVisible = false
    private var isNewPasswordVisible = false
    private var isConfirmPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_change_password,
            container,
            false
        )

        viewModel = ViewModelProvider(
            this,
            ViewModelFactoryClass(requireActivity().application)
        )[ChangePasswordViewModel::class.java]
        binding.btnUpdate.setOnClickListener {
            validate()
        }
        binding.ivShowHideCurrentPassword.setOnClickListener(this)
        binding.ivShowHideNewPassword.setOnClickListener(this)
        binding.ivShowHideConfirmNewPassword.setOnClickListener(this)
        setObserver()
        (activity as DashboardActivity?)?.run {
            showToolbar(true)
            setAppTitle(getString(R.string.change_password))
        }
        return binding.root
    }
    private fun validate(){
        Log.d("validate","${binding.etCurrentPassword.text} ${sharedPreference.getValueString(Constant.USER_PASSWORD)}")
        binding?.run {
            if(TextUtils.isEmpty(etCurrentPassword.text.toString())){
                Toast.makeText(requireContext(),"Please enter current password", Toast.LENGTH_LONG).show()
            }else if(TextUtils.isEmpty(etNewPassword.text.toString())){
                Toast.makeText(requireContext(),"Please enter new password", Toast.LENGTH_LONG).show()
            }else if(TextUtils.isEmpty(etConfirmNewPassword.text.toString())){
                Toast.makeText(requireContext(),"Please re-enter new password ", Toast.LENGTH_LONG).show()
            }else if(etCurrentPassword.text.toString() != sharedPreference.getValueString(Constant.USER_PASSWORD)){
                Toast.makeText(requireContext(),"Current password is wrong", Toast.LENGTH_LONG).show()
            }else {
                var requestModel = ChangePasswordRequest()
                requestModel.run{
                    password = etNewPassword.text.toString()
                    password_2 = etConfirmNewPassword.text.toString()
                }
                viewModel.resetPassword(sharedPreference.getValueInt(Constant.PATRON_ID),requestModel)
            }
        }
    }

    override fun onClick(p0: View?) {
        binding.run {
            when(p0?.id){
                ivShowHideCurrentPassword.id->{
                    showHidePassword(isCurrentPasswordVisible,etCurrentPassword,ivShowHideCurrentPassword)
                    isCurrentPasswordVisible=!isCurrentPasswordVisible
                }
                ivShowHideNewPassword.id->{
                    showHidePassword(isNewPasswordVisible,etNewPassword,ivShowHideNewPassword)
                    isNewPasswordVisible=!isNewPasswordVisible
                }
                ivShowHideConfirmNewPassword.id->{
                    showHidePassword(isConfirmPasswordVisible,etConfirmNewPassword,ivShowHideConfirmNewPassword)
                    isConfirmPasswordVisible=!isConfirmPasswordVisible
                }

            }
        }
        super.onClick(p0)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun showHidePassword(isPasswordVisible:Boolean,etPassword:EditText,ivShowHidePassword:ImageView){
        if (!isPasswordVisible) {
            etPassword?.transformationMethod = HideReturnsTransformationMethod.getInstance()
            ivShowHidePassword?.setImageDrawable(resources.getDrawable(R.drawable.ic_password_show))
        } else {
            etPassword?.transformationMethod = PasswordTransformationMethod.getInstance()
            ivShowHidePassword?.setImageDrawable(resources.getDrawable(R.drawable.ic_password_hide))
        }
    }

    private fun setObserver() {
        viewModel.resetPasswordResponse.observe(requireActivity()) { response ->
            when (response) {
                is Resource.Success -> {
                    ProgressDialog.hideProgressBar()
                    sharedPreference.save(Constant.USER_PASSWORD, binding?.etNewPassword?.text.toString())
                    showToast(getString(R.string.password_change_successful))
                }
                is Resource.Loading -> {
                    ProgressDialog.showProgressBar(requireContext())
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
}