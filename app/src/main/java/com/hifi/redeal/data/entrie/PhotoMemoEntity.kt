package com.hifi.redeal.data.entrie

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hifi.redeal.memo.model.PhotoMemo

@Entity(tableName = "photoMemos")
data class PhotoMemoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val clientOwnerId:Int,
    val memo:String,
    val timestamp: Long,
    @ColumnInfo(defaultValue = "")
    val imageUris:List<String>
)

fun PhotoMemoEntity.asExternalModel() = PhotoMemo(
    id = id,
    clientOwnerId = clientOwnerId,
    memo = memo,
    timestamp = timestamp,
    imageUris = imageUris
)