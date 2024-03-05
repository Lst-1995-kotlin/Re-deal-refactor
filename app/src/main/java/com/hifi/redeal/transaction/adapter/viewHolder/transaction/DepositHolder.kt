package com.hifi.redeal.transaction.adapter.viewHolder.transaction

import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.R
import com.hifi.redeal.databinding.DialogTransactionEditBinding
import com.hifi.redeal.databinding.RowTransactionDepositBinding
import com.hifi.redeal.transaction.model.TradeData
import com.hifi.redeal.transaction.model.TransactionBasic
import com.hifi.redeal.transaction.util.TransactionNumberFormatUtil.replaceNumberFormat
import com.hifi.redeal.transaction.viewmodel.TradeViewModel
import com.hifi.redeal.transaction.viewmodel.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class DepositHolder(
    private val rowTransactionDepositBinding: RowTransactionDepositBinding,
    private val tradeViewModel: TradeViewModel
) : RecyclerView.ViewHolder(rowTransactionDepositBinding.root) {

    private val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
    fun bind(tradeData: TradeData) {
        rowTransactionDepositBinding.run {
            textTransactionDate.text = dateFormat.format(tradeData.date)
            transctionClientNameTextView.text = tradeData.clientName
            depositPriceTextView.text = replaceNumberFormat(tradeData.receivedAmount)
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