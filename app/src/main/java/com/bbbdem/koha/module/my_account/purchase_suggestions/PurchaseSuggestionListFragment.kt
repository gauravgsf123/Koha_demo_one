package com.bbbdem.koha.module.my_account.purchase_suggestions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bbbdem.koha.R
import com.bbbdem.koha.app.BaseFragment
import com.bbbdem.koha.common.Constant
import com.bbbdem.koha.databinding.FragmentPurchaseSuggestionListBinding
import com.bbbdem.koha.module.dashboard.DashboardActivity
import com.bbbdem.koha.module.my_account.purchase_suggestions.model.SuggestionListResponseModel
import com.bbbdem.koha.network.Resource
import com.bbbdem.koha.network.ViewModelFactoryClass
import com.bbbdem.koha.utils.ProgressDialog


class PurchaseSuggestionListFragment : BaseFragment() {
    private lateinit var binding:FragmentPurchaseSuggestionListBinding
    private lateinit var viewModel: SuggestionViewModel
    private var selectedItem = mutableListOf<Int>()
    private lateinit var suggestedBy:String
    private var dataList: MutableList<SuggestionListResponseModel> = mutableListOf()
    private lateinit var adapter: SuggestionAdapter
    private val suggestionCallBack = object :SuggestionAdapter.SuggestionCallBack{
        override fun onCheckBoxClickListener(
            adapterPosition: Int,
            suggestionListResponseModel: ArrayList<SuggestionListResponseModel>
        ) {
            /*selectedItem.clear()
            suggestionListResponseModel.forEach {
                if(it.isChecked)selectedItem.add(it.suggestionId!!)
            }
            var gson = Gson().toJson(selectedItem)*/
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_purchase_suggestion_list,
            container,
            false
        )
        binding.tooldbar.binding.tvTitle.text = resources.getString(R.string.your_purchase_suggestions)
        binding.tooldbar.binding.rlBack.setOnClickListener {
            (activity as DashboardActivity).onBackPressed()
        }
        binding.tooldbar.binding.btnCreateNew.visibility = View.VISIBLE
        viewModel = ViewModelProvider(
            this,
            ViewModelFactoryClass(requireActivity().application)
        )[SuggestionViewModel::class.java]
        adapter = SuggestionAdapter(arrayListOf(),suggestionCallBack)
        suggestedBy = "{\"suggested_by\": { \"=\": \"${sharedPreference.getValueInt(Constant.PATRON_ID)}\" } }"
        binding.tooldbar.binding.btnCreateNew.setOnClickListener {
            (activity as DashboardActivity).replaceFragment(PurchaseSuggestionsFragment())
        }
        binding.btnDelete.setOnClickListener {
            selectedItem.clear()
            adapter.getList().forEach {
                if(it.isChecked)selectedItem.add(it.suggestionId!!)
            }
            viewModel.deleteSuggestions(selectedItem as List<Int>)
        }
        binding.cbAll.setOnCheckedChangeListener { buttonView, isChecked ->
            dataList.forEach {
                it.isChecked = isChecked
            }
            adapter.updateContentList(dataList as ArrayList<SuggestionListResponseModel>)
        }
        binding.tooldbar.binding.editTextSearch.doAfterTextChanged{
            filter(it.toString())
        }

        setObserver()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (activity as DashboardActivity).showReadingHistoryToolbar(true)
        viewModel.getSuggestions(suggestedBy)
    }

    private fun setObserver() {
        viewModel.suggestionListResponseModel.observe(requireActivity()) { response ->
            when (response) {
                is Resource.Success -> {
                    ProgressDialog.hideProgressBar()
                    response.data?.let { dataList.addAll(it) }
                    setupRecylarview(response.data!!)
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

        viewModel.deleteSuggestionModel.observe(requireActivity()) { response ->
            when (response) {
                is Resource.Success -> {
                    ProgressDialog.hideProgressBar()
                    showToast("Your selected purchase suggestion deleted successfully")
                    viewModel.getSuggestions(suggestedBy)
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

    private fun setupRecylarview(dataList: List<SuggestionListResponseModel>) {
        adapter.updateContentList(dataList as ArrayList<SuggestionListResponseModel>)
        binding.rvSuggestion.adapter = adapter
    }

    private fun filter(text: String) {
        // creating a new array list to filter our data.
        var filterList: ArrayList<SuggestionListResponseModel> = ArrayList()
        if(text.isNotEmpty()) {
            filterList.clear()
            for (item in dataList) {
                if (item.title!!.toLowerCase().contains(text.toLowerCase())) {
                    filterList.add(item)
                }
            }
        }else filterList = dataList as ArrayList<SuggestionListResponseModel>

        if (filterList.isEmpty()) {
            adapter.updateContentList(filterList)
            //Toast.makeText(requireContext(), "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            adapter.updateContentList(filterList)
        }

        //Log.d("filter",Gson().toJson(filterList))
    }

}