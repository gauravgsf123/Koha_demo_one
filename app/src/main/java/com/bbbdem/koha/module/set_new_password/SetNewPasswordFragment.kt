package com.bbbdem.koha.module.set_new_password

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
import com.bbbdem.koha.databinding.FragmentSetNewPasswordBinding
import com.bbbdem.koha.network.Resource
import com.bbbdem.koha.network.ViewModelFactoryClass
import com.bbbdem.koha.utils.ProgressDialog


class SetNewPasswordFragment : BaseBottomSheetDialogFragment() {
    private lateinit var binding:FragmentSetNewPasswordBinding
    private lateinit var viewModel: SetNewPasswordViewModel
    private var patronId:Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            patronId = it.getInt(Constant.PATRON_ID)
        }
    }

    override fun initializeView() {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_set_new_password,
            container,
            false
        )

        viewModel = ViewModelProvider(
            this,
            ViewModelFactoryClass(requireActivity().application)
        )[SetNewPasswordViewModel::class.java]
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
            if(TextUtils.isEmpty(etNewPassword.text.toString())){
                Toast.makeText(requireContext(),"Please enter new password", Toast.LENGTH_LONG).show()
            }else if(TextUtils.isEmpty(etConfirmNewPassword.text.toString())){
                Toast.makeText(requireContext(),"Please re-enter new password ", Toast.LENGTH_LONG).show()
            }else {
                var requestModel = SetNewPasswordRequest()
                requestModel.run{
                    password = etNewPassword.text.toString()
                    password_2 = etConfirmNewPassword.text.toString()
                }
                viewModel.setNewPassword(patronId,requestModel)
            }
        }
    }

    private fun setObserver() {
        viewModel.setNewPasswordResponse.observe(this) { response ->
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

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(patronId:Int) =
            SetNewPasswordFragment().apply {
                arguments = Bundle().apply {
                    putInt(Constant.PATRON_ID, patronId)
                }
            }
    }
}