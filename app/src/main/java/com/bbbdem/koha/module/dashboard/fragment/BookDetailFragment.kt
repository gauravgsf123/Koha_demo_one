package com.bbbdem.koha.module.dashboard.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bbbdem.koha.R
import com.bbbdem.koha.app.BaseFragment
import com.bbbdem.koha.common.Constant
import com.bbbdem.koha.databinding.FragmentBookDetailBinding
import com.bbbdem.koha.module.dashboard.DashboardActivity
import com.bbbdem.koha.module.dashboard.DashboardViewModel
import com.bbbdem.koha.module.dashboard.adapter.BookItemListAdapter
import com.bbbdem.koha.module.dashboard.bottomsheet.PlaceHoldsFragment
import com.bbbdem.koha.module.dashboard.model.ItemListOfBookResponseModel
import com.bbbdem.koha.module.my_account.summary.model.BookDetailResponseModel
import com.bbbdem.koha.network.Resource
import com.bbbdem.koha.network.ViewModelFactoryClass
import com.bbbdem.koha.utils.ProgressDialog
import com.squareup.picasso.Picasso


class BookDetailFragment(var bookDetailResponseModel: BookDetailResponseModel) : BaseFragment() {
    private lateinit var binding:FragmentBookDetailBinding
    private lateinit var viewModel:DashboardViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_book_detail,
            container,
            false
        )
        viewModel = ViewModelProvider(
            this,
            ViewModelFactoryClass(requireActivity().application)
        )[DashboardViewModel::class.java]
        viewModel.getItemListForBook(bookDetailResponseModel.biblioId,1,150)
        (activity as DashboardActivity).run {
            showToolbar(true)
            bookDetailResponseModel.title?.let { setAppTitle(it) }
        }
        setupData()
        setObserver()
        if(sharedPreference.getValueInt(Constant.HOLD)==0) binding.btnPlaceHold.visibility = View.INVISIBLE
        else binding.btnPlaceHold.visibility = View.VISIBLE
        binding.btnPlaceHold.setOnClickListener {
            PlaceHoldsFragment(bookDetailResponseModel.biblioId!!).show(requireFragmentManager(),"PlaceHoldsFragment")
        }
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setupData() {
        binding.tvTitle.text = bookDetailResponseModel.title
        binding.tvBy.text = "By : ${bookDetailResponseModel.author}"
        binding.tvBookISBN.text = bookDetailResponseModel.isbn
        binding.tvBookPublicationDetail.text = "${bookDetailResponseModel.publicationPlace.toString()} : ${bookDetailResponseModel.publisher.toString()}, ${bookDetailResponseModel.copyrightDate.toString()}"
        binding.tvBookEdition.text = bookDetailResponseModel.publicationYear
        binding.tvBookNumberOfPage.text = bookDetailResponseModel.pages
        binding.tvBookClassification.text = bookDetailResponseModel.cnClass

        if(bookDetailResponseModel.bookDetailModel?.items?.isNotEmpty() == true) {
            val imageUrl = bookDetailResponseModel.bookDetailModel?.items?.get(0)?.volumeInfo?.imageLinks?.thumbnail
            if(!imageUrl.isNullOrEmpty()) {
                Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.no_image_available)
                    .into(binding.ivBookCover)
            }

        }
    }

    private fun setObserver() {
        viewModel.itemListOfBookResponseModel.observe(requireActivity()) { response ->
            when (response) {
                is Resource.Success -> {
                    ProgressDialog.hideProgressBar()
                    setupRecylerview(response.data!!)
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
    private fun setupRecylerview(data: List<ItemListOfBookResponseModel>) {
        binding.tvStatus.text = "Status: Available(${getAvailableCount(data)}), Issued(${getIssuedCount(data)})"
        binding.rvBookItems.adapter = BookItemListAdapter(data as ArrayList<ItemListOfBookResponseModel>)
    }

    private fun getAvailableCount(data: List<ItemListOfBookResponseModel>):Int{
        var countMinus = 0
        data.forEachIndexed { index, item ->
            if(item.itemId==item.checkoutResponseModel?.itemId){
                countMinus++
            }
            if(item.itemDetailResponseModel?.lostStatus!! >1){
                countMinus++
            }
            if(item.itemDetailResponseModel?.damagedStatus!! >1){
                countMinus++
            }
            if(item.itemDetailResponseModel?.withdrawn!! >1){
                countMinus++
            }
            if(item.itemDetailResponseModel?.notForLoanStatus!! >1){
                countMinus++
            }
        }
        return (data.size-countMinus)
    }

    private fun getIssuedCount(data: List<ItemListOfBookResponseModel>):Int{
        var issuedCount = 0
        data.forEachIndexed { index, item ->
            if(item.itemId==item.checkoutResponseModel?.itemId){
                issuedCount++
            }

        }
        return issuedCount
    }

}