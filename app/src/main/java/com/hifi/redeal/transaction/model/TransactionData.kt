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
    val transactionName: String,
)

data class ClientData(
    var clientAddress: String? = null,
    var clientCeoPhone: String? = null,
    var clientDetailAdd: String? = null,
    var clientExplain: String? = null,
    var clientFaxNumber: String? = null,
    var clientIdx: Long? = null,
    var clientManagerName: String? = null,
    var clientManagerPhone: String? = null,
    var clientMemo: String? = null,
    var clientName: String? = null,
    var clientState: Long? = null,
    @JvmField
    var isBookmark: Boolean? = null,
    var photoMemoIdxList: List<Long>? = null,
    var recordMemoIdxList: List<Long>? = null,
    var transactionIdxList: List<Long>? = null,
    var viewCount: Long? = null,
)

data class ClientSimpleData(
    val clientIdx: Long,
    val clientName: String,
    val clientManagerName: String,
    var clientState: Long,
    @JvmField val isBookmark: Boolean,
)