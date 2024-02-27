package com.hifi.redeal.memo.record

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import java.io.File
import java.io.FileOutputStream

class VoiceMemoRecorder(
    private val context: Context
) {

    private var mediaRecorder: MediaRecorder? = null
    var isPaused: Boolean = false
    private fun initialRecorder(): MediaRecorder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else MediaRecorder()
    }

    fun start(outputFile: File) {
        initialRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(FileOutputStream(outputFile).fd)
            prepare()
            start()
            mediaRecorder = this
        }
    }

    fun pause() {
        mediaRecorder?.pause()
    }

    fun resume() {
        mediaRecorder?.resume()
    }

    fun togglePause() {
        if (!isPaused) {
            mediaRecorder?.pause()
        } else {
            mediaRecorder?.resume()
        }
        isPaused = !isPaused
    }

    fun stop() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
    }
}