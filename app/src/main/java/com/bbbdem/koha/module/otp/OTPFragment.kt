package com.bbbdem.koha.module.otp

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bbbdem.koha.R
import com.bbbdem.koha.app.BaseBottomSheetDialogFragment
import com.bbbdem.koha.common.Constant
import com.bbbdem.koha.databinding.FragmentOtpBinding
import com.bbbdem.koha.module.set_new_password.SetNewPasswordFragment
import com.bbbdem.koha.network.Resource
import com.bbbdem.koha.network.ViewModelFactoryClass
import com.bbbdem.koha.utils.Mailer
import com.bbbdem.koha.utils.ProgressDialog
import com.bbbdem.koha.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class OTPFragment : BaseBottomSheetDialogFragment()/* VerificationListener*/ {
    private lateinit var binding:FragmentOtpBinding
    private lateinit var viewModel:OTPViewModel
    private var otp = "1234"
    private var type = Constant.VerificationType.EMAIL
    private var id = ""
    private var patronId:Int = 0
    private var OTP_LNGTH = 4
    override fun initializeView() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            patronId = it.getInt(Constant.PATRON_ID)
            type = it.getString(Constant.VERIFICATION_TYPE).toString()
            id = it.getString(Constant.ID).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_otp,
            container,
            false
        )
        binding?.run {
            btnContinue.setOnClickListener(this@OTPFragment)
            btnResend.setOnClickListener(this@OTPFragment)
        }
        viewModel = ViewModelProvider(
            this,
            ViewModelFactoryClass(requireActivity().application)
        )[OTPViewModel::class.java]
        runTimerTask()
        if(type == Constant.VerificationType.EMAIL){
            sendOTPonMail(id)
        }else if(type == Constant.VerificationType.MOBILE){
            sendOTPonMobile(id)
        }
        //SendOTP.getInstance().getTrigger().initiate()
        setObserver()
        return binding.root
    }

    private fun setObserver() {
        viewModel.otpResponse.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
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

    override fun onClick(p0: View?) {
        binding.run {
            when(p0?.id){
                btnContinue.id->{
                    if(otp==binding.pinview.value){
                        dismiss()
                        SetNewPasswordFragment.newInstance(patronId).show(requireFragmentManager(),"SetNewPasswordFragment")
                    }else showToast("Wrong OTP entered")
                }
                btnResend.id->{
                    runTimerTask()
                    if(type == Constant.VerificationType.EMAIL){
                        sendOTPonMail(id)
                    }else if(type == Constant.VerificationType.MOBILE){
                        sendOTPonMobile(id)
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(patronId: Int?,type:String,id:String) =
            OTPFragment().apply {
                arguments = Bundle().apply {
                    if (patronId != null) {
                        putInt(Constant.PATRON_ID, patronId)
                        putString(Constant.VERIFICATION_TYPE, type)
                        putString(Constant.ID, id)
                    }
                }
            }
    }

    @SuppressLint("CheckResult")
    private fun sendOTPonMail(email:String) {
        ProgressDialog.showProgressBar(requireContext())
        otp = Utils.getOTP()
        Mailer.sendMail(
            email,
            "Forgot Password KOHA",
            "Your OTP is $otp"
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Log.d("OTP",otp)
                    ProgressDialog.hideProgressBar()
                    Toast.makeText(requireContext(), "OTP has been sent", Toast.LENGTH_SHORT).show()
                    dismiss()
                    runTimerTask()
                }, {
                    Toast.makeText(requireContext(), "$it", Toast.LENGTH_SHORT).show()
                }
            )
    }

    private fun sendOTPonMobile(mobile:String) {
        otp = Utils.getOTP()
        var body = mapOf<String, String>(
            "template_id" to Constant.TEMPLATE_ID,
            "mobile" to mobile,
            "authkey" to Constant.AUTH_KEY,
            "otp" to otp,
            "country" to Constant.COUNTRY_CODE,
            "sender" to Constant.SENDER_ID,
        )
        viewModel.sendOTP(Constant.MSG_URL,body)

       /* otp = Utils.getOTP()
        SendOTPConfigBuilder()
            .setCountryCode(91)
            .setMobileNumber("7011351424")
            .setVerifyWithoutOtp(true) //direct verification while connect with mobile network
            .setSenderId("BESTBB")
            .setMessage("123456 is Your verification digits.")
            .setOtpLength(6)
            .setOtp(otp.toInt())
            .setUnicodeEnable(true)
            .setVerificationCallBack(this).build()

        SendOTP.getInstance().getTrigger().initiate();*/

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

    /*override fun onSendOtpResponse(responseCode: SendOTPResponseCode?, message: String?) {
        activity?.runOnUiThread(Runnable {
            if (responseCode === SendOTPResponseCode.DIRECT_VERIFICATION_SUCCESSFUL_FOR_NUMBER || responseCode === SendOTPResponseCode.OTP_VERIFIED) {
                //otp verified OR direct verified by send otp 2.O
                Log.d("responseCode","01 $responseCode")
            } else if (responseCode === SendOTPResponseCode.READ_OTP_SUCCESS) {
                //Auto read otp from sms successfully
                // you can get otp form message filled
                SendOTP.getInstance().getTrigger().verify(otp);
                Log.d("responseCode","02 $responseCode")
            } else if (responseCode === SendOTPResponseCode.SMS_SUCCESSFUL_SEND_TO_NUMBER || responseCode === SendOTPResponseCode.DIRECT_VERIFICATION_FAILED_SMS_SUCCESSFUL_SEND_TO_NUMBER) {
                // Otp send to number successfully
                Log.d("responseCode","03 $responseCode")
            } else {
                //exception found
                Log.d("responseCode","04 $responseCode")
            }
        })
    }*/
     override fun onDestroy() {
        super.onDestroy()
         //SendOTP.getInstance().getTrigger().stop()
    }
}