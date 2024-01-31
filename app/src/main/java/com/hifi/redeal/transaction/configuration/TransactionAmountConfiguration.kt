package com.hifi.redeal.transaction.configuration

import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import com.hifi.redeal.transaction.util.TransactionNumberFormatUtil.replaceNumberFormat

enum class TransactionAmountConfiguration(private val value: Long) {
    MAX_TRANSACTION_AMOUNT(10_000_000_000)
    ;

    companion object {
        fun transactionAmountCheck(value: Long):Boolean {
            if (MAX_TRANSACTION_AMOUNT.value < value) return false
            return true
        }

        fun setTransactionAmountMessage(view: TextView) {
            view.text = "거래 당 1회 최대 금액은 ${replaceNumberFormat(MAX_TRANSACTION_AMOUNT.value)}원 입니다."
        }
    }
}