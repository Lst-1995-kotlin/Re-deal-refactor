package com.hifi.redeal.transaction.adapter.viewHolder.transaction

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.databinding.RowTransactionSalesBinding
import com.hifi.redeal.transaction.adapter.viewHolder.ViewHolderFactory
import com.hifi.redeal.transaction.viewmodel.TransactionViewModel

class SalesHolderFactory(
    private val transactionViewModel: TransactionViewModel
) : ViewHolderFactory {
    override fun create(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowTransactionSalesBinding.inflate(inflater, parent, false)
        return SalesHolder(binding, transactionViewModel)
    }
}