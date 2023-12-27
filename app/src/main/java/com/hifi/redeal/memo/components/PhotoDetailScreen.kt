package com.hifi.redeal.memo.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.hifi.redeal.MainActivity
import com.hifi.redeal.R
import com.hifi.redeal.memo.repository.PhotoMemoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyAppToolbar(
    title: String,
    modifier: Modifier = Modifier,
    mainActivity: MainActivity
) {
    Box {
        TopAppBar(
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    mainActivity.removeFragment(MainActivity.PHOTO_MEMO_FRAGMENT)
                }) {
                    Icon(
                        painter = painterResource(R.drawable.arrow_back_ios_24px),
                        contentDescription = null,
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,
                navigationIconContentColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.primary
            ),
            modifier = modifier
        )

        Divider(
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.align(Alignment.BottomStart)
        )
    }
}

@OptIn(ExperimentalCoilApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun MainImage(
    src: String,
    repository: PhotoMemoRepository,
    onClick:(isLeft:Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var imageUrl by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        imageUrl = withContext(Dispatchers.IO) {
            repository.getPhotoMemoImgUrlToCoroutine(src)
        }
    }
    val painter = if (imageUrl == "")
        painterResource(id = R.drawable.empty_photo) else
        rememberImagePainter(imageUrl)

    val backgroundColor = Color(240, 240, 240)
    val dismissState = rememberDismissState(confirmValueChange = { dismissValue ->
        when (dismissValue) {
            DismissValue.Default -> { // dismissThresholds 만족 안한 상태
                false
            }
            DismissValue.DismissedToEnd -> { // -> 방향 스와이프 (수정)
                onClick(false)
                false
            }
            DismissValue.DismissedToStart -> { // <- 방향 스와이프 (삭제)
                onClick(true)
                true
            }
        }
    })

    SwipeToDismiss(
        state = dismissState,
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp),
        dismissContent = { // content
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
            )
        },
        background = { // dismiss content
            val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    DismissValue.Default -> backgroundColor.copy(alpha = 0.5f) // dismissThresholds 만족 안한 상태
                    DismissValue.DismissedToEnd -> Color.Green.copy(alpha = 0.4f) // -> 방향 스와이프 (수정)
                    DismissValue.DismissedToStart -> Color.Red.copy(alpha = 0.5f) // <- 방향 스와이프 (삭제)
                }, label = ""
            )
            val icon = when (dismissState.targetValue) {
                DismissValue.Default -> Icons.Default.Circle
                DismissValue.DismissedToEnd -> Icons.Default.Edit
                DismissValue.DismissedToStart -> Icons.Default.Delete
            }
            val scale by animateFloatAsState(
                when (dismissState.targetValue == DismissValue.Default) {
                    true -> 0.8f
                    else -> 1.5f
                }, label = ""
            )
            val alignment = when (direction) {
                DismissDirection.EndToStart -> Alignment.CenterEnd
                DismissDirection.StartToEnd -> Alignment.CenterStart
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 30.dp),
                contentAlignment = alignment
            ) {
                Icon(
                    modifier = Modifier.scale(scale),
                    imageVector = icon,
                    contentDescription = null
                )
            }
        }
    )
}

@OptIn(ExperimentalCoilApi::class)
@Composable
private fun BottomImageList(
    srcArr: List<String>,
    repository: PhotoMemoRepository,
    onClick: (order: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(content = {
        itemsIndexed(srcArr) { idx, src ->
            var imageUrl by remember { mutableStateOf("") }
            LaunchedEffect(Unit) {
                imageUrl = withContext(Dispatchers.IO) {
                    repository.getPhotoMemoImgUrlToCoroutine(src)
                }
            }
            val painter = if (imageUrl == "")
                painterResource(id = R.drawable.empty_photo) else
                rememberImagePainter(imageUrl)
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clickable {
                        onClick(idx)
                    }
            )
        }
    })
}

@Composable
fun PhotoDetailScreen(
    imgOrder: Int,
    imgSrcArr: List<String>,
    repository: PhotoMemoRepository,
    mainActivity: MainActivity
) {
    var order by remember {
        mutableIntStateOf(imgOrder)
    }

    Scaffold(
        topBar = {
            MyAppToolbar(
                title = "상세 이미지", mainActivity = mainActivity
            )
        }
    ) {
            padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {
            MainImage(
                src = imgSrcArr[order],
                repository = repository,
                onClick= {isLeft ->
                    if(isLeft){
                        if(order - 1 > 0) {
                            order--
                        }
                    }else{
                        if(order + 1 < imgSrcArr.size) {
                            order++
                        }
                    }
                }
            )

            BottomImageList(
                srcArr = imgSrcArr,
                repository = repository,
                onClick = {
                    order = it
                }
            )
        }
    }
}

