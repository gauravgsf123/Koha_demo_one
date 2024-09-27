package com.bbbdem.koha.module.dashboard.bottomsheet

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bbbdem.koha.R
import com.bbbdem.koha.app.BaseBottomSheetDialogFragment
import com.bbbdem.koha.common.Constant
import com.bbbdem.koha.databinding.FragmentPlaceHoldsBinding
import com.bbbdem.koha.module.dashboard.DashboardViewModel
import com.bbbdem.koha.module.dashboard.model.PlaceHoldRequestModel
import com.bbbdem.koha.module.registration.model.AllLibraryResponseModel
import com.bbbdem.koha.network.Resource
import com.bbbdem.koha.network.ViewModelFactoryClass
import com.bbbdem.koha.utils.ProgressDialog
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class PlaceHoldsFragment(var biblioId:Int) : BaseBottomSheetDialogFragment() {
    private lateinit var binding:FragmentPlaceHoldsBinding
    private lateinit var viewModel:DashboardViewModel
    private var libraryList = ArrayList<String>()
    private var library = ""
    private var isShow = false
    val myCalendar = Calendar.getInstance()
    private lateinit var date : DatePickerDialog.OnDateSetListener


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
            R.layout.fragment_place_holds,
            container,
            false
        )
        viewModel = ViewModelProvider(
            this,
            ViewModelFactoryClass(requireActivity().application)
        )[DashboardViewModel::class.java]
        binding.btnShowHide.setOnClickListener(this)
        binding.btnConfirmHold.setOnClickListener(this)
        binding.etDate.setOnClickListener(this)
        binding.ivClose.setOnClickListener(this)
        viewModel.getLibraries()
        setObserver()

        date = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, day)
            updateLabel()
        }

        return binding.root
    }

    private fun updateLabel() {
        val myFormat = "yyyy-MM-dd"
        val dateFormat = SimpleDateFormat(myFormat, Locale.US)
        binding?.etDate?.setText(dateFormat.format(myCalendar.time))
    }

    override fun onClick(view: View?) {
        when(view?.id){
            binding.ivClose.id->dismiss()
            binding.btnShowHide.id->showHide()
            binding.btnConfirmHold.id->{
                var placeHoldRequestModel = PlaceHoldRequestModel()
                placeHoldRequestModel.patronId = sharedPreference.getValueInt(Constant.PATRON_ID)
                placeHoldRequestModel.biblioId = biblioId
                placeHoldRequestModel.pickupLibraryId = library
                if(isShow){ placeHoldRequestModel.expirationDate = binding.etDate.text.toString()}
                viewModel.placeHold(placeHoldRequestModel)
            }
            binding.etDate.id->{
                DatePickerDialog(
                    requireContext(),
                    date,
                    myCalendar[Calendar.YEAR],
                    myCalendar[Calendar.MONTH],
                    myCalendar[Calendar.DAY_OF_MONTH]
                ).show()
            }
        }
    }

    private fun getRequest():PlaceHoldRequestModel{
        var placeHoldRequestModel = PlaceHoldRequestModel()
        placeHoldRequestModel.patronId = sharedPreference.getValueInt(Constant.PATRON_ID)
        placeHoldRequestModel.biblioId = biblioId
        placeHoldRequestModel.pickupLibraryId = library
        if(isShow){
            placeHoldRequestModel.expirationDate = binding.etDate.text.toString()
        }

        return placeHoldRequestModel
    }

    private fun showHide(){
        if(isShow){
            binding.tvHoldNotNeeded.visibility = View.GONE
            binding.etDate.visibility = View.GONE
            isShow = false
        }else{
            binding.tvHoldNotNeeded.visibility = View.VISIBLE
            binding.etDate.visibility = View.VISIBLE
            isShow = true
        }
    }

    private fun setObserver() {
        viewModel.allLibraryResponseModel.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    ProgressDialog.hideProgressBar()
                    setupSpinnerLibrary(response.data)
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

        viewModel.placeHoldResponseModel.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    ProgressDialog.hideProgressBar()
                    dismiss()
                    showToast(requireContext().resources.getString(R.string.place_hold_successful))
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

    private fun setupSpinnerLibrary(list: List<AllLibraryResponseModel>?) {
        libraryList.clear()
        libraryList.add(getString(R.string.select_library))
        list?.forEach {
            it.name?.let { it1 -> libraryList.add(it1) }
        }
        val arrayAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_dropdown_item_1line,libraryList)
        binding.spinnerBookLibrary.adapter = arrayAdapter
        binding.spinnerBookLibrary.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SuspiciousIndentation")
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if(position>0)
                    library = list?.get(position-1)?.libraryId ?: ""
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }
}