package com.hifi.redeal.trade.ui.adapter.viewHolder.trade

import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.R
import com.hifi.redeal.databinding.DialogTradeEditBinding
import com.hifi.redeal.databinding.RowTradeSalesBinding
import com.hifi.redeal.trade.data.model.TradeData
import com.hifi.redeal.util.toDateYearOfDayFormat
import com.hifi.redeal.util.toNumberFormat

class SalesHolder(
    private val rowTransactionReleaseBinding: RowTradeSalesBinding,
    private val onDeleteClickListener: (TradeData) -> Unit,
    private val onEditClickListener: (TradeData) -> Unit
) : RecyclerView.ViewHolder(rowTransactionReleaseBinding.root) {

    fun bind(tradeData: TradeData) {
        rowTransactionReleaseBinding.run {
            textTradeDate.text = tradeData.date.toDateYearOfDayFormat()
            tradeClientNameTextView.text = "${tradeData.clientName} ${tradeData.managerName}"
            itemNameTextView.text = tradeData.itemName
            itemSalesCountTextView.text = (tradeData.itemCount).toNumberFormat()
            itemPriceTextView.text = (tradeData.itemPrice).toNumberFormat()
            totalSalesAmountTextView.text =
                (tradeData.itemCount * tradeData.itemPrice).toNumberFormat()
            recievedAmountTextView.text = (tradeData.receivedAmount).toNumberFormat()
            recievablesTextView.text =
                (tradeData.itemCount * tradeData.itemPrice - tradeData.receivedAmount).toNumberFormat()
            if (tradeData.itemCount * tradeData.itemPrice - tradeData.receivedAmount == 0L) {
                recievablesTextView.visibility = View.GONE
                textTrade23.visibility = View.GONE
                textTrade24.visibility = View.GONE
            }
            setLongClickEvent(root, tradeData)
        }
    }

    private fun setLongClickEvent(view: View, tradeData: TradeData) {
        view.setOnLongClickListener {
            val builder = AlertDialog.Builder(view.context, R.style.RoundedAlertDialog)
            val layoutInflater = LayoutInflater.from(view.context)
            val dialogTransactionEditBinding =
                DialogTradeEditBinding.inflate(layoutInflater)
            builder.setView(dialogTransactionEditBinding.root)

            val dialog = builder.show()
            dialogTransactionEditBinding.run {
                tradeDeleteImageButton.setOnClickListener {
                    dialog.dismiss()
                    onDeleteClickListener(tradeData)
                }
                tradeEditImageButton.setOnClickListener {
                    dialog.dismiss()
                    onEditClickListener(tradeData)
                }
            }
            true
        }
    }
}