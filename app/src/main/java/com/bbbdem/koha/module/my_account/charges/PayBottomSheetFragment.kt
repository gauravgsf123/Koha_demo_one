package com.bbbdem.koha.module.my_account.charges

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bbbdem.koha.R
import com.bbbdem.koha.app.BaseBottomSheetDialogFragment
import com.bbbdem.koha.common.Constant
import com.bbbdem.koha.databinding.FragmentPayBottomSheetBinding
import com.bbbdem.koha.login.model.UserDetailResponseModel
import com.bbbdem.koha.module.dashboard.DashboardActivity
import com.bbbdem.koha.module.my_account.charges.model.MerchantauthtokenRequest
import com.bbbdem.koha.module.my_account.charges.model.MerchantauthtokenResponse
import com.bbbdem.koha.module.my_account.charges.model.UserBillDataRequest
import com.bbbdem.koha.network.Resource
import com.bbbdem.koha.network.ViewModelFactoryClass
import com.bbbdem.koha.module.payment.MobilPayFragment
import com.bbbdem.koha.utils.ProgressDialog
import com.bbbdem.koha.utils.Utils
import com.google.gson.Gson


class PayBottomSheetFragment(var callBack: ChargesFragment.CallBackInterface) : BaseBottomSheetDialogFragment() {
    private lateinit var binding:FragmentPayBottomSheetBinding
    private lateinit var viewModel:ChargesViewModel
    private lateinit var userDetail: UserDetailResponseModel
    var totalAmount = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            totalAmount = it.getString(Constant.TOTAL_AMOUNT)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_pay_bottom_sheet,
            container,
            false
        )
        viewModel = ViewModelProvider(
            this,
            ViewModelFactoryClass(requireActivity().application)
        )[ChargesViewModel::class.java]
        binding.tvPaymentDetail.text = "Your total due is ₹ ${totalAmount}"
        binding.btnPay.setOnClickListener(this)
        binding.ivClose.setOnClickListener(this)
        userDetail = Gson().fromJson(sharedPreference.getValueString(Constant.USER_DETAIL), UserDetailResponseModel::class.java)
        setObserver()
        return binding.root
    }

    private fun setObserver() {
        viewModel.merchantauthtokenResponse.observe(requireActivity()) { response ->
            when (response) {
                is Resource.Success -> {
                    ProgressDialog.hideProgressBar()
                    callBillData(response.data)
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
        viewModel.userBillDataResponse.observe(requireActivity()) { response ->
            when (response) {
                is Resource.Success -> {
                    ProgressDialog.hideProgressBar()
                    redirectToMobilPay(response.data?.data)
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

    private fun redirectToMobilPay(data: String?) {
        /*val viewIntent = Intent(
            "android.intent.action.VIEW",
            Uri.parse(data)
        )
        startActivity(viewIntent)*/

        dismiss()
        data?.let { MobilPayFragment(it,callBack) }
            ?.let { (activity as DashboardActivity).addFragment(it) }
    }

    private fun callBillData(data: MerchantauthtokenResponse?) {
        val userInfo = UserBillDataRequest.UserInfo()
        userInfo.state = userDetail.state
        userInfo.email = userDetail.email
        userInfo.gender = userDetail.gender
        userInfo.dob = userDetail.dateOfBirth
        userInfo.city = userDetail.city
        userInfo.pincode = userDetail.postalCode
        userInfo.status = "1"
        userInfo.firstName = userDetail.firstname
        userInfo.lastName = userDetail.surname
        userInfo.addressLine1 = userDetail.address
        userInfo.addressLine2 = userDetail.address2
        userInfo.planName = ""
        val billDescription = UserBillDataRequest.BillDescription()
        billDescription.InvoiceAmount = binding.etAmount.text.toString()
        billDescription.InvoiceDate = Utils.getDateTime("dd/MM/yyyy")
        billDescription.InvoiceNumber = Utils.getInvoiceNumber()
        val userBillDataRequest = UserBillDataRequest()
        userBillDataRequest.token = data?.data?.token
        userBillDataRequest.timestamp = data?.data?.timestamp
        userBillDataRequest.mobile = userDetail.phone
        userBillDataRequest.userId = userDetail.userid
        userBillDataRequest.BillNo = Utils.getInvoiceNumber()
        userBillDataRequest.cafNumber = Utils.getInvoiceNumber()
        userBillDataRequest.Balance = binding.etAmount.text.toString()
        userBillDataRequest.BillDueDate = Utils.getDateTime("dd/MM/yyyy")
        userBillDataRequest.agentId = "101TL"
        userBillDataRequest.updatePayment = "0"
        userBillDataRequest.type = "F"
        userBillDataRequest.ekycStatus = "0"
        userBillDataRequest.publicKey = Constant.PUBLIC_KEY
        userBillDataRequest.merchantId = Constant.MERCHANT_ID
        userBillDataRequest.userInfo = userInfo
        userBillDataRequest.billDescription = billDescription
        viewModel.getUserBillData(Constant.BILL_DETAIL,userBillDataRequest)
    }

    private fun validate(){
        if(TextUtils.isEmpty(binding.etAmount.text)){
            showToast(resources.getString(R.string.please_enter_amount))
        }else if(binding.etAmount.text.toString().toInt() in 1..totalAmount.toInt()){
            val merchantAuthTokenRequest = MerchantauthtokenRequest(
                Constant.END_POINT,Constant.KEY,Constant.MERCHANT_ID
            )
            viewModel.getMerchantAuthToken(Constant.MERCHANT_AUTH_URL,merchantAuthTokenRequest)
        }else{
            showToast(resources.getString(R.string.please_enter_valid_amount))
        }
    }

    override fun onClick(p0: View?) {
        super.onClick(p0)
        when(p0?.id){
            binding.btnPay.id->{
                validate()
                hideKeyboard(p0)
            }
            binding.ivClose.id-> {
                dismiss()
                callBack.onBackPress()
            }
        }
    }

}