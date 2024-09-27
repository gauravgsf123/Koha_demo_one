package com.bbbdem.koha.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bbbdem.koha.R
import com.bbbdem.koha.databinding.ItemPickupScanListBinding

class TripSheetAdapter(var list:ArrayList<TripSheetResponse.TripSheetDocketDetail>): RecyclerView.Adapter<TripSheetAdapter.MyViewHolder>(){
    class MyViewHolder(var binding:ItemPickupScanListBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var binding = DataBindingUtil.inflate<ItemPickupScanListBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_pickup_scan_list,parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var item = list[position]
        holder.binding.apply {
            tvCartonNo.text = item.BarCodeNo
            tvCNoteNumber.text = item.CNoteNo
        }
    }

    override fun getItemCount()=list.size
}