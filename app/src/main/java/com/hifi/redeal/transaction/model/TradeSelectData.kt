package com.hifi.redeal.transaction.model

import java.util.Date

data class TradeSelectData(
    val id: Int,
    val itemName: String,
    val itemCount: String,
    val itemPrice: String,
    val receivedAmount: String,
    val type: Boolean,
    val totalItemAmount: Long,
    val receivables: Long,
    val date: Date,
    val clientId: Int,
    val clientName: String,
    val clientManagerName: String,
    val isSelected: Boolean
    )
