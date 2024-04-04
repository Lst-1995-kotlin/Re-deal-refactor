package com.hifi.redeal.data.entrie

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photoMemos")
data class PhotoMemoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val memo:String,
    val timestamp: Long,
    @ColumnInfo(defaultValue = "")
    val imagePaths:List<String>
)