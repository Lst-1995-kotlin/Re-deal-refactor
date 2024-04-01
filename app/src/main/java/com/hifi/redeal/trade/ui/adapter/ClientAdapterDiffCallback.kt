package com.hifi.redeal.trade.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hifi.redeal.trade.data.model.TradeClientData
import javax.inject.Inject

class ClientAdapterDiffCallback @Inject constructor(): DiffUtil.ItemCallback<TradeClientData>() {
    override fun areItemsTheSame(oldItem: TradeClientData, newItem: TradeClientData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TradeClientData, newItem: TradeClientData): Boolean {
        return oldItem == newItem
    }

}