package com.bbbdem.koha.module.dashboard.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bbbdem.koha.R
import com.bbbdem.koha.databinding.ArrivalsListItemBinding

class ArrivalsAdpter(var list:ArrayList<MovieResponse>):RecyclerView.Adapter<ArrivalsAdpter.MyViewHolder>() {
    class MyViewHolder(var binding: ArrivalsListItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var binding = DataBindingUtil.inflate<ArrivalsListItemBinding>(LayoutInflater.from(parent.context),
            R.layout.arrivals_list_item,parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var item = list[position]
        holder.itemView.context
        holder.binding.apply {
            tvTitle.text = item.Title
        }
    }

    override fun getItemCount() = list.size

}