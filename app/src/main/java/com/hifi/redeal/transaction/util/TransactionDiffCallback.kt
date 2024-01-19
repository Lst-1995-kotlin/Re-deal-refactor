package com.hifi.redeal.transaction.util

import androidx.recyclerview.widget.DiffUtil
import com.hifi.redeal.transaction.model.Transaction

class TransactionDiffCallback : DiffUtil.ItemCallback<Transaction>() {
    override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem.getTransactionIdx() == newItem.getTransactionIdx()
    }

    override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem == newItem
    }

}