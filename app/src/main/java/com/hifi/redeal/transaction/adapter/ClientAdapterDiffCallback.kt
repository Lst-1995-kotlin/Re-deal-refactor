package com.hifi.redeal.transaction.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hifi.redeal.transaction.model.Client

class ClientAdapterDiffCallback: DiffUtil.ItemCallback<Client>() {
    override fun areItemsTheSame(oldItem: Client, newItem: Client): Boolean {
        return oldItem.getClientIdx() == newItem.getClientIdx()
    }

    override fun areContentsTheSame(oldItem: Client, newItem: Client): Boolean {
        return oldItem == newItem
    }

}