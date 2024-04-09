package com.hifi.redeal.data.entrie

import androidx.room.Entity
import androidx.room.PrimaryKey

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