package com.hifi.redeal.memo.components

import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.hifi.redeal.MainActivity
import com.hifi.redeal.R
import com.hifi.redeal.memo.model.PhotoMemoData
import com.hifi.redeal.memo.repository.PhotoMemoRepository
import com.hifi.redeal.memo.utils.intervalBetweenDateText
import com.hifi.redeal.memo.vm.PhotoMemoViewModel
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PhotoMemoToolbar(
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
private fun PhotoMemoItem(
    item: PhotoMemoData,
    mainActivity: MainActivity,
    modifier: Modifier = Modifier,
    repository: PhotoMemoRepository
) {
    Column(modifier) {
        DateText(
            date = item.date.toDate(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp)
        )
        LazyRowImageList(
            srcArr = item.srcArr,
            repository = repository,
            mainActivity = mainActivity,
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
        )
        MemoMultiText(modifier = Modifier.padding(top = 20.dp, start = 4.dp), text = item.context)
    }
}

@Composable
private fun PhotoMemoList(
    photoMemoItemList: List<PhotoMemoData>,
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
fun PhotoMemoScreen(
    photoMemoViewModel: PhotoMemoViewModel,
    repository: PhotoMemoRepository,
    mainActivity: MainActivity,
    clientIdx: Long
) {
    val photoMemoDataList by photoMemoViewModel.photoMemoList.observeAsState()
    Scaffold(
        topBar = {
            PhotoMemoToolbar(
                title = stringResource(id = R.string.photo_memo_toolbar),
                mainActivity = mainActivity
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
