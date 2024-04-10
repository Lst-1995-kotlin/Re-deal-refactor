package com.hifi.redeal.trade.ui.adapter.viewHolder.trade

import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.databinding.RowTradeCountBinding

class CountHolder(
    private val rowTransactionCountBinding: RowTradeCountBinding
) : RecyclerView.ViewHolder(rowTransactionCountBinding.root) {
    fun bind(itemCount: Int) {
        rowTransactionCountBinding.transactionCountTextView.text = "${itemCount}개의 거래내역"
    }
}