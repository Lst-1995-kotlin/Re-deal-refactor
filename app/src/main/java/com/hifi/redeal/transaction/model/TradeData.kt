package com.hifi.redeal.transaction.model

import com.hifi.redeal.data.entrie.TradeEntry
import java.util.Date


data class TradeData (
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
)

fun TradeData.toTradeEntry(): TradeEntry {
    return TradeEntry(
        this.id,
        this.itemName,
        this.itemCount,
        this.itemPrice,
        this.receivedAmount,
        this.type,
        this.totalItemAmount,
        this.receivables,
        this.date,
        this.clientId
    )
}
