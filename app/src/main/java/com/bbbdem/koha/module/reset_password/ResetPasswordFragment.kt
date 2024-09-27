package com.bbbdem.koha.module.reset_password

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bbbdem.koha.R
import com.bbbdem.koha.app.BaseBottomSheetDialogFragment
import com.bbbdem.koha.common.Constant
import com.bbbdem.koha.databinding.FragmentResetPasswordBinding
import com.bbbdem.koha.network.Resource
import com.bbbdem.koha.network.ViewModelFactoryClass
import com.bbbdem.koha.utils.ProgressDialog

class ResetPasswordFragment : BaseBottomSheetDialogFragment() {
    private lateinit var binding:FragmentResetPasswordBinding
    private lateinit var viewModel:ResetPasswordViewModel
    private var patronId:Int = 0
    override fun initializeView() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            patronId = it.getInt(Constant.PATRON_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_reset_password,
            container,
            false
        )

        viewModel = ViewModelProvider(
            this,
            ViewModelFactoryClass(requireActivity().application)
        )[ResetPasswordViewModel::class.java]
        binding.btnUpdate.setOnClickListener {
            validate()
        }
        setObserver()
        return binding.root
    }

    override fun onClick(p0: View?) {

    }

    private fun validate(){
        binding?.run {
            if(TextUtils.isEmpty(etCurrentPassword.text.toString())){
                Toast.makeText(requireContext(),"Please enter current password", Toast.LENGTH_LONG).show()
            }else if(TextUtils.isEmpty(etNewPassword.text.toString())){
                Toast.makeText(requireContext(),"Please enter new password", Toast.LENGTH_LONG).show()
            }else if(TextUtils.isEmpty(etConfirmNewPassword.text.toString())){
                Toast.makeText(requireContext(),"Please re-enter new password ", Toast.LENGTH_LONG).show()
            }else {
                var requestModel = ResetPasswordRequest()
                requestModel.run{
                    password = etNewPassword.text.toString()
                    password_repeated = etConfirmNewPassword.text.toString()
                    old_password = etCurrentPassword.text.toString()
                }
                //viewModel.resetPassword(patronId,requestModel)
            }
        }
    }

    private fun setObserver() {
        viewModel.resetPasswordResponse.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    ProgressDialog.hideProgressBar()
                    showToast(getString(R.string.password_change_successful))
                    dismiss()
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

    companion object {
        @JvmStatic
        fun newInstance(patronId: Int) =
            ResetPasswordFragment().apply {
                arguments = Bundle().apply {
                    putInt(Constant.PATRON_ID, patronId)
                }
            }
    }
}