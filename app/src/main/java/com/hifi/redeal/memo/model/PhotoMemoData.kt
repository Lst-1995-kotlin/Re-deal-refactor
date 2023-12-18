package com.hifi.redeal.memo.model

import com.google.firebase.Timestamp
import javax.inject.Inject

data class PhotoMemoData @Inject constructor(
    val context:String,
    val date: Timestamp,
    val srcArr:List<Any>,
    val clientIdx:Long = -1,
)