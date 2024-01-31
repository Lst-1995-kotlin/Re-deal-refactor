package com.hifi.redeal.transaction.model

import android.widget.TextView
import com.hifi.redeal.transaction.configuration.TransactionType
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

    fun getReceivables() = loadTransactionData.transactionAmountReceived


    fun getTransactionType(): Int {
        return when (loadTransactionData.isDeposit) {
            true -> TransactionType.DEPOSIT.type
            false -> TransactionType.SALES.type
            else -> TransactionType.ERROR.type
        }
    }

    fun getTransactionDate() = loadTransactionData.date

    fun getTransactionClientIdx() = loadTransactionData.clientIdx

    fun getTransactionIdx() = loadTransactionData.transactionIdx

    fun getTransactionValueMap(): HashMap<String, String> {
        val currentTransactionData = HashMap<String, String>()
        if (loadTransactionData.isDeposit) { // 입금 내역일 경우
            currentTransactionData["date"] = dateFormat.format(loadTransactionData.date.toDate())
            currentTransactionData["clientName"] = loadTransactionData.clientName
            currentTransactionData["amountReceived"] =
                replaceNumberFormat(loadTransactionData.transactionAmountReceived)
            return currentTransactionData
        }
        currentTransactionData["date"] = dateFormat.format(loadTransactionData.date.toDate())
        currentTransactionData["clientName"] = loadTransactionData.clientName
        currentTransactionData["itemName"] = loadTransactionData.transactionItemName
        currentTransactionData["itemCount"] =
            replaceNumberFormat(loadTransactionData.transactionItemCount)
        currentTransactionData["itemPrice"] =
            replaceNumberFormat(loadTransactionData.transactionItemPrice)
        currentTransactionData["totalAmount"] =
            replaceNumberFormat(
                loadTransactionData.transactionItemPrice *
                        loadTransactionData.transactionItemCount
            )
        currentTransactionData["amountReceived"] =
            replaceNumberFormat(loadTransactionData.transactionAmountReceived)
        currentTransactionData["receivables"] = replaceNumberFormat(
            loadTransactionData.transactionItemPrice *
                    loadTransactionData.transactionItemCount -
                    loadTransactionData.transactionAmountReceived,
        )
        return currentTransactionData
    }

    fun setModifyViewValue(
        itemName: TextView,
        itemCount: TextView,
        itemAmount: TextView,
        receivedAmount: TextView,
    ) {
        itemName.text = loadTransactionData.transactionItemName
        itemCount.text = replaceNumberFormat(loadTransactionData.transactionItemCount)
        itemAmount.text = replaceNumberFormat(loadTransactionData.transactionItemPrice)
        receivedAmount.text = replaceNumberFormat(loadTransactionData.transactionAmountReceived)
    }

    fun setModifyViewValue(receivedAmount: TextView) {
        receivedAmount.text = replaceNumberFormat(loadTransactionData.transactionAmountReceived)
    }
}
