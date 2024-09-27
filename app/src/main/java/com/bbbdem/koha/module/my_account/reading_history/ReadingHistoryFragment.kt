package com.bbbdem.koha.module.my_account.reading_history

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bbbdem.koha.R
import com.bbbdem.koha.app.BaseFragment
import com.bbbdem.koha.common.Constant
import com.bbbdem.koha.databinding.FragmentReadingHistoryBinding
import com.bbbdem.koha.module.dashboard.DashboardActivity
import com.bbbdem.koha.module.dashboard.fragment.BookDetailFragment
import com.bbbdem.koha.module.my_account.summary.model.CheckoutResponseModel
import com.bbbdem.koha.network.Resource
import com.bbbdem.koha.network.ViewModelFactoryClass
import com.bbbdem.koha.utils.EndlessRecyclerViewScrollListener
import com.bbbdem.koha.utils.ProgressDialog
import java.util.*
import kotlin.collections.ArrayList


class ReadingHistoryFragment : BaseFragment() {
    private lateinit var binding:FragmentReadingHistoryBinding
    private lateinit var viewModel:ReadingHistoryViewModel
    private lateinit var adapter: ReadingHistoryAdapter
    private var dataList: MutableList<CheckoutResponseModel> = mutableListOf()
    private var totalItemsCount = 0
    private var listSize = 0
    private var page = 0
    private var pageIndex = 1
    private var perPage = 5

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
            R.layout.fragment_reading_history,
            container,
            false
        )
        (activity as DashboardActivity).run {
            showReadingHistoryToolbar(true)
        }
        binding.tooldbar.binding.rlBack.setOnClickListener {
            (activity as DashboardActivity).onBackPressed()
        }
        binding.tooldbar.binding.tvTitle.text = resources.getString(R.string.your_checkout_history)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactoryClass(requireActivity().application)
        )[ReadingHistoryViewModel::class.java]
        adapter = ReadingHistoryAdapter(arrayListOf(),object :ReadingHistoryAdapter.ReadingHistoryCallBack{
            override fun onTitleClickListener(
                adapterPosition: Int,
                checkoutResponseModel: CheckoutResponseModel
            ) {
                (activity as DashboardActivity).addFragment(
                    BookDetailFragment(checkoutResponseModel?.bookDetailResponseModel!!)
                )
            }

        })
        viewModel.getCheckoutHistory(sharedPreference.getValueInt(Constant.PATRON_ID).toString(),pageIndex,perPage)
        setObserver()
        binding.tooldbar.binding.editTextSearch.doAfterTextChanged{
            filter(it.toString())
        }
        return binding.root
    }

    private fun setObserver() {
        viewModel.checkoutResponseModel.observe(requireActivity()) { response ->
            when (response) {
                is Resource.Success -> {
                    ProgressDialog.hideProgressBar()
                    //dataList = response.data!!
                    //setupRecylerView(response.data,response.message!!.toInt())
                    totalItemsCount = response.message?.toInt() ?: 0

                    response.data?.let { dataList.addAll(it) }
                    listSize += perPage
                    response.message?.let { setupRecylerView(dataList, it.toInt()) }
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

    @SuppressLint("SetTextI18n")
    private fun setupRecylerView(data: List<CheckoutResponseModel>?, size: Int) {
        binding.tvCheckedOut.text = "Checked out (${data?.size})"
        val layoutManager = LinearLayoutManager(requireContext())
        adapter.updateContentList(dataList as ArrayList<CheckoutResponseModel>)
        binding.rvSummary.layoutManager = layoutManager
        binding.rvSummary.addOnScrollListener(object :
            EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if(listSize> data!!.size){

                }else{
                    //listSize = totalItemsCount
                    viewModel.getCheckoutHistory(sharedPreference.getValueInt(Constant.PATRON_ID).toString(),++pageIndex,perPage)
                }

            }

        })
        binding.rvSummary.adapter = adapter
        binding.rvSummary.scrollToPosition(dataList.size - (perPage+5))

    }

    private fun filter(text: String) {
        // creating a new array list to filter our data.
        var filterList: ArrayList<CheckoutResponseModel> = ArrayList<CheckoutResponseModel>()
        if(text.isNotEmpty()) {
            filterList.clear()
            for (item in dataList) {
                if (item.bookDetailResponseModel?.title!!.toLowerCase().contains(text.toLowerCase())) {
                    filterList.add(item)
                }
            }
        }else filterList = dataList as ArrayList<CheckoutResponseModel>
        binding.tvCheckedOut.text = "Checked out (${filterList.size})"
        if (filterList.isEmpty()) {
            adapter.updateContentList(filterList ,true)
            Toast.makeText(requireContext(), "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            adapter.updateContentList(filterList,true)
        }
    }

}