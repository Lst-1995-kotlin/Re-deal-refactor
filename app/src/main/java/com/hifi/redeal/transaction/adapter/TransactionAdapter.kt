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
import com.hifi.redeal.transaction.viewHolder.transaction.CountHolder

class TransactionAdapter(
    private val viewHolderFactories: Map<Int, ViewHolderFactory>,
    diffCallback: TransactionAdapterDiffCallback
) : ListAdapter<Transaction, RecyclerView.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val factory = viewHolderFactories[viewType]
            ?: throw IllegalArgumentException("올바르지 못한 클라이언트 타입 입니다.")
        return factory.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DepositHolder -> {
                holder.bind(currentList[position], position)
            }

            is SalesHolder -> {
                holder.bind(currentList[position], position)
            }

            is CountHolder -> {
                holder.bind()
            }
        }
    }

    override fun getItemCount(): Int {
        return currentList.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        if (position == itemCount - 1) return TransactionType.COUNT.type
        return currentList[position].getTransactionType()
    }
}


