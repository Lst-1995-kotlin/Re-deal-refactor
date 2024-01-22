package com.hifi.redeal.transaction.model

import android.widget.TextView
import com.hifi.redeal.transaction.adapter.TransactionAdapter.Companion.DEPOSIT_TRANSACTION
import com.hifi.redeal.transaction.adapter.TransactionAdapter.Companion.SALES_TRANSACTION
import com.hifi.redeal.transaction.util.TransactionNumberFormatUtil.replaceNumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class Transaction(
    private val loadTransactionData: LoadTransactionData
) {
    private val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
    override fun hashCode(): Int {
        return loadTransactionData.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        val otherTransaction = other as Transaction
        return otherTransaction.loadTransactionData == this.loadTransactionData
    }

    fun calculateSalesAmount(): Long {
        if (loadTransactionData.isDeposit) return 0L
        return loadTransactionData.transactionItemCount * loadTransactionData.transactionItemPrice
    }

    fun calculateReceivables() =
        calculateSalesAmount() - loadTransactionData.transactionAmountReceived


    fun getTransactionType(): Int {
        if (loadTransactionData.isDeposit) return DEPOSIT_TRANSACTION
        return SALES_TRANSACTION
    }

    fun getTransactionDate() = loadTransactionData.date

    fun getTransactionClientIdx() = loadTransactionData.clientIdx

    fun getTransactionIdx() = loadTransactionData.transactionIdx

    fun setTextViewValue(
        date: TextView,
        clientName: TextView,
        price: TextView,
    ) {
        date.text = dateFormat.format(loadTransactionData.date.toDate())
        clientName.text = loadTransactionData.clientName
        price.text = replaceNumberFormat(loadTransactionData.transactionAmountReceived)
    }

    fun setTextViewValue(
        date: TextView,
        clientName: TextView,
        itemName: TextView,
        itemCount: TextView,
        itemAmount: TextView,
        totalAmount: TextView,
        receivedAmount: TextView,
        receivables: TextView,
    ) {
        date.text = dateFormat.format(loadTransactionData.date.toDate())
        clientName.text = loadTransactionData.clientName
        itemName.text = loadTransactionData.transactionItemName
        itemCount.text = replaceNumberFormat(loadTransactionData.transactionItemCount)
        itemAmount.text = replaceNumberFormat(loadTransactionData.transactionItemPrice)
        totalAmount.text =
            replaceNumberFormat(
                loadTransactionData.transactionItemPrice *
                        loadTransactionData.transactionItemCount
            )
        receivedAmount.text =
            replaceNumberFormat(loadTransactionData.transactionAmountReceived)
        receivables.text =
            replaceNumberFormat(
                loadTransactionData.transactionItemPrice *
                        loadTransactionData.transactionItemCount -
                        loadTransactionData.transactionAmountReceived,
            )
    }
}
