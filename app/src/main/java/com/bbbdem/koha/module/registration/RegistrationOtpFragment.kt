package com.bbbdem.koha.module.registration

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bbbdem.koha.R
import com.bbbdem.koha.app.BaseBottomSheetDialogFragment
import com.bbbdem.koha.common.Constant
import com.bbbdem.koha.databinding.FragmentRegistrationOtpBinding
import com.bbbdem.koha.module.otp.OTPViewModel
import com.bbbdem.koha.module.registration.model.RegisterUserRequestModel
import com.bbbdem.koha.network.Resource
import com.bbbdem.koha.network.ViewModelFactoryClass
import com.bbbdem.koha.utils.Mailer
import com.bbbdem.koha.utils.ProgressDialog
import com.bbbdem.koha.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class RegistrationOtpFragment(var listener: OnActionCompleteListener) : BaseBottomSheetDialogFragment() {
    private lateinit var binding:FragmentRegistrationOtpBinding
    private lateinit var viewModel: OTPViewModel
    private var otp = "1234"
    private lateinit var registerUserRequestModel: RegisterUserRequestModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            registerUserRequestModel = it.getParcelable("requestModel")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_registration_otp,
            container,
            false
        )
        viewModel = ViewModelProvider(
            this,
            ViewModelFactoryClass(requireActivity().application)
        )[OTPViewModel::class.java]
        binding?.run {
            btnContinue.setOnClickListener(this@RegistrationOtpFragment)
            btnResend.setOnClickListener(this@RegistrationOtpFragment)
        }
        sendOTPonMail()
        setObserver()
        return binding.root
    }

    private fun setObserver() {
        viewModel.otpResponse.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    runTimerTask()
                    ProgressDialog.hideProgressBar()
                    showToast(response.message!!)
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

    @SuppressLint("CheckResult")
    private fun sendOTPonMail(){
        ProgressDialog.showProgressBar(requireContext())
        val otp = Utils.getOTP()
        registerUserRequestModel.email?.let {
            Mailer.sendMail(
                it,
                "Forgot Password KOHA",
                "Your OTP is $otp"
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        sendOTPonMobileNumber()
                    }, {
                        showToast("$it")
                    }
                )
        }
    }

    private fun sendOTPonMobileNumber(){
        otp = Utils.getOTP()
        var body = mapOf<String, String>(
            "template_id" to Constant.TEMPLATE_ID,
            "mobile" to registerUserRequestModel.mobile!!,
            "authkey" to Constant.AUTH_KEY,
            "otp" to otp,
            "country" to Constant.COUNTRY_CODE,
            "sender" to Constant.SENDER_ID,
        )
        viewModel.sendOTP(Constant.MSG_URL,body)
    }


    private fun runTimerTask() {
        binding.btnResend.isEnabled = false
        object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                activity?.runOnUiThread(Runnable { binding.btnResend.text = "Resend in " + millisUntilFinished / 1000 })
            }
            override fun onFinish() {
                binding.btnResend.text = "Resend"
                binding.btnResend.isEnabled = true
            }
        }.start()
    }

    override fun onClick(p0: View?) {
        binding.run {
            when(p0?.id){
                btnContinue.id->{
                    if(otp==binding.pinview.value){
                        dismiss()
                        listener.onActionComplete(registerUserRequestModel)
                    }else showToast("Wrong OTP entered")
                }
                btnResend.id->{
                    runTimerTask()
                    sendOTPonMail()
                }
            }
        }
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(registerUserRequestModel: RegisterUserRequestModel, listener:OnActionCompleteListener) =
            RegistrationOtpFragment(listener).apply {
                arguments = Bundle().apply {
                    putParcelable("requestModel",registerUserRequestModel)
                }
            }
    }

    interface OnActionCompleteListener {
        fun onActionComplete(registerUserRequestModel: RegisterUserRequestModel)
    }
}