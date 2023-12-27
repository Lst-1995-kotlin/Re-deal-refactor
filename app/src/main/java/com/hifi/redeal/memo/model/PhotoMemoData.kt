package com.hifi.redeal.memo.model

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import com.hifi.redeal.R
import com.hifi.redeal.memo.repository.PhotoMemoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class PhotoMemoData @Inject constructor(
    val context:String,
    val date: Timestamp,
    val srcArr:List<String>,
    val clientIdx:Long = -1
)