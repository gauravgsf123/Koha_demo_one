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
import com.bbbdem.koha.databinding.FragmentContactInfoBinding
import com.bbbdem.koha.login.model.UserDetailResponseModel
import com.bbbdem.koha.module.my_account.personal_detail.PersonalDetailRequestModel
import com.bbbdem.koha.module.my_account.personal_detail.PersonalDetailViewModel
import com.bbbdem.koha.network.Resource
import com.bbbdem.koha.network.ViewModelFactoryClass
import com.bbbdem.koha.utils.ProgressDialog
import com.google.gson.Gson

class ContactInfoFragment(var viewpager: ViewPager2) : BaseFragment() {
    private lateinit var binding: FragmentContactInfoBinding
    private lateinit var userDetail: UserDetailResponseModel
    private lateinit var viewModel: PersonalDetailViewModel
    private var isEdit = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_contact_info,
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
            when (view?.id) {
                ivEdit.id -> {
                    if (!isEdit) {
                        btnNext.text = "Submit"
                        btnPrevious.visibility = View.VISIBLE
                        ivEdit.visibility = View.GONE
                        editable(true)
                    } else {

                    }
                }
                btnNext.id -> {
                    if(isEdit){
                        var personalDetailRequestModel = PersonalDetailRequestModel()
                        personalDetailRequestModel.phone = etPrimaryPhone.text.toString()
                        personalDetailRequestModel.secondaryPhone = etSecondaryPhone.text.toString()
                        personalDetailRequestModel.email = etPrimaryEmail.text.toString()
                        personalDetailRequestModel.secondaryEmail = etSecondaryEmail.text.toString()
                        personalDetailRequestModel.surname = userDetail.surname
                        personalDetailRequestModel.libraryId = userDetail.libraryId
                        personalDetailRequestModel.categoryId = userDetail.categoryId
                        userDetail.patronId?.let { viewModel.updatePersonalDetail(it,personalDetailRequestModel) }
                    }else viewpager.currentItem = 2
                }
                btnPrevious.id -> {
                    if (isEdit) {
                        btnNext.text = "Previous"
                        ivEdit.visibility = View.VISIBLE
                        btnPrevious.visibility = View.GONE
                        editable(false)
                    } else {

                    }
                }
                else -> {}
            }
        }
    }

        private fun editable(editable: Boolean){
            isEdit = editable
            binding.run {
                etPrimaryPhone.isEnabled = editable
                etSecondaryPhone.isEnabled = editable
                etPrimaryEmail.isEnabled = editable
                etSecondaryEmail.isEnabled = editable
            }
        }

    private fun inRv() {
        userDetail = Gson().fromJson(sharedPreference.getValueString(Constant.USER_DETAIL), UserDetailResponseModel::class.java)
        binding.run {
            ivEdit.setOnClickListener(this@ContactInfoFragment)
            btnNext.setOnClickListener(this@ContactInfoFragment)
            btnPrevious.setOnClickListener(this@ContactInfoFragment)
            etPrimaryPhone.setText(userDetail.phone)
            etSecondaryPhone.setText(userDetail.secondaryPhone)
            etPrimaryEmail.setText(userDetail.email)
            etSecondaryEmail.setText(userDetail.secondaryEmail)
        }
    }

    private fun setObserver() {
        viewModel.personalDetailResponseModel.observe(requireActivity()) { response ->
            when (response) {
                is Resource.Success -> {
                    ProgressDialog.hideProgressBar()
                    binding.run {
                        sharedPreference.save(Constant.USER_DETAIL,Gson().toJson(response.data!!))
                        showToast("Contact detail update successful")
                        btnNext.text = "Next"
                        btnPrevious.text = "Previous"
                        ivEdit.visibility = View.VISIBLE
                        btnPrevious.visibility = View.GONE
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