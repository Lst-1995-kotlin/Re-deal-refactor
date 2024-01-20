package com.hifi.redeal.transaction.util

import java.text.DecimalFormat

object TransactionNumberFormatUtil {
    private val numberFormat = DecimalFormat("#,###")

    fun replaceNumberFormat(value: Long): String{
        return numberFormat.format(value)
    }

    fun replaceNumberFormat(value: Int): String{
        return numberFormat.format(value)
    }

    fun removeNumberFormat(value: String): Long{
        return value.replace(",","").toLong()
    }
}