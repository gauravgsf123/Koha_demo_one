package com.bbbdem.koha.library.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bbbdem.koha.R
import com.bbbdem.koha.app.BaseFragment
import com.bbbdem.koha.common.Constant
import com.bbbdem.koha.databinding.FragmentMainAddressBinding
import com.bbbdem.koha.login.model.UserDetailResponseModel
import com.bbbdem.koha.module.my_account.personal_detail.PersonalDetailRequestModel
import com.bbbdem.koha.module.my_account.personal_detail.PersonalDetailViewModel
import com.bbbdem.koha.network.Resource
import com.bbbdem.koha.network.ViewModelFactoryClass
import com.bbbdem.koha.utils.ProgressDialog
import com.google.gson.Gson

class MainAddressFragment(var viewpager: ViewPager2) : BaseFragment() {
    private lateinit var binding: FragmentMainAddressBinding
    private lateinit var userDetail: UserDetailResponseModel
    private lateinit var viewModel: PersonalDetailViewModel
    private var isEdit = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_main_address,
            container,
            false
        )
        viewModel = ViewModelProvider(
            this,
            ViewModelFactoryClass(requireActivity().application)
        )[PersonalDetailViewModel::class.java]
        setObserver()
        inRv()
        return binding.root
    }

    override fun onClick(view: View?) {
        binding.run {
            when(view?.id){
                ivEdit.id->{
                    if(!isEdit){
                        btnNext.text = "Submit"//activity?.resources?.getText(R.string.submit)
                        btnPrevious.text = "Cancel"//activity?.resources?.getText(R.string.cancel)
                        ivEdit.visibility = View.GONE
                        editable(true)
                    } else {}
                }
                btnNext.id->{
                    if(isEdit){
                        var personalDetailRequestModel = PersonalDetailRequestModel()
                        personalDetailRequestModel.address = etAddress.text.toString()
                        personalDetailRequestModel.address2 = etAddress2.text.toString()
                        personalDetailRequestModel.city = etCity.text.toString()
                        personalDetailRequestModel.state = etState.text.toString()
                        personalDetailRequestModel.postalCode = etZipCode.text.toString()
                        personalDetailRequestModel.country = etCountry.text.toString()
                        personalDetailRequestModel.surname = userDetail.surname
                        personalDetailRequestModel.libraryId = userDetail.libraryId
                        personalDetailRequestModel.categoryId = userDetail.categoryId
                        userDetail.patronId?.let { viewModel.updatePersonalDetail(it,personalDetailRequestModel) }
                    }else viewpager.currentItem = 3
                }
                btnPrevious.id->{
                    if(isEdit){
                        btnNext.text = "Next"//activity?.resources?.getText(R.string.next)
                        btnPrevious.text = "Previous"//activity?.resources?.getText(R.string.previous)
                        ivEdit.visibility = View.VISIBLE
                        editable(false)
                    } else viewpager.currentItem = 1
                }
                else -> {}
            }
        }
    }

    private fun editable(editable: Boolean){
        isEdit = editable
        binding.run {
            etAddress.isEnabled = editable
            etAddress2.isEnabled = editable
            etCity.isEnabled = editable
            etState.isEnabled = editable
            etZipCode.isEnabled = editable
            etCountry.isEnabled = editable
        }
    }

    private fun inRv() {
        userDetail = Gson().fromJson(sharedPreference.getValueString(Constant.USER_DETAIL), UserDetailResponseModel::class.java)
        binding.run {
            ivEdit.setOnClickListener(this@MainAddressFragment)
            btnNext.setOnClickListener(this@MainAddressFragment)
            btnPrevious.setOnClickListener(this@MainAddressFragment)
            etAddress.setText(userDetail.address)
            etAddress2.setText(userDetail.address2)
            etCity.setText(userDetail.city)
            etState.setText(userDetail.state)
            etZipCode.setText(userDetail.postalCode)
            etCountry.setText(userDetail.country)
        }
    }

    private fun setObserver() {
        viewModel.personalDetailResponseModel.observe(requireActivity()) { response ->
            when (response) {
                is Resource.Success -> {
                    ProgressDialog.hideProgressBar()
                    binding.run {
                        sharedPreference.save(Constant.USER_DETAIL,Gson().toJson(response.data!!))
                        showToast("Address detail update successful")
                        btnNext.text = "Next"
                        btnPrevious.text = "Previous"
                        ivEdit.visibility = View.VISIBLE
                        editable(false)
                    }
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