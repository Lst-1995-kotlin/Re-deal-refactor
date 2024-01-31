package com.hifi.redeal.memo.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.hifi.redeal.MainActivity
import com.hifi.redeal.R
import com.hifi.redeal.memo.repository.PhotoMemoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PhotoDetailToolbar(
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
                    mainActivity.removeFragment(MainActivity.PHOTO_DETAIL_FRAGMENT)
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

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun ImageSlider(
    pagerState: PagerState,
    images: List<String>,
    repository: PhotoMemoRepository,
    modifier: Modifier = Modifier
) {
    HorizontalPager(
        count = images.size,
        state = pagerState,
        itemSpacing = 16.dp,
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(vertical = 16.dp),
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
    ) { page ->
        var imageUrl by remember { mutableStateOf("") }
        LaunchedEffect(Unit) {
            imageUrl = withContext(Dispatchers.IO) {
                repository.getPhotoMemoImgUrlToCoroutine(images[page])
            }
        }

        val painter = if (imageUrl == "")
            painterResource(id = R.drawable.empty_photo) else
            rememberAsyncImagePainter(imageUrl)
        Image(
            painter = painter,
            contentDescription = "$page",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun BottomImageList(
    pagerState: PagerState,
    srcArr: List<String>,
    repository: PhotoMemoRepository,
    onClick: (order: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        content = {
            itemsIndexed(srcArr) { idx, src ->
                var imageUrl by remember { mutableStateOf("") }
                LaunchedEffect(Unit) {
                    imageUrl = withContext(Dispatchers.IO) {
                        repository.getPhotoMemoImgUrlToCoroutine(src)
                    }
                }
                val painter = if (imageUrl == "")
                    painterResource(id = R.drawable.empty_photo) else
                    rememberAsyncImagePainter(imageUrl)
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(60.dp)
                        .clickable {
                            onClick(idx)
                        }
                        .border(
                            width = if (pagerState.currentPage == idx) 4.dp else 0.dp,
                            MaterialTheme.colorScheme.primary
                        )
                )
            }
        },
        contentPadding = PaddingValues(20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PhotoDetailScreen(
    imgOrder: Int,
    imgSrcArr: List<String>,
    repository: PhotoMemoRepository,
    mainActivity: MainActivity
) {
    val pagerState = rememberPagerState(initialPage = imgOrder)
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            PhotoDetailToolbar(
                title = stringResource(id = R.string.photo_memo_detail_toolbar),
                mainActivity = mainActivity
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {
            ImageSlider(
                pagerState = pagerState,
                images = imgSrcArr,
                repository = repository,
                modifier = Modifier.weight(1f)
            )

            BottomImageList(
                pagerState = pagerState,
                srcArr = imgSrcArr,
                repository = repository,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(page = it)
                    }
                }
            )
        }
    }
}

