package com.hifi.redeal.memo.utils

import android.content.ContentValues
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

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

fun intervalBetweenDateText(timestamp: Long): String {
    val beforeFormat =
        LocalDateTime.ofInstant(
            java.time.Instant.ofEpochMilli(timestamp),
            ZoneId.systemDefault()
        )
    val nowFormat = LocalDateTime.now()

    val units = arrayOf("시간", "분")
    val amounts = arrayOf(
        ChronoUnit.DAYS.between(beforeFormat, nowFormat),
        ChronoUnit.HOURS.between(beforeFormat, nowFormat),
        ChronoUnit.MINUTES.between(beforeFormat, nowFormat)
    )

    for (i in amounts.indices) {
        if (amounts[i] > 0) {
            return if (i == 0) {
                beforeFormat.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
            } else
                "${amounts[i]}${units[i - 1]} 전"
        }
    }

    return "방금"
}

fun formatRecordTime(time: Long): String {
    val ms = time / 10
    val sec = ms / 100
    val min = sec / 60
    val hours = min / 60
    val milliSeconds = ms % 100
    val seconds = sec % 60
    val minutes = min % 60

    val hoursString = if (hours > 0) {
        "$hours:"
    } else {
        ""
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

    val milliSecondsString = if (milliSeconds < 10) {
        "0$milliSeconds"
    } else {
        milliSeconds.toString()
    }

    return "$hoursString$minutesString:$secondsString.$milliSecondsString"
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

fun createAudioUri(context: Context): Uri? {
    val values = ContentValues()
    values.put(
        MediaStore.MediaColumns.DISPLAY_NAME,
        "음성_${System.currentTimeMillis()}"
    )
    values.put(
        MediaStore.MediaColumns.MIME_TYPE,
        "audio/3gpp"
    )
    values.put(
        MediaStore.MediaColumns.RELATIVE_PATH,
        Environment.DIRECTORY_MUSIC + "/RecordRedeal"
    )

    return context.contentResolver.insert(
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        values
    )
}

fun getUriForFile(context: Context, fileName: String): Uri? {
    val contentResolver = context.contentResolver
    val collection = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

    val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.DISPLAY_NAME
    )

    val selection = "${MediaStore.Audio.Media.DISPLAY_NAME} = ?"
    val selectionArgs = arrayOf(fileName)

    val cursor = contentResolver.query(
        collection,
        projection,
        selection,
        selectionArgs,
        null
    )

    var uri: Uri? = null
    if (cursor != null && cursor.moveToFirst()) {
        val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
        uri = Uri.withAppendedPath(collection, id.toString())
    }

    cursor?.close()

    return uri
}