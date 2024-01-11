package com.hifi.redeal.memo.model

import android.net.Uri
import com.google.firebase.Timestamp
data class UserRecordMemoData(val clientIdx:Long, val context:String, val date:Timestamp, val audioSrc:Uri?, val audioFilename:String)
data class RecordMemoData(val context:String, val date:Timestamp, val audioFileUri:Uri?, val audioFilename:String)

enum class PlayState {
    PLAYING,
    PAUSE,
    STOP
}