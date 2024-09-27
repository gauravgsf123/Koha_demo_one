package com.bbbdem.koha.module.my_account.summary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bbbdem.koha.R
import com.bbbdem.koha.common.Constant
import com.bbbdem.koha.common.SharedPreference
import com.bbbdem.koha.databinding.ItemCheckoutListBinding
import com.bbbdem.koha.module.my_account.summary.model.CheckoutResponseModel
import com.bbbdem.koha.utils.Utils
import com.squareup.picasso.Picasso

class CheckoutAdapter (
    private val itemList: ArrayList<CheckoutResponseModel>,
    private val isDue: Boolean,
    private val summaryDetailCallBack: SummaryDetailCallBack
) : RecyclerView.Adapter<CheckoutAdapter.SummaryDetailViewHolder>() {
    lateinit var sharedPreference :SharedPreference
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummaryDetailViewHolder {
        val binding =
            ItemCheckoutListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        sharedPreference = SharedPreference(parent.context)
        return SummaryDetailViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: SummaryDetailViewHolder, position: Int) {
        val item = itemList[position]
        holder.binding.tvBookName.text = item.bookDetailResponseModel?.title
        holder.binding.tvBookAuthor.text = item.bookDetailResponseModel?.author
        holder.binding.tvBookDueDate.text = item.dueDate?.let { Utils.changeDateFormat(it) }
        if(item.amountOutstanding!=null) {
            holder.binding.tvBookFine.text = "Overdue, Rs. ${item.amountOutstanding.toString()}"
        }else holder.binding.tvBookFine.text = "No due"
        if(item.renewalResponseModel?.allowsRenewal==true){
            holder.binding.tvRenew.visibility = View.VISIBLE
            holder.binding.tvRenew.setOnClickListener {
                if(item.renewalResponseModel?.currentRenewals!! < item.renewalResponseModel?.maxRenewals!!){
                    summaryDetailCallBack.onRenewClickListener(
                        position,
                        itemList[position]
                    )
                }else Toast.makeText(holder.itemView.context,"You have reached your maximum renewal",Toast.LENGTH_LONG).show()

            }
        }else holder.binding.tvRenew.visibility = View.INVISIBLE
        if(item.bookDetailModel?.items?.isNullOrEmpty() == false) {
            val imageUrl = item.bookDetailModel?.items?.get(0)?.volumeInfo?.imageLinks?.thumbnail
            if(!imageUrl.isNullOrEmpty()) {
                Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.no_image_available)
                    .into(holder.binding.ivBook)
            }
        }
        if(isDue){
            if(Utils.checkDate(item.dueDate!!)){
                holder.binding.tvBookDueDate.setTextColor(holder.itemView.context.resources.getColor(R.color.text_color_secondary))
                holder.binding.tvBookDueDateTitle.setTextColor(holder.itemView.context.resources.getColor(R.color.text_color_secondary))
            }else {
                holder.binding.tvBookDueDate.setTextColor(
                    holder.itemView.context.resources.getColor(
                        R.color.text_item_list_warning
                    )
                )
                holder.binding.tvBookDueDateTitle.setTextColor(
                    holder.itemView.context.resources.getColor(
                        R.color.text_item_list_warning
                    )
                )
            }
        }else{
            holder.binding.tvBookDueDate.setTextColor(holder.itemView.context.resources.getColor(R.color.text_item_list_warning))
            holder.binding.tvBookDueDateTitle.setTextColor(holder.itemView.context.resources.getColor(R.color.text_item_list_warning))
        }

        holder.binding.tvBookName.setOnClickListener {
            summaryDetailCallBack.onTitleClickListener(position,item)
        }

        if(sharedPreference.getValueInt(Constant.RENEW)==0) holder.binding.tvRenew.visibility = View.INVISIBLE
        else holder.binding.tvRenew.visibility = View.VISIBLE

    }

    class SummaryDetailViewHolder(
        val binding: ItemCheckoutListBinding
    ) : RecyclerView.ViewHolder(binding.root)

    interface SummaryDetailCallBack{
        fun onRenewClickListener(adapterPosition: Int, checkoutResponseModel: CheckoutResponseModel)
        fun onTitleClickListener(adapterPosition: Int, checkoutResponseModel: CheckoutResponseModel)
    }
}


