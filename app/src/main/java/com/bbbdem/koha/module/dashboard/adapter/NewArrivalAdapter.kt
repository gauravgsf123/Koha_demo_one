package com.bbbdem.koha.module.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bbbdem.koha.R
import com.bbbdem.koha.databinding.ItemArrivalBookListBinding
import com.bbbdem.koha.module.my_account.summary.model.BookDetailResponseModel
import com.squareup.picasso.Picasso

class NewArrivalAdapter(
    private val itemList: List<BookDetailResponseModel>,
    private val booksListCallBack: BookDetailCallBack
) : RecyclerView.Adapter<NewArrivalAdapter.NewArrivalListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewArrivalListViewHolder {
        val binding =
            ItemArrivalBookListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return NewArrivalListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: NewArrivalListViewHolder, position: Int) {
        val item = itemList[position]
        holder.binding.tvTitle.text = item?.title
        holder.binding.tvWriterName.text = item?.author
        holder.binding.root.setOnClickListener {
            booksListCallBack.onDetailClickListener(
                    position,item
                )

        }
        if(item.bookDetailModel?.items?.isNotEmpty() == true) {
            val imageUrl = item.bookDetailModel?.items?.get(0)?.volumeInfo?.imageLinks?.thumbnail
            if(imageUrl?.isNotEmpty() == true) {
                Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.no_image_available)
                    .into(holder.binding.ivBookCover)
            }
        }

    }

    class NewArrivalListViewHolder(
        val binding: ItemArrivalBookListBinding
    ) : RecyclerView.ViewHolder(binding.root)

    interface BookDetailCallBack{
        fun onDetailClickListener(adapterPosition: Int,bookDetailResponseModel:BookDetailResponseModel)
    }
}