package com.bbbdem.koha.module.dashboard.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bbbdem.koha.R
import com.bbbdem.koha.databinding.BorrowedListItemBinding
import com.bbbdem.koha.module.my_account.summary.model.CheckoutResponseModel
import com.bbbdem.koha.utils.Utils
import com.squareup.picasso.Picasso

class BookBorrowedListAdapter(
    private val itemList: ArrayList<CheckoutResponseModel>,
    private val booksListCallBack: BorrowedListCallBack
) : RecyclerView.Adapter<BookBorrowedListAdapter.BorrowedListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BorrowedListViewHolder {
        val binding =
            BorrowedListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return BorrowedListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BorrowedListViewHolder, position: Int) {
        val item = itemList[position]

        if(Utils.daysCount(item.checkoutDate!!)<0){
            holder.binding.tvStatus.text = "Submitted"
        }else{
            holder.binding.tvStatus.text = "${Utils.daysCount(item.checkoutDate!!)} days left"//Utils.changeDateFormat(item.checkoutDate!!)
        }

        holder.binding.root.setOnClickListener {
            booksListCallBack.onDetailClickListener(
                position,
                itemList[position]
            )

        }
        if(item.bookDetailModel?.items?.isNotEmpty()==true) {
            val imageUrl = item.bookDetailModel?.items?.get(0)?.volumeInfo?.imageLinks?.thumbnail
            if(imageUrl?.isNotEmpty() == true){
                Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.no_image_available)
                    .into(holder.binding.ivCover)
            }
        }

    }

    class BorrowedListViewHolder(
        val binding: BorrowedListItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

    interface BorrowedListCallBack{
        fun onDetailClickListener(adapterPosition: Int, checkoutResponseModel: CheckoutResponseModel)
    }
}