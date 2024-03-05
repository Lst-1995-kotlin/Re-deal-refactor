package com.hifi.redeal.transaction.adapter.viewHolder.transaction

import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.R
import com.hifi.redeal.databinding.DialogTransactionEditBinding
import com.hifi.redeal.databinding.RowTransactionSalesBinding
import com.hifi.redeal.transaction.model.TradeData
import com.hifi.redeal.transaction.util.TransactionNumberFormatUtil.replaceNumberFormat
import com.hifi.redeal.transaction.viewmodel.TradeViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class SalesHolder(
    private val rowTransactionReleaseBinding: RowTransactionSalesBinding,
    private val tradeViewModel: TradeViewModel
) : RecyclerView.ViewHolder(rowTransactionReleaseBinding.root) {

    private val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    fun bind(tradeData: TradeData) {
        rowTransactionReleaseBinding.run {
            textTransactionDate.text = dateFormat.format(tradeData.date)
            transctionClientNameTextView.text = tradeData.clientName
            itemNameTextView.text = tradeData.itemName
            itemSalesCountTextView.text = replaceNumberFormat(tradeData.itemCount)
            itemPriceTextView.text = replaceNumberFormat(tradeData.itemPrice)
            totalSalesAmountTextView.text = replaceNumberFormat(tradeData.totalItemAmount)
            recievedAmountTextView.text = replaceNumberFormat(tradeData.receivedAmount)
            recievablesTextView.text =
                replaceNumberFormat(tradeData.itemCount * tradeData.itemPrice - tradeData.receivedAmount)
            if (tradeData.itemCount * tradeData.itemPrice - tradeData.receivedAmount == 0L) {
                recievablesTextView.visibility = View.GONE
                textTransaction23.visibility = View.GONE
                textTransaction24.visibility = View.GONE
            }
            setLongClickEvent(root, tradeData, position)
        }
    }

    private fun setLongClickEvent(view: View, tradeData: TradeData, position: Int) {
        view.setOnLongClickListener {
            val builder = AlertDialog.Builder(view.context, R.style.RoundedAlertDialog)
            val layoutInflater = LayoutInflater.from(view.context)
            val dialogTransactionEditBinding =
                DialogTransactionEditBinding.inflate(layoutInflater)
            builder.setView(dialogTransactionEditBinding.root)

            val dialog = builder.show()
            dialogTransactionEditBinding.run {

            }
            true
        }
    }
}