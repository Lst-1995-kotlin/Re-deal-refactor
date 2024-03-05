package com.hifi.redeal.transaction.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hifi.redeal.transaction.model.TradeData
import javax.inject.Inject

class TradeAdapterDiffCallback @Inject constructor() : DiffUtil.ItemCallback<TradeData>() {

    override fun areItemsTheSame(oldItem: TradeData, newItem: TradeData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TradeData, newItem: TradeData): Boolean {
        return oldItem == newItem
    }

}