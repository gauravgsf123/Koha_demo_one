package com.bbbdem.koha.module.dashboard.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bbbdem.koha.R
import com.bbbdem.koha.app.BaseFragment
import com.bbbdem.koha.common.Constant
import com.bbbdem.koha.databinding.FragmentSearchListBinding
import com.bbbdem.koha.module.dashboard.DashboardActivity
import com.bbbdem.koha.module.dashboard.DashboardViewModel
import com.bbbdem.koha.module.dashboard.adapter.BookListAdapter
import com.bbbdem.koha.module.my_account.summary.model.BookDetailResponseModel
import com.bbbdem.koha.network.Resource
import com.bbbdem.koha.network.ViewModelFactoryClass
import com.bbbdem.koha.utils.ProgressDialog


class SearchListFragment : BaseFragment() {
    private lateinit var binding:FragmentSearchListBinding
    private lateinit var viewModel: DashboardViewModel
    private lateinit var adapter : BookListAdapter
    private var library: String? = null
    private var category: String? = null
    private var searchBy: String? = null
    private var search: String? = null
    private var dateRange: String? = null
    private var type: Int = 0
    private var perPage = 10
    private var pageIndex = 1
    private var totalList = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            library = it.getString(Constant.LIBRARY)
            category = it.getString(Constant.CATEGORY)
            searchBy = it.getString(Constant.SEARCH_BY)
            search = it.getString(Constant.SEARCH)
            type = it.getInt(Constant.FRAGMENT_TYPE)
            dateRange = it.getString(Constant.DATE_RANGE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_search_list,
            container,
            false
        )
        viewModel = ViewModelProvider(
            this,
            ViewModelFactoryClass(requireActivity().application)
        )[DashboardViewModel::class.java]
        callSearchApi(pageIndex)
        binding.ivNext.setOnClickListener(this)
        binding.ivPrevious.setOnClickListener(this)
        setObserver()
        return binding.root
    }

    private fun callSearchApi(pageIndex:Int){
        if (type == Constant.FragmentType.FILTER) {
            //val query = "{\"${searchBy}\": { \"-like\": \"%${search}%\" }}"
            search?.let { Log.d("query", it) }
            if(searchBy?.isNotEmpty()==true) {
                search?.let { viewModel.searchBookList(it, library, category,pageIndex,perPage) }
            }else {
                var query = ""
                if(!library.isNullOrEmpty() && !category.isNullOrEmpty()) {
                    query = "{\"home_library_id\":\"${library}\",\"item_type_id\":\"${category}\"}"
                }else if(!library.isNullOrEmpty() && category.isNullOrEmpty()){
                    query = "{\"home_library_id\":\"${library}\"}"
                }else if(library.isNullOrEmpty() && !category.isNullOrEmpty()){
                    query = "{\"item_type_id\":\"${category}\"}"
                }
                viewModel.searchBookItem(query,pageIndex,perPage)
            }
        }else if (type == Constant.FragmentType.ADVANCE_SEARCH) {
            //viewModel.searchBookList(search!!, library, category)
            if(searchBy?.isNotEmpty()==true) {
                search?.let { viewModel.searchBookList(it, library, category,pageIndex,perPage) }
            }else {
                var query = ""
                if(!library.isNullOrEmpty() && !category.isNullOrEmpty()) {
                    query = "{\"home_library_id\":\"${library}\",\"item_type_id\":\"${category}\"}"
                }else if(!library.isNullOrEmpty() && category.isNullOrEmpty()){
                    query = "{\"home_library_id\":\"${library}\"}"
                }else if(library.isNullOrEmpty() && !category.isNullOrEmpty()){
                    query = "{\"item_type_id\":\"${category}\"}"
                }
                viewModel.searchBookItem(query,pageIndex,perPage)
            }
        }else if (type == Constant.FragmentType.SEARCH) {
            viewModel.searchBookList(search!!, library, category,pageIndex,perPage)
        } else{
            //val query = "{\"${searchBy}\": { \"-like\": \"%${search}%\" }}"
            if(searchBy == "barcode") {
                search?.let { viewModel.searchBookItem(it, pageIndex, perPage) }
            }else search?.let { viewModel.searchBookList(it, library, category,pageIndex, perPage) }
        }
    }

    override fun onClick(p0: View?) {
        super.onClick(p0)
        when(p0?.id){
            binding.ivPrevious.id->callSearchApi(--pageIndex)
            binding.ivNext.id->callSearchApi(++pageIndex)
        }
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            SearchListFragment().apply {
                arguments = this.arguments
            }
    }

    private fun setObserver() {
        viewModel.searchBookList.observe(requireActivity()) { response ->
            when (response) {
                is Resource.Success -> {
                    ProgressDialog.hideProgressBar()
                    //booksList = response.data!!
                    setupRecylerView(response.data!!)
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
                    response?.message?.let { showToast(it) }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupRecylerView(data: List<BookDetailResponseModel>) {
        if(pageIndex==1) binding.ivPrevious.visibility = View.INVISIBLE
        else binding.ivPrevious.visibility = View.VISIBLE
        if(data.size<perPage)binding.ivNext.visibility = View.INVISIBLE
        else binding.ivNext.visibility = View.VISIBLE


        if(type == Constant.FragmentType.ADVANCE_SEARCH && dateRange?.length!! >1){
            val filterData = filterDateRange(data)
            binding.rvBookList.adapter = BookListAdapter(filterData as ArrayList<BookDetailResponseModel>,object :BookListAdapter.BooksListCallBack{
                override fun onDetailClickListener(adapterPosition: Int, item: BookDetailResponseModel) {
                    (activity as DashboardActivity).addFragment(BookDetailFragment(item))
                }
            })
            binding.tvTopResult.text = "Result ${(10*(pageIndex-1))+1}-${(((pageIndex-1)*10)+data.size)}"
        }else{
            binding.rvBookList.adapter = BookListAdapter(data as ArrayList<BookDetailResponseModel>,object :BookListAdapter.BooksListCallBack{
                override fun onDetailClickListener(adapterPosition: Int, item: BookDetailResponseModel) {
                    (activity as DashboardActivity).addFragment(BookDetailFragment(item))
                }
            })
            binding.tvTopResult.text = "Result ${(10*(pageIndex-1))+1}-${(((pageIndex-1)*10)+data.size)}"
        } //data as ArrayList<BookDetailResponseModel>
        //
        //val layoutManager = LinearLayoutManager(requireContext())
        /*adapter = BookListAdapter(data as ArrayList<BookDetailResponseModel>,object :BookListAdapter.BooksListCallBack{
            override fun onDetailClickListener(adapterPosition: Int, item: BookDetailResponseModel) {
                (activity as DashboardActivity).replaceFragment(BookDetailFragment(item))
            }

        })
        binding.rvBookList.layoutManager = layoutManager*/

        //binding.rvBookList.scrollToPosition(data.size - 12)
    }

    private fun filterDateRange(data: List<BookDetailResponseModel>) : ArrayList<BookDetailResponseModel>{
        val filterData = ArrayList<BookDetailResponseModel>()
        //val dateRange = arguments?.getString(Constant.DATE_RANGE)
        val split = dateRange?.split("-")
        val dateFrom = split?.get(0)
        val dateTo = split?.get(1)
        data.forEachIndexed { index, bookDetailResponseModel ->
            Log.d("filterDateRange","$dateFrom $dateTo ${bookDetailResponseModel.publicationYear}")
            if(!bookDetailResponseModel.publicationYear.isNullOrBlank()) {
                if (dateTo?.toInt()?.let {
                        dateFrom?.toInt()
                            ?.rangeTo(it)
                    }?.contains(bookDetailResponseModel.publicationYear?.toInt()!!) == true) {
                    filterData.add(bookDetailResponseModel)
                }
            }else filterData.add(bookDetailResponseModel)
        }
        return filterData
    }
}