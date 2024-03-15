package com.hifi.redeal.trade.data.model

import com.google.firebase.Timestamp


data class TransactionData(
    val clientIdx: Long,
    val date: Timestamp,
    @JvmField
    val isDeposit: Boolean,
    val transactionAmountReceived: Long,
    val transactionIdx: Long,
    val transactionItemCount: Long,
    val transactionItemPrice: Long,
    val transactionItemName: String,
)


data class SelectTransactionData(
    val isSelected: Boolean
)
