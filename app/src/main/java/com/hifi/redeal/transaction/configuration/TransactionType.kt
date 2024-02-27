package com.hifi.redeal.transaction.configuration

enum class TransactionType(val type: Int) {
    ERROR(0),
    DEPOSIT(1),
    SALES(2),
    COUNT(3)
}
