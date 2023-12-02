package com.hifi.redeal.transaction.model

import android.widget.TextView
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class Transaction(
    private val transactionData: CustomTransactionData
) {

    private val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())
    private val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())

    fun getTransactionType(): Int {
        if (transactionData.isDeposit) return 1
        return 2
    }

    fun setViewText(date: TextView, clientName: TextView, price: TextView) {
        date.text = dateFormat.format(transactionData.date.toDate())
        clientName.text = transactionData.clientName
        price.text = numberFormat.format(transactionData.transactionAmountReceived.toLong())
    }

    fun setViewText(
        date: TextView,
        clientName: TextView,
        productName: TextView,
        productCount: TextView,
        unitPrice: TextView,
        totalAmount: TextView,
        receivedAmount: TextView,
        receivables: TextView
    ) {
        date.text = dateFormat.format(transactionData.date.toDate())
        clientName.text = transactionData.clientName
        productName.text = transactionData.transactionName
        productCount.text = numberFormat.format(transactionData.transactionItemCount)
        unitPrice.text = numberFormat.format(transactionData.transactionItemPrice.toLong())
        totalAmount.text = numberFormat.format(
            transactionData.transactionItemPrice.toLong() *
                    transactionData.transactionItemCount
        )
        receivedAmount.text =
            numberFormat.format(transactionData.transactionAmountReceived.toLong())
        receivables.text = numberFormat.format(
            (transactionData.transactionItemPrice.toLong() *
                    transactionData.transactionItemCount) -
                    transactionData.transactionAmountReceived.toLong()
        )
    }

}