package com.hifi.redeal.trade.ui.adapter.viewHolder.tradeEdit

import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.R
import com.hifi.redeal.databinding.RowTradeSelectDepositBinding
import com.hifi.redeal.trade.data.model.TradeSelectData
import com.hifi.redeal.util.toDateYearOfDayFormat
import com.hifi.redeal.util.toNumberFormat

class DepositSelectHolder(
    private val rowTransactionSelectDepositBinding: RowTradeSelectDepositBinding,
    private val onClickListener: (TradeSelectData) -> Unit,
) : RecyclerView.ViewHolder(rowTransactionSelectDepositBinding.root) {
    fun bind(tradeSelectData: TradeSelectData) {
        rowTransactionSelectDepositBinding.run {
            transactionSelectDateTextView.text = tradeSelectData.date.toDateYearOfDayFormat()
            transactionSelectClientNameTextView.text =
                "${tradeSelectData.clientName} ${tradeSelectData.managerName}"
            depositPriceTextView.text = tradeSelectData.receivedAmount.toNumberFormat()
            setClickEvent(rowTransactionSelectDepositBinding, tradeSelectData)
            if (tradeSelectData.checked) {
                rowTransactionSelectDepositBinding.transactionSelectDepositCheckBox.setImageResource(
                    R.drawable.done_paint_24px
                )
                return
            }
            rowTransactionSelectDepositBinding.transactionSelectDepositCheckBox.setImageResource(
                R.drawable.rounded_rectangle_stroke_primary20
            )
        }
    }

    private fun setClickEvent(
        rowTransactionSelectDepositBinding: RowTradeSelectDepositBinding,
        tradeSelectData: TradeSelectData
    ) {
        rowTransactionSelectDepositBinding.root.setOnClickListener {
            onClickListener(tradeSelectData)
        }
    }
}