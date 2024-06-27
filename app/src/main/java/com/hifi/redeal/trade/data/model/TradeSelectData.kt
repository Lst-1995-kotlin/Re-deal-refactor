package com.hifi.redeal.trade.data.model

import com.hifi.redeal.data.entrie.TradeEntity
import java.util.Date

data class TradeSelectData(
    val id: Int,
    val itemName: String,
    val itemCount: Long,
    val itemPrice: Long,
    val receivedAmount: Long,
    val type: Int,
    val date: Date,
    val checked: Boolean,
    val clientId: Int,
    val clientName: String,
    val managerName: String
)

fun TradeSelectData.toCheckChange(): TradeSelectData {
    return TradeSelectData(
        this.id,
        this.itemName,
        this.itemCount,
        this.itemPrice,
        this.receivedAmount,
        this.type,
        this.date,
        !this.checked,
        this.clientId,
        this.clientName,
        this.managerName
    )
}
fun TradeSelectData.toTradeEntry(): TradeEntity {
    return TradeEntity(
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