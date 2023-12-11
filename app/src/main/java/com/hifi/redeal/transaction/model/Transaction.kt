package com.hifi.redeal.transaction.model

import android.widget.TextView
import com.hifi.redeal.transaction.adapter.TransactionAdapter.Companion.DEPOSIT_TRANSACTION
import com.hifi.redeal.transaction.adapter.TransactionAdapter.Companion.WITHDRAWAL_TRANSACTION
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class Transaction(
    private val transactionData: TransactionData,
) {

    private val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())
    private val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
    private var transactionClientName = ""

    fun setTransactionClientName(name: String) {
        transactionClientName = name
    }

    fun getTransactionType(): Int {
        if (transactionData.isDeposit) return DEPOSIT_TRANSACTION
        return WITHDRAWAL_TRANSACTION
    }

    fun getTransactionDate() = transactionData.date

    fun getTransactionClientIdx() = transactionData.clientIdx

    fun setTextViewValue(date: TextView, clientName: TextView, price: TextView) {
        date.text = dateFormat.format(transactionData.date.toDate())
        clientName.text = transactionClientName
        price.text = numberFormat.format(transactionData.transactionAmountReceived.toLong())
    }

    fun setTextViewValue(
        date: TextView,
        clientName: TextView,
        productName: TextView,
        productCount: TextView,
        unitPrice: TextView,
        totalAmount: TextView,
        receivedAmount: TextView,
        receivables: TextView,
    ) {
        date.text = dateFormat.format(transactionData.date.toDate())
        clientName.text = transactionClientName
        productName.text = transactionData.transactionName
        productCount.text = numberFormat.format(transactionData.transactionItemCount)
        unitPrice.text = numberFormat.format(transactionData.transactionItemPrice.toLong())
        totalAmount.text = numberFormat.format(
            transactionData.transactionItemPrice.toLong() *
                transactionData.transactionItemCount,
        )
        receivedAmount.text =
            numberFormat.format(transactionData.transactionAmountReceived.toLong())
        receivables.text = numberFormat.format(
            (
                transactionData.transactionItemPrice.toLong() *
                    transactionData.transactionItemCount
                ) -
                transactionData.transactionAmountReceived.toLong(),
        )
    }
}
