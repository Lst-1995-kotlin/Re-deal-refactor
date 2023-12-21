package com.hifi.redeal.memo.components

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.hifi.redeal.memo.model.PhotoMemoData
import com.hifi.redeal.memo.repository.PhotoMemoRepository
import com.hifi.redeal.memo.utils.intervalBetweenDateText
import com.hifi.redeal.memo.vm.PhotoMemoViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat

@Composable
fun MemoBox(
    text: String = "새로운 메모",
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = Color.White
    )
    {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp))
                .padding(12.dp)
                .fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun PhotoMemoItem(
    item: PhotoMemoData,
    modifier: Modifier = Modifier,
    repository: PhotoMemoRepository
) {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    Column(modifier) {
        Text(
            text = intervalBetweenDateText(dateFormat.format(item.date.toDate())),
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.ExtraBold
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // 변경 : 기존 nx3 행 -> 1행
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 2.dp),
            content = {
                items(item.srcArr) { src ->
                    var imageUrl by remember { mutableStateOf("") }
                    LaunchedEffect(Unit) {
                        imageUrl = withContext(Dispatchers.IO) {
                            repository.getPhotoMemoImgUrlToCoroutine(src)
                        }
                    }
                    var painter = if (imageUrl == "")
                        painterResource(id = R.drawable.empty_photo) else
                        rememberImagePainter(imageUrl)
                    Image(
                        painter = painter,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(100.dp)
                            .clickable {
                                // todo : 이미지 클릭 처리
                            }
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
        MemoBox(item.context, modifier = Modifier.padding(top = 6.dp))
    }
}

@Composable
fun PhotoMemoList(
    photoMemoItemList: List<PhotoMemoData>,
    modifier: Modifier = Modifier,
    repository: PhotoMemoRepository
) {
    LazyColumn(
        content = {
            items(photoMemoItemList) { item ->
                PhotoMemoItem(item = item, Modifier.padding(horizontal = 24.dp), repository)
                Divider(Modifier.padding(vertical = 16.dp))
            }
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAppToolbar(
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
fun PhotoMemoScreen(
    photoMemoViewModel: PhotoMemoViewModel,
    repository: PhotoMemoRepository,
    mainActivity: MainActivity
) {
    val photoMemoDataList by photoMemoViewModel.photoMemoList.observeAsState()

    Scaffold(
        topBar = { MyAppToolbar(title = "포토 메모", mainActivity = mainActivity) },
        containerColor = Color.White
    ) { padding ->
        photoMemoDataList?.let {
            PhotoMemoList(
                photoMemoItemList = photoMemoDataList!!,
                modifier = Modifier
                    .padding(padding)
                    .padding(top = 8.dp),
                repository
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun SearchBarPreview() {
//    RedealTheme {
//        PhotoMemoFragment()
//    }
//}

private val favoriteCollectionsData = listOf(
    R.drawable.empty_photo,
    R.drawable.empty_photo,
    R.drawable.empty_photo,
    R.drawable.empty_photo,
    R.drawable.empty_photo,
)