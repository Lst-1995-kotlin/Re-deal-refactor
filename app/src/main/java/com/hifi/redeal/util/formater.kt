package com.hifi.redeal.util

import java.text.DecimalFormat
import android.icu.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val numberFormat = DecimalFormat("#,###")

// 사용자 설정 언어에 따라 시간 표기가 변경되도록 설정.
private lateinit var dateYearOfDayFormat: SimpleDateFormat
fun Int.toNumberFormat(): String {
    return numberFormat.format(this)
}

fun Long.toNumberFormat(): String {
    return numberFormat.format(this)
}

fun String.numberFormatToLong(): Long {
    val text = this.replace(",", "").trim()
    return when {
        text.isEmpty() -> 0L
        !text.all { it.isDigit() } -> throw NumberFormatException("${text}(은)는 숫자 포맷 으로 변경할 수 없습니다.")
        else -> text.toLong()
    }
}

fun Date.toDateYearOfDayFormat(): String {
    dateYearOfDayFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
    return dateYearOfDayFormat.format(this.time)
}