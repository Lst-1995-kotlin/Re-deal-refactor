package com.hifi.redeal.trade.configuration

enum class TradeAmountConfiguration(private val value: Long) {
    MAX_TRANSACTION_AMOUNT(10_000_000_000)
    ;

    companion object {
        fun transactionAmountCheck(value: Long): Boolean {
            return MAX_TRANSACTION_AMOUNT.value >= value
        }
    }
}