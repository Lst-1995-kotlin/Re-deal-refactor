package com.hifi.redeal.trade.configuration

import android.widget.TextView
import com.hifi.redeal.trade.util.TransactionNumberFormatUtil.replaceNumberFormat

enum class TransactionAmountConfiguration(private val value: Long) {
    MAX_TRANSACTION_AMOUNT(10_000_000_000)
    ;

    companion object {
        fun transactionAmountCheck(value: Long): Boolean {
            return MAX_TRANSACTION_AMOUNT.value >= value
        }

        fun setTransactionAmountMessage(view: TextView) {
            view.text = "거래 당 1회 최대 금액은 ${replaceNumberFormat(MAX_TRANSACTION_AMOUNT.value)}원 입니다."
        }
    }
}