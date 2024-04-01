package com.hifi.redeal.trade.ui.adapter.viewHolder.trade

import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.R
import com.hifi.redeal.databinding.DialogTransactionEditBinding
import com.hifi.redeal.databinding.RowTransactionDepositBinding
import com.hifi.redeal.trade.data.model.TradeData
import com.hifi.redeal.util.toDateYearOfDayFormat
import com.hifi.redeal.util.toNumberFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class DepositHolder(
    private val rowTransactionDepositBinding: RowTransactionDepositBinding,
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