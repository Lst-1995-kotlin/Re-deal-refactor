package com.hifi.redeal.trade.configuration

enum class TradeAmountConfiguration(private val value: Long) {
    MAX_TRADE_AMOUNT(10_000_000_000)
    ;

    companion object {
        fun tradeAmountCheck(value: Long): Boolean {
            return MAX_TRADE_AMOUNT.value >= value
        }
    }
}