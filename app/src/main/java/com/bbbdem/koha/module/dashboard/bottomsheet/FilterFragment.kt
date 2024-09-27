package com.bbbdem.koha.module.dashboard.bottomsheet

import android.annotation.SuppressLint
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
import com.bbbdem.koha.databinding.FragmentFilterBinding
import com.bbbdem.koha.module.dashboard.DashboardActivity
import com.bbbdem.koha.module.dashboard.DashboardViewModel
import com.bbbdem.koha.module.dashboard.fragment.BookListFragment
import com.bbbdem.koha.module.dashboard.fragment.SearchListFragment
import com.bbbdem.koha.module.my_account.purchase_suggestions.model.ItemResponseModel
import com.bbbdem.koha.module.registration.model.AllLibraryResponseModel
import com.bbbdem.koha.network.Resource
import com.bbbdem.koha.network.ViewModelFactoryClass
import com.bbbdem.koha.utils.ProgressDialog

class FilterFragment : BaseBottomSheetDialogFragment() {
    private lateinit var binding: FragmentFilterBinding
    private lateinit var viewModel: DashboardViewModel
    private var libraryList = ArrayList<String>()
    private var itemList = ArrayList<String>()
    private var library = ""
    private var category = ""
    private var searchBy = ""

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
            R.layout.fragment_filter,
            container,
            false
        )
        viewModel = ViewModelProvider(
            this,
            ViewModelFactoryClass(requireActivity().application)
        )[DashboardViewModel::class.java]
        binding.tvAdvanceSearch.setOnClickListener(this)
        binding.ivClose.setOnClickListener(this)
        binding.btnReset.setOnClickListener(this)
        binding.btnSearch.setOnClickListener(this)
        viewModel.getLibraries()
        setObserver()
        setupSearchBy()
        return binding.root
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
                    searchBy = searchByList[position].toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
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
        }
    }

    override fun onClick(view: View?) {
        super.onClick(view)
        binding.run {
            when(view?.id) {
                ivClose.id -> dismiss()
                tvAdvanceSearch.id -> {
                    dismiss()
                    AdvanceFilterFragment().show(requireFragmentManager(),"AdvanceFilterFragment")
                }
                btnReset.id -> reset()
                btnSearch.id -> {
                    dismiss()
                    //var query = "{\"${searchBy}\": { \"-like\": \"%${search}%\" }}"
                    //var query = "{ \"-and\": [ { \"home_library_id\": { \"-like\": \"DBAD\" } }, { \"item_type_id\": { \"-like\": \"EN\" } } ] }"
                    if(validate()) {
                        val query = "{\"${searchBy}\": { \"-like\": \"%${etSearch.text.trim()}%\" }}"
                        val bundle = Bundle()
                        bundle.putString(Constant.LIBRARY, library)
                        bundle.putString(Constant.CATEGORY, category)
                        bundle.putString(Constant.SEARCH_BY, etSearch.text.trim().toString())
                        bundle.putString(Constant.SEARCH, query)
                        bundle.putInt(Constant.FRAGMENT_TYPE, Constant.FragmentType.FILTER)
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
}