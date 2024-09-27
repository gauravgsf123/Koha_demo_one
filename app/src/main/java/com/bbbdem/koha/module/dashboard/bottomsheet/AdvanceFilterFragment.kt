package com.bbbdem.koha.module.dashboard.bottomsheet

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
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
import com.bbbdem.koha.databinding.FragmentAdvanceFilterBinding
import com.bbbdem.koha.module.dashboard.DashboardActivity
import com.bbbdem.koha.module.dashboard.DashboardViewModel
import com.bbbdem.koha.module.dashboard.fragment.BookListFragment
import com.bbbdem.koha.module.dashboard.fragment.SearchListFragment
import com.bbbdem.koha.module.my_account.purchase_suggestions.model.ItemResponseModel
import com.bbbdem.koha.module.registration.model.AllLibraryResponseModel
import com.bbbdem.koha.network.Resource
import com.bbbdem.koha.network.ViewModelFactoryClass
import com.bbbdem.koha.utils.ProgressDialog
import com.bbbdem.koha.view.SearchMoreView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class AdvanceFilterFragment : BaseBottomSheetDialogFragment() {
    private lateinit var binding:FragmentAdvanceFilterBinding
    private lateinit var viewModel: DashboardViewModel
    val myCalendar = Calendar.getInstance()
    private lateinit var date : DatePickerDialog.OnDateSetListener
    private var libraryList = ArrayList<String>()
    private var itemList = ArrayList<String>()
    private var library = ""
    private var category = ""
    private var searchBy = ""
    private var searchWith = ""
    private var availableOnly = false
    private var addMoreList = mutableListOf<SearchMoreView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_advance_filter,
            container,
            false
        )

        viewModel = ViewModelProvider(
            this,
            ViewModelFactoryClass(requireActivity().application)
        )[DashboardViewModel::class.java]
        binding.ivClose.setOnClickListener(this)
        binding.btnReset.setOnClickListener(this)
        binding.btnSearch.setOnClickListener(this)
        binding.etDateFrom.setOnClickListener(this)
        binding.etDateTo.setOnClickListener(this)
        viewModel.getLibraries()
        setObserver()
        setupSearchBy()
        setupSearchWith()
        binding.ivClose.setOnClickListener {
            dismiss()
        }
        binding.tvAddMore.setOnClickListener{
            val searchMoreView=SearchMoreView(requireContext())
            searchMoreView.binding.ivRemoveView.setOnClickListener {
                binding.linearLayout.removeView(
                    searchMoreView
                )
                addMoreList.remove(searchMoreView)
            }
            binding.linearLayout.addView(searchMoreView)
            addMoreList.add(searchMoreView)
        }
        binding.cbLoan.setOnCheckedChangeListener { buttonView, isChecked ->
            availableOnly = isChecked
        }
        date = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, day)
            updateLabel()
        }
        setObserver()
        return binding.root
    }

    private fun updateLabel() {
        val myFormat = "yyyy-MM-dd"
        val dateFormat = SimpleDateFormat(myFormat, Locale.US)
    }

    private fun setObserver() {
        viewModel.allLibraryResponseModel.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    ProgressDialog.hideProgressBar()
                    viewModel.getItem()
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

        viewModel.itemResponseModel.observe(requireActivity()) { response ->
            when (response) {
                is Resource.Success -> {
                    ProgressDialog.hideProgressBar()
                    setupSpinnerItem(response.data)
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

    private fun setupSpinnerItem(list: List<ItemResponseModel>?) {
        itemList.clear()
        itemList.add(getString(R.string.select_category))
        list?.forEach {
            it.description?.let { it1 -> itemList.add(it1) }
        }
        val arrayAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_dropdown_item_1line,itemList)
        binding.spinnerBookCategory.adapter = arrayAdapter
        binding.spinnerBookCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SuspiciousIndentation")
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if(position>0)
                    category = list?.get(position-1)?.value ?: ""
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

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

    private fun setupSearchBy() {
        val searchByList = resources.getStringArray(R.array.search_by)
        val arrayAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_dropdown_item_1line,searchByList)
        binding.spinnerSearchBy.adapter = arrayAdapter
        binding.spinnerSearchBy.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SuspiciousIndentation")
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                //if(position>0)
                searchBy = searchByList[position].toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    private fun setupSearchWith() {
        val searchByList = resources.getStringArray(R.array.search_with)
        val arrayAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_dropdown_item_1line,searchByList)
        binding.spinnerContains.adapter = arrayAdapter
        binding.spinnerContains.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SuspiciousIndentation")
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                //if(position>0)
                searchWith = searchByList[position].toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    override fun onClick(p0: View?) {
        super.onClick(p0)
        binding.run {
            when(p0?.id) {
                ivClose.id -> dismiss()
                btnReset.id -> reset()
                btnSearch.id -> {
                    dismiss()
                    if(validate()) {
                        val bundle = Bundle()
                        bundle.putString(Constant.LIBRARY, library)
                        bundle.putString(Constant.CATEGORY, category)
                        bundle.putString(Constant.SEARCH_BY, etSearch.text.toString())
                        bundle.putString(Constant.SEARCH, advanceSearch())
                        bundle.putString(Constant.DATE_RANGE, "${etDateFrom.text}-${etDateTo.text}")
                        bundle.putBoolean(Constant.IS_AVAILABLE, availableOnly)
                        bundle.putInt(Constant.FRAGMENT_TYPE, Constant.FragmentType.ADVANCE_SEARCH)
                        val fragment = SearchListFragment.newInstance()
                        fragment.arguments = bundle
                        (activity as DashboardActivity).replaceFragment(fragment)
                    }else {
                        (activity as DashboardActivity).replaceFragment(BookListFragment(Constant.BookListType.NEW_ARRIVAL))
                    }
                }
            }
        }
    }

    private fun validate():Boolean{
        return if(library.isNotEmpty()){
            true
        }else if(category.isNotEmpty()){
            true
        }else if(binding.etSearch.text.isNotEmpty()){
            true
        }else{
            false
        }
    }

    private fun advanceSearch():String{
        val query = "{\"${searchBy}\": { \"-like\": \"%${binding.etSearch.text.trim()}%\"}}"
        var stringBuffer  = String()
        if(addMoreList.isNotEmpty()) {
            addMoreList.forEachIndexed { index, searchMoreView ->
                if (index == 0) {
                    val query1 =
                        "{ \"${searchMoreView.searchByValue}\": { \"-like\": \"%${searchMoreView.binding.etSearchText.text.trim()}%\" } }"
                    stringBuffer = "{ \"-${searchMoreView.andOr}\": [ $query, $query1 ] }"
                } else {
                    val query1 =
                        "{ \"${searchMoreView.searchByValue}\": { \"-like\": \"%${searchMoreView.binding.etSearchText.text.trim()}%\" } }"
                    stringBuffer = "{ \"-${searchMoreView.andOr}\": [ $stringBuffer, $query1 ] }"
                }
            }
        }else stringBuffer = query
        //val finalstr = "{ \"-and\": [${query} $stringBuffer]}"
        Log.d("finalstr",stringBuffer)
        return stringBuffer
    }

    private fun reset(){
        binding.run {
            library = ""
            category = ""
            searchBy = ""
            etSearch.setText("")
            binding.spinnerSearchBy.setSelection(0)
            binding.spinnerBookLibrary.setSelection(0)
            binding.spinnerBookCategory.setSelection(0)
            binding.spinnerContains.setSelection(0)
            binding.linearLayout.removeAllViews()
            addMoreList.clear()
        }
    }

}