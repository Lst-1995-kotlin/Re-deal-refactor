package com.hifi.redeal.trade.ui.adapter.viewHolder.trade

import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.R
import com.hifi.redeal.databinding.DialogTradeEditBinding
import com.hifi.redeal.databinding.RowTradeDepositBinding

import com.hifi.redeal.trade.data.model.TradeData
import com.hifi.redeal.util.toDateYearOfDayFormat
import com.hifi.redeal.util.toNumberFormat

class DepositHolder(
    private val rowTransactionDepositBinding: RowTradeDepositBinding,
    private val onDeleteClickListener: (TradeData) -> Unit,
    private val onEditClickListener: (TradeData) -> Unit
) : RecyclerView.ViewHolder(rowTransactionDepositBinding.root) {

    fun bind(tradeData: TradeData) {
        rowTransactionDepositBinding.run {
            textTransactionDate.text = tradeData.date.toDateYearOfDayFormat()
            transctionClientNameTextView.text = tradeData.clientName
            depositPriceTextView.text = tradeData.receivedAmount.toNumberFormat()
            setLongClickEvent(root, tradeData)
        }
    }

    private fun setLongClickEvent(view: View, tradeData: TradeData) {
        view.setOnLongClickListener {
            val builder = AlertDialog.Builder(view.context, R.style.RoundedAlertDialog)
            val layoutInflater = LayoutInflater.from(view.context)
            val dialogTransactionEditBinding = DialogTradeEditBinding.inflate(layoutInflater)
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