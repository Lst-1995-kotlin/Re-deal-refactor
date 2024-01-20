package com.hifi.redeal.transaction.util

import androidx.recyclerview.widget.DiffUtil
import com.hifi.redeal.transaction.model.Client

class ClientDiffCallback: DiffUtil.ItemCallback<Client>() {
    override fun areItemsTheSame(oldItem: Client, newItem: Client): Boolean {
        return oldItem.getClientIdx() == newItem.getClientIdx()
    }

    override fun areContentsTheSame(oldItem: Client, newItem: Client): Boolean {
        return oldItem == newItem
    }

}