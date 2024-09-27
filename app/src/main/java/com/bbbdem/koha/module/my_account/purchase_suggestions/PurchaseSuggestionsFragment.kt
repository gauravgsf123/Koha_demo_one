package com.bbbdem.koha.module.my_account.purchase_suggestions

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bbbdem.koha.R
import com.bbbdem.koha.app.BaseFragment
import com.bbbdem.koha.common.Constant
import com.bbbdem.koha.databinding.FragmentPurchaseSuggestionsBinding
import com.bbbdem.koha.module.dashboard.DashboardActivity
import com.bbbdem.koha.module.my_account.purchase_suggestions.model.ItemResponseModel
import com.bbbdem.koha.module.my_account.purchase_suggestions.model.SuggestionModel
import com.bbbdem.koha.module.registration.model.AllLibraryResponseModel
import com.bbbdem.koha.network.Resource
import com.bbbdem.koha.network.ViewModelFactoryClass
import com.bbbdem.koha.utils.ProgressDialog
import com.bbbdem.koha.utils.Utils


class PurchaseSuggestionsFragment : BaseFragment() {
    private lateinit var binding:FragmentPurchaseSuggestionsBinding
    private lateinit var viewModel: SuggestionViewModel
    private var libraryList = ArrayList<String>()
    private var itemList = ArrayList<String>()
    private var library = ""
    private var item = ""

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
            R.layout.fragment_purchase_suggestions,
            container,
            false
        )
        viewModel = ViewModelProvider(
            this,
            ViewModelFactoryClass(requireActivity().application)
        )[SuggestionViewModel::class.java]
        viewModel.getItem()
        setObserver()
        binding.btnSubmit.setOnClickListener(this)
        binding.btnCancel.setOnClickListener(this)
        (activity as DashboardActivity).run {
            showToolbar(true)
            setAppTitle(resources.getString(R.string.your_purchase_suggestions))
        }

        return binding.root
    }

    private fun setObserver() {
        viewModel.allLibraryResponseModel.observe(requireActivity()) { response ->
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
        viewModel.itemResponseModel.observe(requireActivity()) { response ->
            when (response) {
                is Resource.Success -> {
                    ProgressDialog.hideProgressBar()
                    setupSpinnerItem(response.data)
                    viewModel.getLibraries()
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
        viewModel.suggestionModel.observe(requireActivity()) { response ->
            when (response) {
                is Resource.Success -> {
                    ProgressDialog.hideProgressBar()
                    showToast(requireContext().resources.getString(R.string.suggestion_added_successsful))
                    (activity as DashboardActivity).popBack()
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

    private fun setupSpinnerItem(data: List<ItemResponseModel>?) {
        itemList.clear()
        itemList.add(getString(R.string.select_item))
        data?.forEach {
            it.description?.let { it1 -> itemList.add(it1) }
        }
        var arrayAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_dropdown_item_1line,itemList)
        binding.spinnerItemType.adapter = arrayAdapter
        binding.spinnerItemType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SuspiciousIndentation")
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if(position>0)
                    item = data?.get(position-1)?.categoryName ?: ""
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
        var arrayAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_dropdown_item_1line,libraryList)
        binding.spinnerLibrary.adapter = arrayAdapter
        binding.spinnerLibrary.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SuspiciousIndentation")
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if(position>0)
                    library = list?.get(position-1)?.libraryId ?: ""
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    private fun validate(){
        binding?.run {
            if(TextUtils.isEmpty(etTitle.text.toString())){
                Toast.makeText(requireContext(),"Please enter book title", Toast.LENGTH_LONG).show()
            }/*else if(TextUtils.isEmpty(etAuthor.text.toString())){
                Toast.makeText(requireContext(),"Please enter author name", Toast.LENGTH_LONG).show()
            }else if(TextUtils.isEmpty(etISBN.text.toString())){
                Toast.makeText(requireContext(),"Please enter ISBN number", Toast.LENGTH_LONG).show()
            }else if(TextUtils.isEmpty(etPublisher.text.toString())){
                Toast.makeText(requireContext(),"Please enter publisher name", Toast.LENGTH_LONG).show()
            }else if(TextUtils.isEmpty(etPublicationYear.text.toString())){
                Toast.makeText(requireContext(),"Please enter publication year", Toast.LENGTH_LONG).show()
            }else if(TextUtils.isEmpty(etQuantity.text.toString())){
                Toast.makeText(requireContext(),"Please enter quantity", Toast.LENGTH_LONG).show()
            }*/else {
                val copyRightDate = if(etPublicationYear.text.toString().isNotEmpty()) etPublicationYear.text.toString().toInt()
                else 0
                var requestModel = SuggestionModel(author = etAuthor.text.toString(),
                    isbn = etISBN.text.toString(),
                    note = etNotes.text.toString(),
                    publisherCode = etPublisher.text.toString(),
                    copyrightDate = copyRightDate,
                    quantity = etQuantity.text.toString(),
                    suggestedBy = sharedPreference.getValueInt(Constant.PATRON_ID),
                    suggestionDate = Utils.getDateTime("yyyy-MM-dd"),
                    title = etTitle.text.toString(),
                    libraryId = library,
                    itemType = item)
                viewModel.addSuggestions(requestModel)
            }
        }
    }

    override fun onClick(view: View?) {
        binding.run {
            when(view?.id){
                btnSubmit.id->validate()
                btnCancel.id->(activity as DashboardActivity).popBack()
            }
        }

    }

}