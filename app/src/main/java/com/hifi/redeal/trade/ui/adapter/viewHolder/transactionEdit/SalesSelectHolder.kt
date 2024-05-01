package com.hifi.redeal.trade.ui.adapter.viewHolder.transactionEdit

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.R
import com.hifi.redeal.databinding.RowTradeSelectSalesBinding
import com.hifi.redeal.trade.data.model.TradeData
import com.hifi.redeal.trade.ui.viewmodel.TradeViewModel

class SalesSelectHolder(
    private val rowTransactionSelectSalesBinding: RowTradeSelectSalesBinding,
    private val tradeViewModel: TradeViewModel
) : RecyclerView.ViewHolder(rowTransactionSelectSalesBinding.root) {
    fun bind(tradeData: TradeData) {
        rowTransactionSelectSalesBinding.run {
            transactionSelectDateTextView.text = tradeData.date.toString()
            transactionSelectClientNameTextView.text = tradeData.clientName
            itemNameTextView.text = tradeData.itemName
            itemSalesCountTextView.text = tradeData.itemCount.toString()
            itemPriceTextView.text = tradeData.itemPrice.toString()
            totalSalesAmountTextView.text = (tradeData.itemPrice * tradeData.itemCount).toString()
            recievedAmountTextView.text = tradeData.receivedAmount.toString()
            recievablesTextView.text =
                ((tradeData.itemPrice * tradeData.itemCount) - tradeData.receivedAmount).toString()
            if (((tradeData.itemPrice * tradeData.itemCount) - tradeData.receivedAmount).toInt() == 0) {
                recievablesTextView.visibility = View.GONE
                textTransaction23.visibility = View.GONE
                textTransaction24.visibility = View.GONE
            }
            //setClickEvent(rowTransactionSelectSalesBinding, tradeData)
        }
    }

    fun setSelectedImage(isSelected: Boolean) {
        if (isSelected) {
            rowTransactionSelectSalesBinding.transactionSelectSalesCheckBox.setImageResource(
                R.drawable.done_paint_24px
            )
            return
        }
        rowTransactionSelectSalesBinding.transactionSelectSalesCheckBox.setImageResource(
            R.drawable.rounded_rectangle_stroke_primary20
        )
    }

//    private fun setClickEvent(
//        rowTransactionSelectSalesBinding: RowTransactionSelectSalesBinding,
//        tradeData: TradeData
//    ) {
//        rowTransactionSelectSalesBinding.root.setOnClickListener {
//            transactionViewModel.transactionSelectedChanged(tradeData.id)
//        }
//    }
}