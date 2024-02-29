package com.hifi.redeal.transaction.adapter.viewHolder.transactionEdit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.databinding.RowTransactionSelectDepositBinding
import com.hifi.redeal.transaction.adapter.viewHolder.ViewHolderFactory
import com.hifi.redeal.transaction.viewmodel.TransactionViewModel

class DepositSelectHolderFactory(
    private val transactionViewModel: TransactionViewModel
) : ViewHolderFactory {
    override fun create(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowTransactionSelectDepositBinding.inflate(inflater, parent, false)
        return DepositSelectHolder(binding, transactionViewModel)
    }
}