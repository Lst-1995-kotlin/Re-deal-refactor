package com.hifi.redeal.trade.data.model

data class TradeClientData(
    val id: Int,
    val name: String,
    val managerName: String,
    var state: Long,
    val bookmark: Boolean
)
