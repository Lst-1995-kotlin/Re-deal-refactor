package com.hifi.redeal.transaction.adapter.viewHolder.trade

import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.databinding.RowTransactionCountBinding

class CountHolder(
    private val rowTransactionCountBinding: RowTransactionCountBinding
) : RecyclerView.ViewHolder(rowTransactionCountBinding.root) {
    fun bind(itemCount: Int) {
        rowTransactionCountBinding.transactionCountTextView.text = "${itemCount}개의 거래내역"
    }
}