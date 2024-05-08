package com.hifi.redeal.trade.data.model

import com.hifi.redeal.data.entrie.TradeEntity
import java.util.Date


data class TradeData(
    val id: Int,
    val itemName: String,
    val itemCount: Long,
    val itemPrice: Long,
    val receivedAmount: Long,
    val type: Int,
    val date: Date,
    val clientId: Int,
    val clientName: String,
    val managerName: String
)

fun TradeData.toTradeEntry(): TradeEntity {
    return TradeEntity(
        this.id,
        this.itemName,
        this.itemCount,
        this.itemPrice,
        this.receivedAmount,
        this.type,
        this.date,
        false,
        this.clientId
    )
}
