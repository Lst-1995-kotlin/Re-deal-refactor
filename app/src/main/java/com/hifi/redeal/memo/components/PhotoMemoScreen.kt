package com.hifi.redeal.memo.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.hifi.redeal.R
import com.hifi.redeal.memo.model.PhotoMemo
import com.hifi.redeal.memo.navigation.NavigationDestination
import com.hifi.redeal.memo.ui.DateText
import com.hifi.redeal.memo.ui.MemoTopAppBar
import com.hifi.redeal.memo.vm.PhotoMemoViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object PhotoMemoDestination : NavigationDestination {
    override val route = "client_photo_memo"
    override val titleRes = R.string.photo_memo_toolbar
    const val clientIdArg = "clientId"
    val routeWithArgs = "$route/{$clientIdArg}"
}

@Composable
fun PhotoMemoScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onFabClick: () -> Unit = {},
    onClickPhoto: (photoMemoUris: String, order:Int) -> Unit = {_, _ ->},
    viewModel: PhotoMemoViewModel = hiltViewModel(),
) {
    val photoMemoUiState by viewModel.photoMemosUiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
        topBar = {
            MemoTopAppBar(
                titleRes = PhotoMemoDestination.titleRes,
                canNavigateBack = true,
                onNavigationClick = onBackClick
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onFabClick,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        containerColor = Color.White
    ) { padding ->
        PhotoMemoColumn(
            photoMemos = photoMemoUiState.photoMemos,
            onClickPhoto = onClickPhoto,
            modifier = Modifier
                .padding(padding)
                .padding(vertical = 16.dp, horizontal = 20.dp),
        )

        // todo : 리스트 마지막 부분에 맨위로 가는 기능 등 처리 해주기
    }
}

@Composable
private fun PhotoMemoColumn(
    photoMemos: List<PhotoMemo>,
    modifier: Modifier = Modifier,
    onClickPhoto: (photoMemoUris: String, order:Int) -> Unit = {_, _ ->},
) {
    LazyColumn(
        content = {
            items(photoMemos) { photoMemo ->
                PhotoMemoItem(
                    photoMemo = photoMemo,
                    onClickPhoto = onClickPhoto,
                )
                Divider(Modifier.padding(vertical = 16.dp))
            }
        },
        modifier = modifier
    )
}

@Composable
private fun PhotoMemoItem(
    photoMemo: PhotoMemo,
    modifier: Modifier = Modifier,
    onClickPhoto: (photoMemoUris: String, order:Int) -> Unit = {_, _ ->}
) {
    Column(modifier) {
        DateText(
            timestamp = photoMemo.timestamp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp)
        )
        LazyRowImageList(
            imageUris = photoMemo.imageUris,
            onClickPhoto = onClickPhoto,
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
        )
        MemoMultiText(modifier = Modifier.padding(top = 20.dp, start = 4.dp), text = photoMemo.memo)
    }
}

@Composable
private fun LazyRowImageList(
    imageUris: List<String>,
    modifier: Modifier = Modifier,
    onClickPhoto: (photoMemoUris: String, order:Int) -> Unit = {_, _ ->}
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 2.dp),
        content = {
            itemsIndexed(imageUris) { idx, uri ->
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(shape = RoundedCornerShape(8.dp))
                        .clickable {
                            val encodedUrls = imageUris.map {
                                URLEncoder.encode(
                                    it,
                                    StandardCharsets.UTF_8.toString()
                                )
                            }
                            onClickPhoto(encodedUrls.joinToString(separator = ","), idx)
                        }
                )
            }
        },
        modifier = modifier
    )
}

@Composable
private fun MemoMultiText(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier
    )
}