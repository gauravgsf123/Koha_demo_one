package com.bbbdem.koha.module.my_account.charges

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bbbdem.koha.R
import com.bbbdem.koha.app.BaseFragment
import com.bbbdem.koha.common.Constant
import com.bbbdem.koha.databinding.FragmentChargesBinding
import com.bbbdem.koha.module.dashboard.DashboardActivity
import com.bbbdem.koha.module.my_account.charges.model.PaymentCreditRequest
import com.bbbdem.koha.network.Resource
import com.bbbdem.koha.network.ViewModelFactoryClass
import com.bbbdem.koha.utils.ProgressDialog
import com.bbbdem.koha.utils.Utils


class ChargesFragment : BaseFragment() {
    private lateinit var binding:FragmentChargesBinding
    private lateinit var viewModel:ChargesViewModel
    var totalAmount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_charges,
            container,
            false
        )
        viewModel = ViewModelProvider(
            this,
            ViewModelFactoryClass(requireActivity().application)
        )[ChargesViewModel::class.java]
        (activity as DashboardActivity?)?.run {
            showToolbar(true)
            //setTitle(getString(R.string.your_personal_detail))
        }
        viewModel.getCharges(sharedPreference.getValueInt(Constant.PATRON_ID))
        setObserver()
        if(sharedPreference.getValueInt(Constant.PAYMENT_GATEWAY)==0) binding.tvPay.visibility = View.GONE
        else binding.tvPay.visibility = View.VISIBLE
        binding.tvFine.setOnClickListener(this)
        binding.tvPay.setOnClickListener(this)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        selectedView(binding.tvFine)
    }

    private fun setObserver() {
        viewModel.chargesResponseModel.observe(requireActivity()) { response ->
            when (response) {
                is Resource.Success -> {
                    ProgressDialog.hideProgressBar()
                    setupChargeRecylerView(response.data!!)
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

        viewModel.paymentCreditResponse.observe(requireActivity()) { response ->
            when (response) {
                is Resource.Success -> {
                    ProgressDialog.hideProgressBar()
                    viewModel.getCharges(sharedPreference.getValueInt(Constant.PATRON_ID))
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

    private fun setupChargeRecylerView(data:List<ChargesResponseModel>){
        totalAmount = 0
        var chargeList = ArrayList<ChargesResponseModel>()
        data.forEach {
            if(it.amountOutstanding!! >0){
                totalAmount+=it.amountOutstanding!!
                chargeList.add(it)
            }
        }
        binding.tvTotalDue.text = "₹ ${totalAmount.toFloat()}"

        binding.rvCharge.adapter = ChargeAdapter(chargeList)
    }

    override fun onClick(p0: View?) {
        binding.run {
            when(p0?.id){
                tvFine.id->selectedView(tvFine)
                tvPay.id->{
                    selectedView(tvPay)
                    var bundle = Bundle()
                    bundle.putString(Constant.TOTAL_AMOUNT,"$totalAmount")
                    bundle.putString(Constant.TOTAL_AMOUNT,"$totalAmount")
                    val fragmentPayBottomSheetFragment = PayBottomSheetFragment(object : CallBackInterface{
                        override fun onBackPress() {
                            selectedView(binding.tvFine)
                        }

                        override fun onPaymentDone(tnxId: String) {
                            paymentCredit(tnxId)
                        }

                    })
                    fragmentPayBottomSheetFragment.arguments = bundle
                    fragmentPayBottomSheetFragment.show(parentFragmentManager,"PayBottomSheetFragment")
                }
            }
        }
    }

    private fun paymentCredit(tnxId: String){
        val paymentCreditRequest = PaymentCreditRequest()
        paymentCreditRequest.creditType = "PAYMENT"
        paymentCreditRequest.amount = "1"
        paymentCreditRequest.libraryId = sharedPreference.getValueString(Constant.LIBRARY_ID)
        paymentCreditRequest.paymentType = "ONLINE"
        paymentCreditRequest.date = Utils.getDateTime("yyyy-MM-dd")
        paymentCreditRequest.description = "Overdue Charges Payment"
        paymentCreditRequest.note = "$tnxId payment_transaction_id"
        viewModel.paymentCredit(sharedPreference.getValueInt(Constant.PATRON_ID),paymentCreditRequest)
    }

    private fun selectedView(textView: TextView){
        binding.run {
            tvFine.isSelected = false
            tvPay.isSelected = false
            tvFine.setTextColor(resources.getColor(R.color.primary_dark))
            tvPay.setTextColor(resources.getColor(R.color.primary_dark))
        }
        textView.isSelected = true
        textView.setTextColor(resources.getColor(R.color.white))
    }

    interface CallBackInterface{
        fun onBackPress()
        fun onPaymentDone(tnxId:String)
    }

}