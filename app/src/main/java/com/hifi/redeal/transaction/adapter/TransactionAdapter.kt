package com.hifi.redeal.transaction.adapter

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.transaction.configuration.TransactionType
import com.hifi.redeal.transaction.model.Transaction
import com.hifi.redeal.transaction.viewHolder.transaction.DepositHolder
import com.hifi.redeal.transaction.viewHolder.transaction.SalesHolder
import com.hifi.redeal.transaction.viewHolder.ViewHolderFactory

class TransactionAdapter(
    private val viewHolderFactories: Map<Int, ViewHolderFactory>,
    diffCallback: TransactionAdapterDiffCallback
) : ListAdapter<Transaction, RecyclerView.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val factory = viewHolderFactories[viewType]
        return factory?.create(parent) ?: createDefaultViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val transaction = currentList[position]
        when (holder.itemViewType) {
            TransactionType.DEPOSIT.type -> {
                val item = holder as DepositHolder
                item.bind(transaction, position)
            }

            TransactionType.SALES.type -> {
                val item = holder as SalesHolder
                item.bind(transaction, position)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return currentList[position].getTransactionType()
    }

    private fun createDefaultViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        Log.e("TransactionAdapter", "올바르지 못한 거래 타입 입니다")
        val view = View(parent.context)
        return object : RecyclerView.ViewHolder(view) {}
    }
}


