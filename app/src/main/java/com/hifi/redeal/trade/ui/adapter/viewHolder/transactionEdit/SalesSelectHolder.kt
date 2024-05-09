package com.hifi.redeal.trade.ui.adapter.viewHolder.transactionEdit

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.R
import com.hifi.redeal.databinding.RowTradeSelectSalesBinding
import com.hifi.redeal.trade.data.model.TradeData
import com.hifi.redeal.trade.data.model.TradeSelectData
import com.hifi.redeal.trade.ui.viewmodel.TradeViewModel
import com.hifi.redeal.util.toDateYearOfDayFormat
import com.hifi.redeal.util.toNumberFormat

class SalesSelectHolder(
    private val rowTransactionSelectSalesBinding: RowTradeSelectSalesBinding,
    private val onClickListener: (TradeSelectData) -> Unit
) : RecyclerView.ViewHolder(rowTransactionSelectSalesBinding.root) {
    fun bind(tradeSelectData: TradeSelectData) {
        rowTransactionSelectSalesBinding.run {
            transactionSelectDateTextView.text = tradeSelectData.date.toDateYearOfDayFormat()
            transactionSelectClientNameTextView.text = "${tradeSelectData.clientName} ${tradeSelectData.managerName}"
            itemNameTextView.text = tradeSelectData.itemName
            itemSalesCountTextView.text = tradeSelectData.itemCount.toNumberFormat()
            itemPriceTextView.text = tradeSelectData.itemPrice.toNumberFormat()
            totalSalesAmountTextView.text =
                (tradeSelectData.itemPrice * tradeSelectData.itemCount).toNumberFormat()
            recievedAmountTextView.text = tradeSelectData.receivedAmount.toNumberFormat()
            recievablesTextView.text =
                ((tradeSelectData.itemPrice * tradeSelectData.itemCount) - tradeSelectData.receivedAmount).toNumberFormat()
            if (((tradeSelectData.itemPrice * tradeSelectData.itemCount) - tradeSelectData.receivedAmount).toInt() == 0) {
                recievablesTextView.visibility = View.GONE
                textTransaction23.visibility = View.GONE
                textTransaction24.visibility = View.GONE
            }
            setClickEvent(rowTransactionSelectSalesBinding, tradeSelectData)
            if (tradeSelectData.checked) {
                rowTransactionSelectSalesBinding.transactionSelectSalesCheckBox.setImageResource(
                    R.drawable.done_paint_24px
                )
                return
            }
            rowTransactionSelectSalesBinding.transactionSelectSalesCheckBox.setImageResource(
                R.drawable.rounded_rectangle_stroke_primary20
            )
        }
    }

    private fun setClickEvent(
        rowTransactionSelectSalesBinding: RowTradeSelectSalesBinding,
        tradeSelectData: TradeSelectData
    ) {
        rowTransactionSelectSalesBinding.root.setOnClickListener {
            onClickListener(tradeSelectData)
        }
    }
}