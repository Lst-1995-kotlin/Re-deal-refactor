package com.hifi.redeal.transaction.model

import com.google.firebase.Timestamp

data class TransactionData(
    val clientIdx: Long,
    val date: Timestamp,
    @JvmField
    val isDeposit: Boolean,
    val transactionAmountReceived: String,
    val transactionIdx: Long,
    val transactionItemCount: Long,
    val transactionItemPrice: String,
    val transactionItemName: String,
)

data class ClientData(
    val clientIdx: Long,
    val clientName: String,
    val clientManagerName: String,
    var clientState: Long,
    @JvmField val isBookmark: Boolean,
)