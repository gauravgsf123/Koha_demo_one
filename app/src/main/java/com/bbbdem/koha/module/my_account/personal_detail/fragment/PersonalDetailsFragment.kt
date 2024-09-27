package com.bbbdem.koha.module.my_account.personal_detail.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bbbdem.koha.R
import com.bbbdem.koha.app.BaseFragment
import com.bbbdem.koha.common.Constant
import com.bbbdem.koha.databinding.FragmentPersonalDetailsBinding
import com.bbbdem.koha.login.model.UserDetailResponseModel
import com.bbbdem.koha.module.my_account.personal_detail.PersonalDetailRequestModel
import com.bbbdem.koha.module.my_account.personal_detail.PersonalDetailViewModel
import com.bbbdem.koha.network.Resource
import com.bbbdem.koha.network.ViewModelFactoryClass
import com.bbbdem.koha.utils.ProgressDialog
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

class PersonalDetailsFragment(var viewpager: ViewPager2) : BaseFragment() {
    private lateinit var binding: FragmentPersonalDetailsBinding
    private lateinit var userDetail: UserDetailResponseModel
    private lateinit var viewModel:PersonalDetailViewModel
    private var isEdit = false
    val myCalendar = Calendar.getInstance()
    private lateinit var date : DatePickerDialog.OnDateSetListener
    private var gender = ""
    private var title = "Mr"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_personal_details,
            container,
            false
        )
        viewModel = ViewModelProvider(
            this,
            ViewModelFactoryClass(requireActivity().application)
        )[PersonalDetailViewModel::class.java]
        inRv()
        setObserver()
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
                    }else{}
                }
                btnNext.id->{
                    if(isEdit){
                        var personalDetailRequestModel = PersonalDetailRequestModel()
                        personalDetailRequestModel.title = title
                        personalDetailRequestModel.firstname = etFirstName.text.toString()
                        personalDetailRequestModel.middleName = etMiddlename.text.toString()
                        personalDetailRequestModel.surname = etSurname.text.toString()
                        personalDetailRequestModel.dateOfBirth = etDOB.text.toString()
                        personalDetailRequestModel.gender = gender
                        personalDetailRequestModel.libraryId = userDetail.libraryId
                        personalDetailRequestModel.categoryId = userDetail.categoryId
                        userDetail.patronId?.let { viewModel.updatePersonalDetail(it,personalDetailRequestModel) }
                    }else viewpager.currentItem = 2
                }
                btnPrevious.id->{
                    if(isEdit){
                        btnNext.text = "Next"//activity?.resources?.getText(R.string.next)
                        btnPrevious.text = "Previous"//activity?.resources?.getText(R.string.previous)
                        ivEdit.visibility = View.VISIBLE
                        editable(false)
                    }else viewpager.currentItem = 0
                }
                etDOB.id->{
                    DatePickerDialog(
                        requireContext(),
                        date,
                        myCalendar[Calendar.YEAR],
                        myCalendar[Calendar.MONTH],
                        myCalendar[Calendar.DAY_OF_MONTH]
                    ).show()
                }
                else -> {}
            }
        }

    }

    private fun editable(editable: Boolean){
        isEdit = editable
        binding.run {
            etFirstName.isEnabled = editable
            etMiddlename.isEnabled = editable
            etSurname.isEnabled = editable
            etDOB.isEnabled = editable
            rgGender.clipChildren = editable
            rbMale.isClickable = editable
            rbFemale.isClickable = editable
            rbOther.isClickable = editable
            rbMale.isEnabled = editable
            rbFemale.isEnabled = editable
            rbOther.isEnabled = editable
        }
    }

    private fun inRv() {
        userDetail = Gson().fromJson(sharedPreference.getValueString(Constant.USER_DETAIL), UserDetailResponseModel::class.java)
        binding.run {
            ivEdit.setOnClickListener(this@PersonalDetailsFragment)
            btnNext.setOnClickListener(this@PersonalDetailsFragment)
            btnPrevious.setOnClickListener(this@PersonalDetailsFragment)
            etDOB?.setOnClickListener(this@PersonalDetailsFragment)
            etFirstName.setText(userDetail.firstname)
            etMiddlename.setText(userDetail.middleName)
            etSurname.setText(userDetail.surname)
            etDOB.setText(userDetail.dateOfBirth)
            gender = userDetail.gender.toString()
            when(gender){
                "M"->rbMale.isChecked= true
                "F"->rbFemale.isChecked= true
                "O"->rbOther.isChecked= true
                }
            }

        date = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, day)
            updateLabel()
        }

        binding?.rgGender?.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                when(checkedId){
                    R.id.rbMale->gender = "M"
                    R.id.rbFemale->gender = "F"
                    R.id.rbOther->gender = "O"
                }
            })

        var salutationArrayList = resources.getStringArray(R.array.salutation)
        var arrayAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_dropdown_item_1line,salutationArrayList)
        binding?.spinnerSalutation?.adapter = arrayAdapter
        binding?.spinnerSalutation?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SuspiciousIndentation")
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                title = salutationArrayList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    private fun updateLabel() {
        val myFormat = "yyyy-MM-dd"
        val dateFormat = SimpleDateFormat(myFormat, Locale.US)
        binding.etDOB.setText(dateFormat.format(myCalendar.time))
    }

    private fun setObserver() {
        viewModel.personalDetailResponseModel.observe(requireActivity()) { response ->
            when (response) {
                is Resource.Success -> {
                    ProgressDialog.hideProgressBar()
                    binding.run {
                        sharedPreference.save(Constant.USER_DETAIL,Gson().toJson(response.data!!))
                        showToast("Personal detail update successful")
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