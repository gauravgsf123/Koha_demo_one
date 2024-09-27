package com.bbbdem.koha.library.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.bbbdem.koha.R
import com.bbbdem.koha.app.BaseFragment
import com.bbbdem.koha.common.Constant
import com.bbbdem.koha.databinding.FragmentLibraryInfoBinding
import com.bbbdem.koha.login.model.UserDetailResponseModel
import com.google.gson.Gson


class LibraryInfoFragment(var viewpager: ViewPager2) : BaseFragment() {
    private lateinit var binding: FragmentLibraryInfoBinding
    private lateinit var userDetail: UserDetailResponseModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_library_info,
            container,
            false
        )
        addListItems()
        inRv()
        return binding.root
    }

    override fun onClick(view: View?) {
        binding?.run {
            when(view?.id){
                btnNext.id->{
                    viewpager.currentItem = 1
                }
            }
        }
    }

    private fun inRv() {
        userDetail = Gson().fromJson(sharedPreference.getValueString(Constant.USER_DETAIL), UserDetailResponseModel::class.java)
        binding.run {
            btnNext.setOnClickListener(this@LibraryInfoFragment)
            etCardNumber.text = userDetail.cardnumber
            etUsername.text = userDetail.userid
            var split = userDetail.expiryDate?.split("-")
            tvYear.text = split?.get(0) ?: ""
            tvMonth.text = split?.get(1) ?: ""
            tvDay.text = split?.get(2) ?: ""
            etLibrary.text = userDetail.libraryId
            etCategory.text = userDetail.categoryId
        }

    }

    private fun addListItems() {

    }

}