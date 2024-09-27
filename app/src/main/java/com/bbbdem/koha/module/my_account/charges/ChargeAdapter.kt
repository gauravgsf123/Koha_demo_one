package com.bbbdem.koha.module.my_account.charges

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bbbdem.koha.databinding.ItemChargesListBinding
import com.bbbdem.koha.utils.Utils

class ChargeAdapter (
    private val itemList: ArrayList<ChargesResponseModel>
) : RecyclerView.Adapter<ChargeAdapter.SummaryDetailViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummaryDetailViewHolder {
        val binding =
            ItemChargesListBinding.inflate(
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
        holder.binding.tvDate.text = item.date?.let{Utils.changeDateFormat(it)}
        holder.binding.tvDescription.text = item.description
        holder.binding.tvAmount.text = item.amountOutstanding.toString()
    }

    class SummaryDetailViewHolder(
        val binding: ItemChargesListBinding
    ) : RecyclerView.ViewHolder(binding.root)
}


