package com.bbbdem.koha.module.my_account.summary

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getColor
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bbbdem.koha.R
import com.bbbdem.koha.app.BaseFragment
import com.bbbdem.koha.common.Constant
import com.bbbdem.koha.databinding.FragmentSummaryDetailBinding
import com.bbbdem.koha.module.dashboard.DashboardActivity
import com.bbbdem.koha.module.dashboard.fragment.BookDetailFragment
import com.bbbdem.koha.module.my_account.charges.ChargeAdapter
import com.bbbdem.koha.module.my_account.charges.ChargesResponseModel
import com.bbbdem.koha.module.my_account.summary.model.CheckoutResponseModel
import com.bbbdem.koha.module.my_account.summary.model.HoldsResponseModel
import com.bbbdem.koha.network.Resource
import com.bbbdem.koha.network.ViewModelFactoryClass
import com.bbbdem.koha.utils.ProgressDialog
import com.bbbdem.koha.utils.Utils
import com.bbbdem.koha.view.ConfirmationDialogFragment


class SummaryDetailFragment : BaseFragment() {
    private lateinit var binding:FragmentSummaryDetailBinding
    private lateinit var viewModel: SummaryDetailViewModel
    private lateinit var list: List<CheckoutResponseModel>
    private var holdList: List<HoldsResponseModel> = ArrayList()
    private var chargeList: List<ChargesResponseModel> = ArrayList()


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
            R.layout.fragment_summary_detail,
            container,
            false
        )
        viewModel = ViewModelProvider(
            this,
            ViewModelFactoryClass(requireActivity().application)
        )[SummaryDetailViewModel::class.java]
        binding.clCheckout.isSelected = true
        binding.clCheckout.setOnClickListener(this)
        binding.clOverdue.setOnClickListener(this)
        binding.clHolds.setOnClickListener(this)
        binding.clFeeDue.setOnClickListener(this)
        //selectTab(binding.clCheckout,binding.tvCheckout,binding.tvCheckoutCount)
        (activity as DashboardActivity?)?.run {
            showToolbar(true)
            setAppTitle(getString(R.string.your_summary))
        }
        viewModel.getCheckout(sharedPreference.getValueInt(Constant.PATRON_ID).toString())
        setObserver()
        return binding.root
    }

    private fun setObserver() {
        viewModel.checkoutResponseModel.observe(requireActivity()) { response ->
            when (response) {
                is Resource.Success -> {
                    ProgressDialog.hideProgressBar()
                    list = response.data!!
                    viewModel.getHolds(sharedPreference.getValueInt(Constant.PATRON_ID).toString())
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
        viewModel.holdsResponseModel.observe(requireActivity()) { response ->
            when (response) {
                is Resource.Success -> {
                    ProgressDialog.hideProgressBar()
                    holdList = response.data!!
                    binding.tvHoldsCount.text = holdList.size.toString()
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

        viewModel.chargesResponseModel.observe(requireActivity()) { response ->
            when (response) {
                is Resource.Success -> {
                    ProgressDialog.hideProgressBar()
                    chargeList = response.data!!
                    selectTab(binding.clCheckout,binding.tvCheckout,binding.tvCheckoutCount)
                    var totalAmount = 0
                    chargeList.forEach {
                        totalAmount+=it.amountOutstanding!!
                    }
                    binding.tvTotalDue.text = "₹ ${totalAmount.toFloat()}"
                    binding.tvFeeDueAmount.text = "${totalAmount.toFloat()}"

                    //checkDue(chargeList,list)
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
        viewModel.renewalResponseModel.observe(requireActivity()) { response ->
            when (response) {
                is Resource.Success -> {
                    ProgressDialog.hideProgressBar()
                    response.message?.let { showToast(it) }
                    viewModel.getCheckout(sharedPreference.getValueInt(Constant.PATRON_ID).toString())
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

        viewModel.cancelHoldResponseModel.observe(requireActivity()) { response ->
            when (response) {
                is Resource.Success -> {
                    ProgressDialog.hideProgressBar()
                    response.message?.let { showToast(it) }
                    viewModel.getCheckout(sharedPreference.getValueInt(Constant.PATRON_ID).toString())
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
                }
            }
        }
    }

    private fun checkDue(chargeList: List<ChargesResponseModel>, list: List<CheckoutResponseModel>):ArrayList<CheckoutResponseModel> {
        val checkout = HashSet<CheckoutResponseModel>()
        chargeList.forEachIndexed { index, chargesResponseModel ->
            list.forEachIndexed { index, checkoutResponseModel ->
                if(checkoutResponseModel.checkoutId.toString() == chargesResponseModel.checkoutId){
                    checkoutResponseModel.amountOutstanding = chargesResponseModel.amountOutstanding
                }
                if(!contains(checkout,checkoutResponseModel.checkoutId!!)){
                    checkout.add(checkoutResponseModel)
                }
            }
        }
        val newCheckout: ArrayList<CheckoutResponseModel> = ArrayList<CheckoutResponseModel>(checkout)
        return newCheckout
    }

    fun contains(list: HashSet<CheckoutResponseModel>, checkoutId: Int): Boolean {
        for (item in list) {
            if (item.checkoutId?.equals(checkoutId) == true) {
                return true
            }
        }
        return false
    }

    private fun showConformationForRenewDialog(checkoutId: Int?) {
        ConfirmationDialogFragment.newInstance(
            resources.getString(R.string.are_you_sure_you_want_to_renew),
            object : ConfirmationDialogFragment.ConfirmationDialogFragmentListener {

                override fun onConfirmationCancelClick() {
                }

                override fun onConfirmationOKClick() {
                    viewModel.renewal(checkoutId!!)
                }

            }
        ).show(parentFragmentManager, "")
    }

    private fun showConformationForCancelDialog(holdId: Int?) {
        ConfirmationDialogFragment.newInstance(
            resources.getString(R.string.are_you_sure_you_want_to_cancel),
            object : ConfirmationDialogFragment.ConfirmationDialogFragmentListener {

                override fun onConfirmationCancelClick() {
                }

                override fun onConfirmationOKClick() {
                    viewModel.cancelHoldItem(holdId!!)
                }

            }
        ).show(parentFragmentManager, "")
    }

    private fun resetView(){
        binding.run {
            clCheckout.isSelected = false
            clOverdue.isSelected = false
            clHolds.isSelected = false
            clFeeDue.isSelected = false
            tvCheckout.setTextColor(resources.getColor(R.color.primary_dark))
            tvOverdue.setTextColor(resources.getColor(R.color.primary_dark))
            tvHolds.setTextColor(resources.getColor(R.color.primary_dark))
            tvFeeDue.setTextColor(resources.getColor(R.color.primary_dark))
            tvCheckoutCount.setTextColor(resources.getColor(R.color.primary_dark))
            tvOverdueCount.setTextColor(resources.getColor(R.color.primary_dark))
            tvHoldsCount.setTextColor(resources.getColor(R.color.primary_dark))
            tvFeeDueAmount.setTextColor(resources.getColor(R.color.primary_dark))
            tvCheckoutCount.setDrawableColor(R.color.primary_dark)
            tvOverdueCount.setDrawableColor(R.color.primary_dark)
            tvHoldsCount.setDrawableColor(R.color.primary_dark)
            tvFeeDueAmount.setDrawableColor(R.color.primary_dark)

        }
    }

    fun TextView.setDrawableColor(@ColorRes color: Int) {
        compoundDrawables.filterNotNull().forEach {
            it.colorFilter = PorterDuffColorFilter(getColor(context, color), PorterDuff.Mode.SRC_IN)
        }
    }

    override fun onClick(view: View?) {
        binding.run{
            when(view?.id){
                clCheckout.id->selectTab(binding.clCheckout,binding.tvCheckout,binding.tvCheckoutCount)
                clOverdue.id->selectTab(binding.clOverdue,binding.tvOverdue,binding.tvOverdueCount)
                clHolds.id->selectTab(binding.clHolds,binding.tvHolds,binding.tvHoldsCount)
                clFeeDue.id->selectTab(binding.clFeeDue,binding.tvFeeDue,binding.tvFeeDueAmount)
            }
        }

    }

    private fun selectTab(constraintLayout: ConstraintLayout, textViewTitle: TextView, textViewCount: TextView) {
        resetView()
        constraintLayout.isSelected = true
        textViewTitle.setTextColor(resources.getColor(R.color.white))
        textViewCount.setTextColor(resources.getColor(R.color.white))
        textViewCount.setDrawableColor(R.color.white)
        binding.tvCheckoutCount.text = list.size.toString()
        binding.tvOverdueCount.text = getCheckoutData(list,false).size.toString()
        when(constraintLayout){
            binding.clCheckout->{
                showHideView(true)
                setupRecylerView(checkDue(chargeList,list),true)
                binding.tvCheckedOut.run {
                    text = "Checkout (${list.size})"
                    visibility = View.VISIBLE
                }
            }
            binding.clOverdue->{
                setupRecylerView(getCheckoutData(checkDue(chargeList,list),false),false)
                showHideView(true)
                binding.tvCheckedOut.run {
                    text = "Overdue (${list.size})"
                    visibility = View.VISIBLE
                }
            }
            binding.clHolds->{
                setupHoldRecylerView(holdList)
                showHideView(true)
                binding.tvCheckedOut.run {
                    text = "Holds (${holdList.size})"
                    visibility = View.VISIBLE
                }
            }
            binding.clFeeDue->{
                showHideView(false)
                binding.tvCheckedOut.visibility = View.GONE
                if(chargeList.isNotEmpty()){
                    setupChargeRecylerView(chargeList)
                    binding.nestedScrollView.visibility = View.VISIBLE
                }else {
                    binding.nestedScrollView.visibility = View.GONE
                }

            }
        }
    }

    private fun showHideView(isShow:Boolean){
        binding.run {
            if(isShow) {
                rvSummary.visibility = View.VISIBLE
                tvCheckedOut.visibility = View.VISIBLE
                nestedScrollView.visibility = View.GONE
            }else{
                rvSummary.visibility = View.GONE
                tvCheckedOut.visibility = View.GONE
                nestedScrollView.visibility = View.VISIBLE
            }
        }

    }

    private fun setupChargeRecylerView(data:List<ChargesResponseModel>){
        var chargeList = ArrayList<ChargesResponseModel>()
        data.forEach {
            if(it.amountOutstanding!! >0){
                chargeList.add(it)
            }
        }
        binding.rvCharge.adapter = ChargeAdapter(chargeList)
    }

    private fun setupRecylerView(data: List<CheckoutResponseModel>?, isDue: Boolean) {
        binding.rvSummary.adapter = CheckoutAdapter(data as ArrayList<CheckoutResponseModel>,isDue,object :
            CheckoutAdapter.SummaryDetailCallBack {
            override fun onRenewClickListener(
                adapterPosition: Int,
                checkoutResponseModel: CheckoutResponseModel
            ) {
                showConformationForRenewDialog(checkoutResponseModel.checkoutId)
            }

            override fun onTitleClickListener(
                adapterPosition: Int,
                checkoutResponseModel: CheckoutResponseModel
            ) {
                (activity as DashboardActivity).addFragment(
                    BookDetailFragment(checkoutResponseModel?.bookDetailResponseModel!!)
                )
            }
        }
        )
    }

    private fun setupHoldRecylerView(data: List<HoldsResponseModel>?) {
        binding.rvSummary.adapter = HoldsAdapter(data as ArrayList<HoldsResponseModel>,object :
            HoldsAdapter.HoldsDetailCallBack {
            override fun onCancelClickListener(
                adapterPosition: Int,
                holdsResponseModel: HoldsResponseModel
            ) {
                showConformationForCancelDialog(holdsResponseModel.holdId)
            }

            override fun onTitleClickListener(
                adapterPosition: Int,
                holdsResponseModel: HoldsResponseModel
            ) {
                (activity as DashboardActivity).addFragment(
                    BookDetailFragment(holdsResponseModel?.bookDetailResponseModel!!)
                )
            }

        })
    }

    private fun getCheckoutData(list: List<CheckoutResponseModel>,isDue:Boolean): List<CheckoutResponseModel>{
        var filterList = mutableListOf<CheckoutResponseModel>()//<CheckoutResponseModel>()
        list.forEach {
            if(Utils.checkDate(it.dueDate!!)==isDue){
                filterList.add(it)
            }
        }
        return filterList
    }

}