package com.hifi.redeal.memo.ui.photoMemo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.hifi.redeal.R
import com.hifi.redeal.memo.navigation.NavigationDestination
import com.hifi.redeal.memo.ui.MemoTopAppBar
import com.hifi.redeal.theme.RedealTheme
import kotlinx.coroutines.launch

object PhotoDetailDestination : NavigationDestination {
    override val route = "photo_memo_detail"
    override val titleRes = R.string.photo_detail_toolbar
    const val imageUris = "imageUris"
    const val initialOrder = "initialOrder"
    val routeWithArgs = "$route/{$imageUris}/{$initialOrder}"
}

@OptIn(ExperimentalPagerApi::class)
@Composable
internal fun PhotoDetailScreen(
    modifier: Modifier = Modifier,
    imageUris: List<String> = emptyList(),
    initialOrder: Int = 0,
    onBackClick: () -> Unit = {}
) {
    val pagerState = rememberPagerState(initialPage = initialOrder)
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            MemoTopAppBar(
                titleRes = PhotoDetailDestination.titleRes,
                canNavigateBack = true,
                onNavigationClick = onBackClick
            )
        },
        modifier = modifier
    ) { padding ->
        PhotoPager(
            modifier = Modifier.padding(padding),
            pagerState = pagerState,
            imageUris = imageUris,
            onClickBottomImage = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(page = it)
                }
            }
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun PhotoPager(
    pagerState: PagerState,
    imageUris: List<String>,
    onClickBottomImage: (order: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        HorizontalPager(
            count = imageUris.size,
            state = pagerState,
            itemSpacing = 16.dp,
            verticalAlignment = Alignment.CenterVertically,
            contentPadding = PaddingValues(vertical = 16.dp),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.White)
        ) { page ->
            Image(
                painter = rememberAsyncImagePainter(imageUris[page]),
                contentDescription = "$page",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
            )
        }

        LazyRow(
            content = {
                itemsIndexed(imageUris) { idx, src ->
                    Image(
                        painter = rememberAsyncImagePainter(src),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(60.dp)
                            .clickable {
                                onClickBottomImage(idx)
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
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        )
    }
}
@Preview("Photo Detail Screen")
@Composable
private fun PhotoDetailScreenPreview(){
    RedealTheme {
        PhotoDetailScreen()
    }
}

