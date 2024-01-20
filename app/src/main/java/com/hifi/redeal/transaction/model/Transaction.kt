package com.hifi.redeal.transaction.model

import android.widget.TextView
import com.hifi.redeal.transaction.adapter.TransactionAdapter.Companion.DEPOSIT_TRANSACTION
import com.hifi.redeal.transaction.adapter.TransactionAdapter.Companion.SALES_TRANSACTION
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class Transaction(
    private val transactionData: TransactionData,
) {
    private val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())
    private val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
    private var transactionClientName: String? = null


    override fun hashCode(): Int {
        var result = transactionData.hashCode()
        result = 31 * result + transactionClientName.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        val otherTransaction = other as Transaction
        return otherTransaction.transactionData == this.transactionData &&
                otherTransaction.transactionClientName == this.transactionClientName
    }

    fun calculateSalesAmount(): Long {
        if (transactionData.isDeposit) return 0L
        return transactionData.transactionItemCount * transactionData.transactionItemPrice.toLong()
    }

    fun calculateReceivables() =
        calculateSalesAmount() - transactionData.transactionAmountReceived.toLong()


    fun isNotSettingClientName() = transactionClientName.isNullOrEmpty()

    fun setTransactionClientName(name: String) {
        transactionClientName = name
    }

    fun getTransactionType(): Int {
        if (transactionData.isDeposit) return DEPOSIT_TRANSACTION
        return SALES_TRANSACTION
    }

    fun getTransactionDate() = transactionData.date

    fun getTransactionClientIdx() = transactionData.clientIdx

    fun getTransactionIdx() = transactionData.transactionIdx

    fun setTextViewValue(
        date: TextView,
        clientName: TextView,
        price: TextView,
    ) {
        date.text = dateFormat.format(transactionData.date.toDate())
        clientName.text = transactionClientName
        price.text = numberFormat.format(transactionData.transactionAmountReceived.toLong())
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
        date.text = dateFormat.format(transactionData.date.toDate())
        clientName.text = transactionClientName
        itemName.text = transactionData.transactionItemName
        itemCount.text = numberFormat.format(transactionData.transactionItemCount)
        itemAmount.text = numberFormat.format(transactionData.transactionItemPrice.toLong())
        totalAmount.text =
            numberFormat.format(
                transactionData.transactionItemPrice.toLong() *
                        transactionData.transactionItemCount,
            )
        receivedAmount.text =
            numberFormat.format(transactionData.transactionAmountReceived.toLong())
        receivables.text =
            numberFormat.format(
                transactionData.transactionItemPrice.toLong() *
                        transactionData.transactionItemCount -
                        transactionData.transactionAmountReceived.toLong(),
            )
    }
}
