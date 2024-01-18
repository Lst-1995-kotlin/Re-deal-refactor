package com.hifi.redeal.memo.utils

import android.content.Context
import android.media.MediaPlayer
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date

fun dpToPx(context: Context, dp: Int): Int {
    val scale = context.resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

fun getTotalDuration(mediaPlayer: MediaPlayer?): String {
    val totalDuration = mediaPlayer?.duration ?: 0

    val minutes = (totalDuration / 1000) / 60
    val seconds = (totalDuration / 1000) % 60

    return String.format("%02d:%02d", minutes, seconds)
}

fun getCurrentDuration(currentPosition: Int): String {
    val minutes = (currentPosition / 1000) / 60
    val seconds = (currentPosition / 1000) % 60

    return String.format("%02d:%02d", minutes, seconds)
}

fun intervalBetweenDateText(date: Date): String {
    val beforeFormat = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
    val nowFormat = LocalDateTime.now()

    val units = arrayOf("시간", "분")
    val amounts = arrayOf(
        ChronoUnit.DAYS.between(beforeFormat, nowFormat),
        ChronoUnit.HOURS.between(beforeFormat, nowFormat),
        ChronoUnit.MINUTES.between(beforeFormat, nowFormat)
    )

    for (i in amounts.indices) {
        if (amounts[i] > 0) {
            return if(i == 0){
                beforeFormat.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
            }else
                "${amounts[i]}${units[i-1]} 전"
        }
    }

    return "방금"
}

fun Long.convertToDurationTime(): String {
    val sec = this / 1000
    val min = sec / 60
    val hours = min / 60
    val seconds = sec % 60
    val minutes = min % 60

    val hoursString = if (hours < 10) {
        "0$hours"
    } else {
        hours.toString()
    }
    val minutesString = if (minutes < 10) {
        "0$minutes"
    } else {
        minutes.toString()
    }
    val secondsString = if (seconds < 10) {
        "0$seconds"
    } else {
        seconds.toString()
    }
    return "$hoursString:$minutesString:$secondsString"
}