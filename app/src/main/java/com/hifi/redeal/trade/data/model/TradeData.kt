package com.hifi.redeal.trade.data.model

import com.hifi.redeal.data.entrie.TradeEntry
import java.util.Date


data class TradeData(
    val id: Int,
    val itemName: String,
    val itemCount: Long,
    val itemPrice: Long,
    val receivedAmount: Long,
    val type: Int,
    val date: Date,
    val checked: Boolean,
    val clientId: Int,
    val clientName: String
)

fun TradeData.toTradeEntry(): TradeEntry {
    return TradeEntry(
        this.id,
        this.itemName,
        this.itemCount,
        this.itemPrice,
        this.receivedAmount,
        this.type,
        this.date,
        this.checked,
        this.clientId
    )
}
