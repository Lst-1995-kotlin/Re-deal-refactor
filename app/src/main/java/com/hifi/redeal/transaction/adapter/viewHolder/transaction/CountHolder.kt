package com.hifi.redeal.transaction.adapter.viewHolder.transaction

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.databinding.RowTransactionCountBinding
import com.hifi.redeal.transaction.viewmodel.TransactionViewModel

class CountHolder(
    private val rowTransactionCountBinding: RowTransactionCountBinding,
    private val transactionViewModel: TransactionViewModel,
    private val viewLifecycleOwner: LifecycleOwner
) : RecyclerView.ViewHolder(rowTransactionCountBinding.root){
    fun bind() {
        transactionViewModel.transactionList.observe(viewLifecycleOwner) {
            rowTransactionCountBinding.transactionCountTextView.text = "${it.size}개의 거래내역"
        }
    }
}