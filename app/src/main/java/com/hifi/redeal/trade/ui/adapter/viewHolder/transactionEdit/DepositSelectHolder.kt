package com.hifi.redeal.trade.ui.adapter.viewHolder.transactionEdit

import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.R
import com.hifi.redeal.databinding.RowTransactionSelectDepositBinding
import com.hifi.redeal.trade.data.model.TradeData
import com.hifi.redeal.trade.domain.viewmodel.TradeViewModel

class DepositSelectHolder(
    private val rowTransactionSelectDepositBinding: RowTransactionSelectDepositBinding,
    private val tradeViewModel: TradeViewModel
) : RecyclerView.ViewHolder(rowTransactionSelectDepositBinding.root) {
    fun bind(tradeData: TradeData) {
        rowTransactionSelectDepositBinding.run {
            transactionSelectDateTextView.text = tradeData.date.toString()
            transactionSelectClientNameTextView.text = tradeData.clientName
            depositPriceTextView.text = tradeData.receivedAmount.toString()
            setClickEvent(rowTransactionSelectDepositBinding, tradeData)
        }
    }

    fun setSelectedImage(isSelected: Boolean) {
        if (isSelected) {
            rowTransactionSelectDepositBinding.transactionSelectDepositCheckBox.setImageResource(
                R.drawable.done_paint_24px
            )
            return
        }
        rowTransactionSelectDepositBinding.transactionSelectDepositCheckBox.setImageResource(
            R.drawable.rounded_rectangle_stroke_primary20
        )
    }

    private fun setClickEvent(
        rowTransactionSelectDepositBinding: RowTransactionSelectDepositBinding,
        tradeData: TradeData
    ) {
        rowTransactionSelectDepositBinding.root.setOnClickListener {
            //tradeViewModel.transactionSelectedChanged(tradeData.id)
        }
    }
}