package com.hifi.redeal.transaction.model

import android.widget.TextView
import com.hifi.redeal.transaction.configuration.TransactionType
import com.hifi.redeal.transaction.util.TransactionNumberFormatUtil.replaceNumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class Transaction(
    private val transactionData: TransactionData,
    private val clientData: ClientData
) {
    private val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
    override fun hashCode(): Int {
        return transactionData.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        val otherTransaction = other as Transaction
        return otherTransaction.transactionData == this.transactionData
    }

    fun calculateSalesAmount(): Long {
        if (transactionData.isDeposit) return 0L
        return transactionData.transactionItemCount * transactionData.transactionItemPrice
    }

    fun getTransactionType(): Int {
        return when (transactionData.isDeposit) {
            true -> TransactionType.DEPOSIT.type
            false -> TransactionType.SALES.type
        }
    }

    fun getTransactionDate() = transactionData.date

    fun equalsTransactionClientIndex(index: Long) = clientData.clientIdx == index

    fun getTransactionIdx() = transactionData.transactionIdx

    fun getReceivables() = transactionData.transactionAmountReceived

    fun getClientInformation() = Client(clientData)

    fun getTransactionValueMap(): HashMap<String, String> {
        val currentTransactionData = HashMap<String, String>()
        if (transactionData.isDeposit) { // 입금 내역일 경우
            currentTransactionData["date"] = dateFormat.format(transactionData.date.toDate())
            currentTransactionData["clientName"] = clientData.clientName
            currentTransactionData["amountReceived"] =
                replaceNumberFormat(transactionData.transactionAmountReceived)
            return currentTransactionData
        }
        currentTransactionData["date"] = dateFormat.format(transactionData.date.toDate())
        currentTransactionData["clientName"] = clientData.clientName
        currentTransactionData["itemName"] = transactionData.transactionItemName
        currentTransactionData["itemCount"] =
            replaceNumberFormat(transactionData.transactionItemCount)
        currentTransactionData["itemPrice"] =
            replaceNumberFormat(transactionData.transactionItemPrice)
        currentTransactionData["totalAmount"] =
            replaceNumberFormat(
                transactionData.transactionItemPrice *
                        transactionData.transactionItemCount
            )
        currentTransactionData["amountReceived"] =
            replaceNumberFormat(transactionData.transactionAmountReceived)
        currentTransactionData["receivables"] = replaceNumberFormat(
            transactionData.transactionItemPrice *
                    transactionData.transactionItemCount -
                    transactionData.transactionAmountReceived,
        )
        return currentTransactionData
    }

    fun setModifyViewValue(
        itemName: TextView,
        itemCount: TextView,
        itemAmount: TextView,
        receivedAmount: TextView,
    ) {
        itemName.text = transactionData.transactionItemName
        itemCount.text = replaceNumberFormat(transactionData.transactionItemCount)
        itemAmount.text = replaceNumberFormat(transactionData.transactionItemPrice)
        receivedAmount.text = replaceNumberFormat(transactionData.transactionAmountReceived)
    }

    fun setModifyViewValue(receivedAmount: TextView) {
        receivedAmount.text = replaceNumberFormat(transactionData.transactionAmountReceived)
    }
}
