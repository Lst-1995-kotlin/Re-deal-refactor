package com.hifi.redeal.memo.components

import android.os.Bundle
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.hifi.redeal.MainActivity
import com.hifi.redeal.R
import com.hifi.redeal.memo.model.PhotoMemo
import com.hifi.redeal.memo.repository.PhotoMemoRepository
import com.hifi.redeal.memo.ui.MemoTopAppBar
import com.hifi.redeal.memo.utils.intervalBetweenDateText
import com.hifi.redeal.memo.vm.PhotoMemoViewModel
import java.util.Date

@Composable
fun PhotoMemoScreen(
    photoMemoViewModel: PhotoMemoViewModel,
    repository: PhotoMemoRepository,
    mainActivity: MainActivity,
    clientIdx: Long,
    modifier: Modifier = Modifier
) {
    val photoMemoDataList by photoMemoViewModel.photoMemoList.observeAsState()
    Scaffold(
        modifier = modifier,
        topBar = {
            MemoTopAppBar(
                titleRes = R.string.photo_memo_toolbar,
                canNavigateBack = true,
                onNavigationClick = {
                    mainActivity.removeFragment(MainActivity.PHOTO_MEMO_FRAGMENT)
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val newBundle = Bundle()
                    newBundle.putLong("clientIdx", clientIdx)
                    mainActivity.replaceFragment(
                        MainActivity.ADD_PHOTO_MEMO_FRAGMENT,
                        true,
                        newBundle
                    )
                },
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
        photoMemoDataList?.let {
            PhotoMemoList(
                photoMemoItemList = photoMemoDataList!!,
                mainActivity = mainActivity,
                repository = repository,
                modifier = Modifier
                    .padding(padding)
                    .padding(vertical = 16.dp, horizontal = 20.dp),
            )
        }

        // todo : 리스트 마지막 부분에 맨위로 가는 기능 등 처리 해주기
    }
}

@Composable
private fun PhotoMemoList(
    photoMemoItemList: List<PhotoMemo>,
    mainActivity: MainActivity,
    repository: PhotoMemoRepository,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        content = {
            items(photoMemoItemList) { item ->
                PhotoMemoItem(
                    item = item,
                    mainActivity = mainActivity,
                    repository = repository
                )
                Divider(Modifier.padding(vertical = 16.dp))
            }
        },
        modifier = modifier
    )
}

@Composable
private fun PhotoMemoItem(
    item: PhotoMemo,
    mainActivity: MainActivity,
    modifier: Modifier = Modifier,
    repository: PhotoMemoRepository
) {
    Column(modifier) {
        DateText(
            date = item.date,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp)
        )
        LazyRowImageList(
            srcArr = item.imagePaths,
            repository = repository,
            mainActivity = mainActivity,
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
        )
        MemoMultiText(modifier = Modifier.padding(top = 20.dp, start = 4.dp), text = item.memo)
    }
}

@Composable
private fun DateText(
    date: Date,
    modifier: Modifier = Modifier
) {
    Text(
        text = intervalBetweenDateText(date),
        style = MaterialTheme.typography.bodySmall.copy(
            fontWeight = FontWeight.Bold
        ),
        modifier = modifier
    )
}

@Composable
private fun LazyRowImageList(
    srcArr: List<String>,
    repository: PhotoMemoRepository,
    mainActivity: MainActivity,
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 2.dp),
        content = {
            itemsIndexed(srcArr) { idx, src ->
                var imageUrl by remember { mutableStateOf("") }
                val coroutineScope = rememberCoroutineScope()
                LaunchedEffect(coroutineScope) {
                    imageUrl = repository.getPhotoMemoImgUrlToCoroutine(src)
                }
                val painter = if (imageUrl == "")
                    painterResource(id = R.drawable.empty_photo) else
                    rememberAsyncImagePainter(imageUrl)
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(shape = RoundedCornerShape(8.dp))
                        .clickable {
                            val newBundle = Bundle()
                            newBundle.putInt("order", idx)
                            newBundle.putStringArrayList(
                                "srcArr",
                                srcArr as ArrayList<String>
                            )
                            mainActivity.replaceFragment(
                                MainActivity.PHOTO_DETAIL_FRAGMENT,
                                true,
                                newBundle
                            )
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