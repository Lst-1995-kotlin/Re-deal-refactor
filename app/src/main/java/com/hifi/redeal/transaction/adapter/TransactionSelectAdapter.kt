package com.hifi.redeal.transaction.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.R
import com.hifi.redeal.transaction.configuration.TransactionType
import com.hifi.redeal.transaction.model.Transaction
import com.hifi.redeal.transaction.viewHolder.ViewHolderFactory
import com.hifi.redeal.transaction.viewHolder.transaction.CountHolder
import com.hifi.redeal.transaction.viewHolder.transaction.DepositHolder
import com.hifi.redeal.transaction.viewHolder.transaction.SalesHolder
import com.hifi.redeal.transaction.viewHolder.transactionEdit.DepositSelectHolder
import com.hifi.redeal.transaction.viewHolder.transactionEdit.SalesSelectHolder

class TransactionSelectAdapter(
    private val viewHolderFactories: Map<Int, ViewHolderFactory>,
    diffCallback: TransactionSelectAdapterDiffCallback
) : ListAdapter<Transaction, RecyclerView.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val factory = viewHolderFactories[viewType]
            ?: throw IllegalArgumentException("올바르지 못한 클라이언트 타입 입니다.")
        return factory.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DepositSelectHolder -> {
                holder.bind(currentList[position])
                holder.setCheckImage(currentList[position].isSelected())
            }

            is SalesSelectHolder -> {
                holder.bind(currentList[position])
                holder.setCheckImage(currentList[position].isSelected())
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return currentList[position].getTransactionType()
    }

}


