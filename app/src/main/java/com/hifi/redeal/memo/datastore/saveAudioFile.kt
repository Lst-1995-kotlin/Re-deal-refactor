package com.hifi.redeal.memo.datastore

import android.content.Context
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream

fun saveAudioFile(context: Context, audioUri: String): String {
    val inputStream = context.contentResolver.openInputStream(audioUri.toUri())

    val audioDirectory = File(context.filesDir, "audio")
    if (!audioDirectory.exists()) {
        audioDirectory.mkdirs()
    }

    val fileName = "audio_${System.currentTimeMillis()}.mp4"
    val file = File(audioDirectory, fileName)

    try {
        FileOutputStream(file).use { fos ->
            inputStream?.copyTo(fos)
            fos.flush()
        }
        return file.toUri().toString()
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        inputStream?.close()
    }

    return ""
}
