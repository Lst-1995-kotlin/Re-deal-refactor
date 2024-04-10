package com.hifi.redeal.data.entrie

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hifi.redeal.memo.model.RecordMemo

@Entity(tableName = "recordMemos")
data class RecordMemoEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val clientOwnerId:Int,
    val memo:String,
    val timestamp:Long,
    val duration:Long,
    val audioFilename:String,
    val audioFileUri:String
)

fun RecordMemoEntity.asExternalModel() = RecordMemo(
    id = id,
    clientOwnerId = clientOwnerId,
    memo = memo,
    timestamp = timestamp,
    duration = duration,
    audioFilename = audioFilename,
    audioFileUri = audioFileUri
)