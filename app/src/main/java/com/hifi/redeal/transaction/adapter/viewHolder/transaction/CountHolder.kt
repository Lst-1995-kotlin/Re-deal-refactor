package com.hifi.redeal.transaction.adapter.viewHolder.transaction

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.databinding.RowTransactionCountBinding
import com.hifi.redeal.transaction.viewmodel.TradeViewModel

class CountHolder(
    private val rowTransactionCountBinding: RowTransactionCountBinding,
    private val tradeViewModel: TradeViewModel,
    private val viewLifecycleOwner: LifecycleOwner
) : RecyclerView.ViewHolder(rowTransactionCountBinding.root) {
    fun bind() {
        tradeViewModel.tradeCount.observe(viewLifecycleOwner) {
            rowTransactionCountBinding.transactionCountTextView.text = "${it}개의 거래내역"
        }
    }
}