package com.hifi.redeal.transaction.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hifi.redeal.transaction.model.TransactionBasic
import javax.inject.Inject

class TransactionSelectAdapterDiffCallback @Inject constructor() :
    DiffUtil.ItemCallback<TransactionBasic>() {

    override fun areItemsTheSame(oldItem: TransactionBasic, newItem: TransactionBasic): Boolean {
        return oldItem.getTransactionIdx() == newItem.getTransactionIdx()
    }

    override fun areContentsTheSame(oldItem: TransactionBasic, newItem: TransactionBasic): Boolean {
        return oldItem == newItem
    }

}