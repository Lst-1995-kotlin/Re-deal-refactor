package com.hifi.redeal.trade.ui.adapter.viewHolder.trade

import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.R
import com.hifi.redeal.databinding.DialogTransactionEditBinding
import com.hifi.redeal.databinding.RowTransactionSalesBinding
import com.hifi.redeal.trade.data.model.TradeData
import com.hifi.redeal.util.toDateYearOfDayFormat
import com.hifi.redeal.util.toNumberFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class SalesHolder(
    private val rowTransactionReleaseBinding: RowTransactionSalesBinding,
    private val onDeleteClickListener: (TradeData) -> Unit,
    private val onEditClickListener: (TradeData) -> Unit
) : RecyclerView.ViewHolder(rowTransactionReleaseBinding.root) {

    fun bind(tradeData: TradeData) {
        rowTransactionReleaseBinding.run {
            textTransactionDate.text = tradeData.date.toDateYearOfDayFormat()
            transctionClientNameTextView.text = tradeData.clientName
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
                textTransaction23.visibility = View.GONE
                textTransaction24.visibility = View.GONE
            }
            setLongClickEvent(root, tradeData)
        }
    }

    private fun setLongClickEvent(view: View, tradeData: TradeData) {
        view.setOnLongClickListener {
            val builder = AlertDialog.Builder(view.context, R.style.RoundedAlertDialog)
            val layoutInflater = LayoutInflater.from(view.context)
            val dialogTransactionEditBinding =
                DialogTransactionEditBinding.inflate(layoutInflater)
            builder.setView(dialogTransactionEditBinding.root)

            val dialog = builder.show()
            dialogTransactionEditBinding.run {
                transactionDeleteImageButton.setOnClickListener {
                    dialog.dismiss()
                    onDeleteClickListener(tradeData)
                }
                transactionEditImageButton.setOnClickListener {
                    dialog.dismiss()
                    onEditClickListener(tradeData)
                }
            }
            true
        }
    }
}