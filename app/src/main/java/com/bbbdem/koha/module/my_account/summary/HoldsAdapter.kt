package com.bbbdem.koha.module.my_account.summary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bbbdem.koha.R
import com.bbbdem.koha.databinding.ItemHoldsListBinding
import com.bbbdem.koha.module.my_account.summary.model.HoldsResponseModel
import com.squareup.picasso.Picasso

class HoldsAdapter (
    private val itemList: ArrayList<HoldsResponseModel>,
    private val holdsDetailCallBack: HoldsDetailCallBack
) : RecyclerView.Adapter<HoldsAdapter.SummaryDetailViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummaryDetailViewHolder {
        val binding =
            ItemHoldsListBinding.inflate(
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
        //holder.binding.tvCancel.text = item.cancellationDate?.let { Utils.changeDateFormat(it) }
        //holder.binding.tvStatus.text = item.holdDate?.let { Utils.changeDateFormat(it) }
        //if(item.renewalResponseModel?.allowsRenewal==true){
            holder.binding.tvCancel.visibility = View.VISIBLE
            holder.binding.tvCancel.setOnClickListener {
                //if(item.renewalResponseModel?.currentRenewals!! < item.renewalResponseModel?.maxRenewals!!){
                holdsDetailCallBack.onCancelClickListener(
                        position,
                        itemList[position]
                    )
                //}else Toast.makeText(holder.itemView.context,"You have reached your maximum renewal",Toast.LENGTH_LONG).show()

            }
        //}else holder.binding.tvRenew.visibility = View.INVISIBLE
        if(item.bookDetailModel?.items?.isNotEmpty() == true) {
            val imageUrl = item.bookDetailModel?.items?.get(0)?.volumeInfo?.imageLinks?.thumbnail
            Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.no_image_available)
                .into(holder.binding.ivBook)
        }
        var status = ""
        if(item.status == null){
            if(item.suspended==false) status = "Pending"
            else status = "Suspended"
        }else if(item.status.equals("W",true)){
            status = "Waiting since ${item.waitingDate.toString()}"
        }
        holder.binding.tvStatus.text = status

        holder.binding.tvBookName.setOnClickListener {
            holdsDetailCallBack.onTitleClickListener(position,item)
        }

    }

    class SummaryDetailViewHolder(
        val binding: ItemHoldsListBinding
    ) : RecyclerView.ViewHolder(binding.root)

    interface HoldsDetailCallBack{
        fun onCancelClickListener(adapterPosition: Int, holdsResponseModel: HoldsResponseModel)
        fun onTitleClickListener(adapterPosition: Int, holdsResponseModel: HoldsResponseModel)
    }
}


