package com.hifi.redeal.transaction.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hifi.redeal.transaction.model.Transaction

class TransactionAdapterDiffCallback : DiffUtil.ItemCallback<Transaction>() {
    override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem.getTransactionIdx() == newItem.getTransactionIdx()
    }

    override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem == newItem
    }

}