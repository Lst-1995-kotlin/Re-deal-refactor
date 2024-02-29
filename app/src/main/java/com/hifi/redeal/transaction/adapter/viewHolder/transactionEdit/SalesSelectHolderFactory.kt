package com.hifi.redeal.transaction.adapter.viewHolder.transactionEdit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.databinding.RowTransactionSelectSalesBinding
import com.hifi.redeal.transaction.adapter.viewHolder.ViewHolderFactory
import com.hifi.redeal.transaction.viewmodel.TransactionViewModel

class SalesSelectHolderFactory(
    private val transactionViewModel: TransactionViewModel
) : ViewHolderFactory {
    override fun create(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowTransactionSelectSalesBinding.inflate(inflater, parent, false)
        return SalesSelectHolder(binding, transactionViewModel)
    }
}