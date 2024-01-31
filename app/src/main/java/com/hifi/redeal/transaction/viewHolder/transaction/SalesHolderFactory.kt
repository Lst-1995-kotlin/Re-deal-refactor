package com.hifi.redeal.transaction.viewHolder.transaction

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.MainActivity
import com.hifi.redeal.databinding.RowTransactionSalesBinding
import com.hifi.redeal.transaction.viewHolder.ViewHolderFactory
import com.hifi.redeal.transaction.viewmodel.TransactionViewModel

class SalesHolderFactory(
    private val mainActivity: MainActivity,
    private val transactionViewModel: TransactionViewModel
) : ViewHolderFactory {
    override fun create(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowTransactionSalesBinding.inflate(inflater, parent, false)
        return SalesHolder(binding, mainActivity, transactionViewModel)
    }
}