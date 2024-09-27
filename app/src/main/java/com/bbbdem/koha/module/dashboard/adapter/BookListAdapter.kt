package com.bbbdem.koha.module.dashboard.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bbbdem.koha.R
import com.bbbdem.koha.databinding.ItemBookListBinding
import com.bbbdem.koha.module.dashboard.model.ItemListOfBookResponseModel
import com.bbbdem.koha.module.my_account.summary.model.BookDetailResponseModel
import com.squareup.picasso.Picasso

class BookListAdapter (
    private var itemList: ArrayList<BookDetailResponseModel>,
    private val booksListCallBack: BooksListCallBack
) : RecyclerView.Adapter<BookListAdapter.BookListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookListViewHolder {
        val binding =
            ItemBookListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return BookListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BookListViewHolder, position: Int) {
        val item = itemList[position]
        holder.binding.tvBookName.text = item.title
        holder.binding.tvBookAuthor.text = item.author
        holder.binding.tvBookPublication.text = "${item.publicationPlace.toString()} : ${item.publisher.toString()}, ${item.copyrightDate.toString()}"
        /*if(!item.itemListOfBookResponseModel.isNullOrEmpty()) {
            holder.binding.tvBookStatus.text = "Available(${
                item.itemListOfBookResponseModel?.let {
                    getAvailableCount(it)
                }
            }), Issued(${item.itemListOfBookResponseModel?.let { 
                getIssuedCount(it)
            }})"
        }*/
        if(item.bookDetailModel?.items?.isNotEmpty() == true) {
            val imageUrl = item.bookDetailModel?.items?.get(0)?.volumeInfo?.imageLinks?.thumbnail
            if(imageUrl?.isNotEmpty() == true){
                Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.no_image_available)
                    .into(holder.binding.ivBook)
            }
        }
        holder.binding.root.setOnClickListener {
            booksListCallBack.onDetailClickListener(
                position,item
            )

        }

    }

    class BookListViewHolder(
        val binding: ItemBookListBinding
    ) : RecyclerView.ViewHolder(binding.root)

    fun updateList(list: ArrayList<BookDetailResponseModel>){
        val position: Int = itemList.size + 1
        itemList.addAll(list)
        //notifyItemInserted(itemList.size);

    }

    interface BooksListCallBack{
        fun onDetailClickListener(adapterPosition: Int, item: BookDetailResponseModel)
    }

    fun getAvailableCount(data: List<ItemListOfBookResponseModel>):Int{
        var countMinus = 0
        data.forEachIndexed { index, item ->
            if(item.itemId==item.checkoutResponseModel?.itemId){
                countMinus++
            }
            if(item.itemDetailResponseModel!=null) {
                if (item.itemDetailResponseModel?.lostStatus!! > 1) {
                    countMinus++
                }
                if (item.itemDetailResponseModel?.damagedStatus!! > 1) {
                    countMinus++
                }
                if (item.itemDetailResponseModel?.withdrawn!! > 1) {
                    countMinus++
                }
                if (item.itemDetailResponseModel?.notForLoanStatus!! > 1) {
                    countMinus++
                }
            }
        }
        return (data.size-countMinus)
    }

    fun getIssuedCount(data: List<ItemListOfBookResponseModel>):Int{
        var issuedCount = 0
        data.forEachIndexed { index, item ->
            if(item.itemId==item.checkoutResponseModel?.itemId){
                issuedCount++
            }

        }
        return issuedCount
    }
}


