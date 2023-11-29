package com.hifi.redeal.transaction.model

import android.view.LayoutInflater
import android.view.View
import com.hifi.redeal.databinding.RowTransactionBinding
import com.hifi.redeal.databinding.RowTransactionDepositBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class Transaction(
    private val transactionData: CustomTransactionData
) {
    fun getTransactionView(inflater: LayoutInflater): View {
        val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())
        val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
        if (transactionData.isDeposit) {
            val rowTransactionDepositBinding = RowTransactionDepositBinding.inflate(inflater)
            rowTransactionDepositBinding.run {
                textTransactionDate.text = dateFormat.format(transactionData.date.toDate())
                transctionClientNameTextView.text = transactionData.clientName
                depositPriceTextView.text =
                    numberFormat.format(transactionData.transactionAmountReceived.toLong())
            }
            return rowTransactionDepositBinding.root
        }
        val rowTransactionBinding = RowTransactionBinding.inflate(inflater)
        rowTransactionBinding.run {
            textTransactionDate.text = dateFormat.format(transactionData.date.toDate())
            transctionClientNameTextView.text = transactionData.clientName
            textProductName.text = transactionData.transactionName
            textProductCount.text = numberFormat.format(transactionData.transactionItemCount)
            textUnitPrice.text = numberFormat.format(transactionData.transactionItemPrice.toLong())
            textTotalAmount.text =
                numberFormat.format(
                    transactionData.transactionItemPrice.toLong() * transactionData.transactionItemCount
                )
            textRecievedAmount.text =
                numberFormat.format(transactionData.transactionAmountReceived.toLong())
            textRecievables.text = numberFormat.format(
                (transactionData.transactionItemPrice.toLong() * transactionData.transactionItemCount) - transactionData.transactionAmountReceived.toLong()
            )
        }
        return rowTransactionBinding.root
    }
}