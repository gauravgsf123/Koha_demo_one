package com.bbbdem.koha.module.dashboard.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bbbdem.koha.R
import com.bbbdem.koha.app.BaseFragment
import com.bbbdem.koha.common.Constant
import com.bbbdem.koha.databinding.FragmentHomeBinding
import com.bbbdem.koha.module.dashboard.DashboardActivity
import com.bbbdem.koha.module.dashboard.DashboardViewModel
import com.bbbdem.koha.module.dashboard.adapter.BookBorrowedListAdapter
import com.bbbdem.koha.module.dashboard.adapter.CirculatingBooksAdapter
import com.bbbdem.koha.module.dashboard.adapter.NewArrivalAdapter
import com.bbbdem.koha.module.dashboard.model.CirculatingBooksResponseModel
import com.bbbdem.koha.module.my_account.summary.model.BookDetailResponseModel
import com.bbbdem.koha.module.my_account.summary.model.CheckoutResponseModel
import com.bbbdem.koha.network.Resource
import com.bbbdem.koha.network.ViewModelFactoryClass
import com.bbbdem.koha.utils.ProgressDialog
import com.google.gson.Gson

class HomeFragment : BaseFragment() {
    private lateinit var binding:FragmentHomeBinding
    private lateinit var adapter:ArrivalsAdpter
    private lateinit var viewModel:DashboardViewModel
    private var booksList: ArrayList<BookDetailResponseModel> = ArrayList()
    private var circulatingBooksList: ArrayList<CirculatingBooksResponseModel> = ArrayList()

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
            R.layout.fragment_home,
            container,
            false
        )
        viewModel = ViewModelProvider(
            this,
            ViewModelFactoryClass(requireActivity().application)
        )[DashboardViewModel::class.java]
        (activity as DashboardActivity).run {
            showToolbar(false)
            updateStatusBarColor("#009A90")
        }
        binding.tvViewAllNewArrival.setOnClickListener(this)
        binding.tvViewAllTopCirculatingBook.setOnClickListener(this)
        binding.tvViewAllTBorrowedBook.setOnClickListener(this)

        setObserver()
        if(sharedPreference.getValueString(Constant.NEW_ARRIVALS)?.isNotEmpty() == true){
            binding.tvBookCount.text = sharedPreference.getValueString(Constant.TOTAL_BOOK)
            binding.tvUsersCount.text = sharedPreference.getValueString(Constant.TOTAL_PATRON)
            val newArrivalData = Gson().fromJson(sharedPreference.getValueString(Constant.NEW_ARRIVALS), Array<BookDetailResponseModel>::class.java)
            val bookDetailResponseModel :MutableList<BookDetailResponseModel> = mutableListOf()
            bookDetailResponseModel.addAll(newArrivalData)
            setupNewArrivalRecylerView(bookDetailResponseModel as ArrayList<BookDetailResponseModel>)

            if(sharedPreference.getValueString(Constant.TOP_CIRCULATING)?.isNotEmpty() == true){
                val circulatingData = Gson().fromJson(sharedPreference.getValueString(Constant.TOP_CIRCULATING), Array<CirculatingBooksResponseModel>::class.java)
                val circulatingBooksResponseModel :MutableList<CirculatingBooksResponseModel> = mutableListOf()
                circulatingBooksResponseModel.addAll(circulatingData)
                setupCirculatingRecylerView(circulatingBooksResponseModel as ArrayList<CirculatingBooksResponseModel>)
            }
            if (sharedPreference.getValueInt(Constant.PATRON_ID)!=0) {
                binding.tvBorrowedBook.visibility = View.VISIBLE
                binding.tvViewAllTBorrowedBook.visibility = View.VISIBLE
                if (sharedPreference.getValueString(Constant.RECENTLY_BORROWED)
                        ?.isNotEmpty() == true
                ) {
                    val recentlyBorrowedData = Gson().fromJson(
                        sharedPreference.getValueString(Constant.RECENTLY_BORROWED),
                        Array<CheckoutResponseModel>::class.java
                    )
                    val checkoutResponseModel: MutableList<CheckoutResponseModel> = mutableListOf()
                    checkoutResponseModel.addAll(recentlyBorrowedData)
                    setupBorrowedRecylerView(checkoutResponseModel as ArrayList<CheckoutResponseModel>)
                }
            }else{
                binding.tvBorrowedBook.visibility = View.GONE
                binding.tvViewAllTBorrowedBook.visibility = View.GONE
            }

        }else{
            viewModel.getBookList("-biblio_id",1,10)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            // on below line we are setting is refreshing to false.
            binding.swipeRefreshLayout.isRefreshing = false
            viewModel.getBookList("-biblio_id",1,10)

        }


        return binding.root
    }

    private fun setObserver() {
        viewModel.booksListResponseModel.observe(requireActivity()) { response ->
            when (response) {
                is Resource.Success -> {
                    ProgressDialog.hideProgressBar()
                    sharedPreference.save(Constant.NEW_ARRIVALS, Gson().toJson(response.data))
                    booksList = response.data!! as ArrayList<BookDetailResponseModel>
                    viewModel.getCirculatingBookList("-checkouts_count",1,10)
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

        viewModel.circulatingBooksResponseModel.observe(requireActivity()) { response ->
            when (response) {
                is Resource.Success -> {
                    ProgressDialog.hideProgressBar()
                    binding.tvBookCount.text = response.message
                    sharedPreference.save(Constant.TOP_CIRCULATING, Gson().toJson(response.data))
                    sharedPreference.save(Constant.TOTAL_BOOK, response.message!!)
                    circulatingBooksList = response.data!! as ArrayList<CirculatingBooksResponseModel>
                    if (sharedPreference.getValueInt(Constant.PATRON_ID)!=0) {
                        binding.tvBorrowedBook.visibility = View.VISIBLE
                        binding.tvViewAllTBorrowedBook.visibility = View.VISIBLE
                        viewModel.getBorrowedBook(
                            sharedPreference.getValueInt(Constant.PATRON_ID),
                            "-checkout_id",
                            true,
                            1,
                            10
                        )
                    }else{
                        binding.tvBorrowedBook.visibility = View.GONE
                        binding.tvViewAllTBorrowedBook.visibility = View.GONE
                        setupCirculatingRecylerView(circulatingBooksList)
                        setupNewArrivalRecylerView(booksList)
                        viewModel.getAllPatrons()
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

        viewModel.checkoutResponseModel.observe(requireActivity()) { response ->
            when (response) {
                is Resource.Success -> {
                    ProgressDialog.hideProgressBar()
                    sharedPreference.save(Constant.RECENTLY_BORROWED, Gson().toJson(response.data))
                    setupCirculatingRecylerView(circulatingBooksList)
                    setupNewArrivalRecylerView(booksList)
                    setupBorrowedRecylerView(response.data!! as ArrayList<CheckoutResponseModel>)
                    viewModel.getAllPatrons()
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

        viewModel.totalPatrons.observe(requireActivity()) { response ->
            when (response) {
                is Resource.Success -> {
                    ProgressDialog.hideProgressBar()
                    binding.tvUsersCount.text = response.message
                    sharedPreference.save(Constant.TOTAL_PATRON, response.message!!)
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

    private fun setupBorrowedRecylerView(data: ArrayList<CheckoutResponseModel>) {
        binding.rvBorrowedBook.adapter = BookBorrowedListAdapter(data as ArrayList<CheckoutResponseModel>,object :BookBorrowedListAdapter.BorrowedListCallBack{
            override fun onDetailClickListener(
                position: Int,
                checkoutResponseModel: CheckoutResponseModel
            ) {

            }

        })
    }

    private fun setupCirculatingRecylerView(data: ArrayList<CirculatingBooksResponseModel>) {
        binding.rvPickPopular.adapter = CirculatingBooksAdapter(data,object :CirculatingBooksAdapter.BookDetailCallBack{
            override fun onDetailClickListener(
                position: Int,bookDetailResponseModel:BookDetailResponseModel
            ) {
                (activity as DashboardActivity).replaceFragment(BookDetailFragment(bookDetailResponseModel))
            }

        })
    }

    private fun setupNewArrivalRecylerView(data: ArrayList<BookDetailResponseModel>) {
        binding.rvArrivals.adapter = NewArrivalAdapter(data,object :NewArrivalAdapter.BookDetailCallBack{
            override fun onDetailClickListener(
                position: Int,bookDetailResponseModel:BookDetailResponseModel
            ) {
                (activity as DashboardActivity).replaceFragment(BookDetailFragment(bookDetailResponseModel))
            }

        })
    }


    override fun onClick(view: View?) {
        binding.run {
            when(view?.id){
                tvViewAllNewArrival.id->{(activity as DashboardActivity).replaceFragment(BookListFragment(Constant.BookListType.NEW_ARRIVAL))}
                tvViewAllTopCirculatingBook.id->{(activity as DashboardActivity).replaceFragment(BookListFragment(
                    Constant.BookListType.NEW_ARRIVAL
                )
                )}
                tvViewAllTBorrowedBook.id->{(activity as DashboardActivity).replaceFragment(BookListFragment(
                    Constant.BookListType.NEW_ARRIVAL
                )
                )}
            }
        }

    }
}