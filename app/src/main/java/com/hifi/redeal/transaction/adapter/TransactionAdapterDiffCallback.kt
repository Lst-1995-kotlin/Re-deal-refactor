package com.hifi.redeal.transaction.adapter

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import com.hifi.redeal.transaction.model.Transaction
import javax.inject.Inject

class TransactionAdapterDiffCallback @Inject constructor() : DiffUtil.ItemCallback<Transaction>() {

    override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem.getTransactionIdx() == newItem.getTransactionIdx()
    }

    override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        Log.d("tttt", "old : ${oldItem.hashCode()} new : ${newItem.hashCode()} == : ${oldItem == newItem}")
        return oldItem == newItem
    }

}