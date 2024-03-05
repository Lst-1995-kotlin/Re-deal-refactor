package com.hifi.redeal.transaction.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.transaction.adapter.viewHolder.ViewHolderFactory
import com.hifi.redeal.transaction.adapter.viewHolder.transactionEdit.DepositSelectHolder
import com.hifi.redeal.transaction.adapter.viewHolder.transactionEdit.SalesSelectHolder
import com.hifi.redeal.transaction.model.TransactionBasic

class TransactionSelectAdapter(
    private val viewHolderFactories: Map<Int, ViewHolderFactory>,
    diffCallback: TransactionSelectAdapterDiffCallback
) : ListAdapter<TransactionBasic, RecyclerView.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val factory = viewHolderFactories[viewType]
            ?: throw IllegalArgumentException("올바르지 못한 클라이언트 타입 입니다.")
        return factory.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DepositSelectHolder -> {
                holder.bind(currentList[position])
                holder.setSelectedImage(currentList[position].isSelected())
            }

            is SalesSelectHolder -> {
                holder.bind(currentList[position])
                holder.setSelectedImage(currentList[position].isSelected())
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return currentList[position].getTransactionType()
    }

}


