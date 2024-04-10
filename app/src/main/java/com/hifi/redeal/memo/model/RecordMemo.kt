package com.hifi.redeal.memo.model

import com.hifi.redeal.data.entrie.RecordMemoEntity

data class RecordMemo(
    val id: Int = 0,
    val clientOwnerId: Int = 0,
    val memo: String = "",
    val timestamp: Long = 0L,
    val duration: Long = 0L,
    val audioFilename: String = "",
    val audioFileUri: String = ""
)

fun RecordMemo.toRecordMemoEntity() = RecordMemoEntity(
    id = id,
    clientOwnerId = clientOwnerId,
    memo = memo,
    timestamp = timestamp,
    duration = duration,
    audioFilename = audioFilename,
    audioFileUri = audioFileUri
)