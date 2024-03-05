package com.hifi.redeal.transaction.adapter

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import com.hifi.redeal.transaction.model.TransactionBasic
import javax.inject.Inject

class TransactionAdapterDiffCallback @Inject constructor() : DiffUtil.ItemCallback<TransactionBasic>() {

    override fun areItemsTheSame(oldItem: TransactionBasic, newItem: TransactionBasic): Boolean {
        return oldItem.getTransactionIdx() == newItem.getTransactionIdx()
    }

    override fun areContentsTheSame(oldItem: TransactionBasic, newItem: TransactionBasic): Boolean {
        Log.d("tttt", "old : ${oldItem.hashCode()} new : ${newItem.hashCode()} == : ${oldItem == newItem}")
        return oldItem == newItem
    }

}