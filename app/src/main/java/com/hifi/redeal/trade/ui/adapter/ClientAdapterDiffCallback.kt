package com.hifi.redeal.trade.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hifi.redeal.trade.data.model.ClientData
import javax.inject.Inject

class ClientAdapterDiffCallback @Inject constructor(): DiffUtil.ItemCallback<ClientData>() {
    override fun areItemsTheSame(oldItem: ClientData, newItem: ClientData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ClientData, newItem: ClientData): Boolean {
        return oldItem == newItem
    }

}