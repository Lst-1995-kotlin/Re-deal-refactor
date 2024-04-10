package com.hifi.redeal.memo.model

import com.hifi.redeal.data.entrie.PhotoMemoEntity

data class PhotoMemo (
    val id:Int = 0,
    val clientOwnerId:Int = 0,
    val memo:String = "",
    val timestamp: Long = 0,
    val imageUris:List<String> = emptyList()
)

fun PhotoMemo.toPhotoMemoEntity() = PhotoMemoEntity(
    id = id,
    clientOwnerId = clientOwnerId,
    memo = memo,
    timestamp = timestamp,
    imageUris = imageUris
)