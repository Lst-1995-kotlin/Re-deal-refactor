package com.hifi.redeal.memo.model

import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import com.hifi.redeal.memo.repository.PhotoMemoRepository
import javax.inject.Inject

data class PhotoMemoData @Inject constructor(
    val context:String,
    val date: Timestamp,
    val srcArr:List<String>,
    val clientIdx:Long = -1
)