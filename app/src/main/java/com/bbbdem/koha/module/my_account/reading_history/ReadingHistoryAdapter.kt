package com.bbbdem.koha.module.my_account.reading_history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bbbdem.koha.R
import com.bbbdem.koha.databinding.ItemCheckoutListBinding
import com.bbbdem.koha.module.my_account.summary.model.CheckoutResponseModel
import com.bbbdem.koha.utils.Utils
import com.squareup.picasso.Picasso

class ReadingHistoryAdapter (
    private val itemList: ArrayList<CheckoutResponseModel>,
    private val readingHistoryCallBack: ReadingHistoryCallBack
) : RecyclerView.Adapter<ReadingHistoryAdapter.SummaryDetailViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummaryDetailViewHolder {
        val binding =
            ItemCheckoutListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return SummaryDetailViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: SummaryDetailViewHolder, position: Int) {
        val item = itemList[position]
        holder.binding.tvBookName.text = item.bookDetailResponseModel?.title
        holder.binding.tvBookAuthor.text = item.bookDetailResponseModel?.author
        holder.binding.tvBookDueDate.text = item.checkinDate?.let { Utils.changeDateFormat(it) }
        holder.binding.tvBookDueDateTitle.text = holder.itemView.context.resources.getString(R.string.date)
        if(item.bookDetailModel?.items?.isNotEmpty() == true) {
            val imageUrl = item.bookDetailModel?.items?.get(0)?.volumeInfo?.imageLinks?.thumbnail
            if(imageUrl?.isNotEmpty() == true) {
                Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.no_image_available)
                    .into(holder.binding.ivBook)
            }
        }
        holder.binding.tvRenew.visibility = View.INVISIBLE
        holder.binding.tvBookFine.visibility = View.INVISIBLE
        holder.binding.tvBookFineTitle.visibility = View.INVISIBLE
        holder.binding.tvBookName.setOnClickListener {
            readingHistoryCallBack.onTitleClickListener(position,item)
        }

    }

    class SummaryDetailViewHolder(
        val binding: ItemCheckoutListBinding
    ) : RecyclerView.ViewHolder(binding.root)

    fun updateContentList(contentList: ArrayList<CheckoutResponseModel>,isfilter:Boolean=false) {
        /*//if(page == 0)
        itemList.clear()
        itemList.addAll(contentList)
        notifyDataSetChanged()*/

        val position: Int = itemList.size + 1
        if(isfilter){
            itemList.clear()
            itemList.addAll(contentList)
        }else itemList.addAll(contentList)

        notifyDataSetChanged()
    }

    interface ReadingHistoryCallBack{
        fun onTitleClickListener(adapterPosition: Int, checkoutResponseModel: CheckoutResponseModel)
    }

}


