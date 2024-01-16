package com.hifi.redeal.memo.model

import java.util.Date
import javax.inject.Inject

data class PhotoMemoData @Inject constructor(
    val context:String,
    val date: Date,
    val srcArr:List<String>,
    val clientIdx:Long = -1
)

enum class BottomButtonState {
    IDLE,
    PRESSED,
    DISABLED
}