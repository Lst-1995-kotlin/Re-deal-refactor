package com.hifi.redeal.transaction.model

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.hifi.redeal.databinding.RowTransactionBinding
import com.hifi.redeal.databinding.RowTransactionDepositBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class Transaction(
    private val transactionData: CustomTransactionData
) {

    val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())
    val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())

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
        recievedAmount: TextView,
        recievables: TextView
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
        recievedAmount.text =
            numberFormat.format(transactionData.transactionAmountReceived.toLong())
        recievables.text = numberFormat.format(
            (transactionData.transactionItemPrice.toLong() *
                    transactionData.transactionItemCount) -
                    transactionData.transactionAmountReceived.toLong()
        )
    }

}