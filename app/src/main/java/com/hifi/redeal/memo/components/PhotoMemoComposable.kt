package com.hifi.redeal.memo.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Timestamp
import com.hifi.redeal.R
import com.hifi.redeal.memo.model.PhotoMemoData
import com.hifi.redeal.theme.RedealTheme

@Composable
fun MemoBox(
    text: String = "새로운 메모",
    modifier: Modifier = Modifier
) {
    // todo : 배경색이 흰색이 아님
    Surface(
        modifier = modifier,
        color = Color.White
    )
    {
        Text(
            text = text,
            // todo : text style 기존과 다름
            style = MaterialTheme.typography.bodyLarge,
            //todo: border 테두리 색 변경
            modifier = Modifier
                .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp))
                .padding(12.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
fun PhotoMemoItem(
    text: String,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        // todo : 날짜 text Color 변경
        Text(
            text = "2023.12.18",
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.ExtraBold
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // todo : 기존 nx3 행 -> 1행
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 2.dp),
            content = {
                items(favoriteCollectionsData){item ->
                    Image(
                        painter = painterResource(id = item),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(100.dp)
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
        MemoBox(text, modifier = Modifier.padding(top = 6.dp))
    }
}

@Composable
fun PhotoMemoList(
    photoMemoItemList:List<PhotoMemoData>,
    modifier:Modifier = Modifier
){
    LazyColumn(
        content = {
            items(photoMemoItemList){item ->
                PhotoMemoItem(text = item.context, Modifier.padding(horizontal = 24.dp))
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
    modifier: Modifier = Modifier
) {
    Box{
        TopAppBar(
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                )
            },
            navigationIcon = {
                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(R.drawable.arrow_back_ios_24px),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            modifier = modifier,
        )

        Divider(
            thickness = 4.dp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.align(Alignment.BottomStart)
        )
    }
}

@Composable
fun PhotoMemoFragment(
    modifier:Modifier = Modifier
){
    Column(){
        MyAppToolbar(title = "포토 메모")
        PhotoMemoList(photoMemoItemList = testPhotoMemoList)
    }
}
@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    RedealTheme {
        Column() {
            MyAppToolbar("포토 메모")
            PhotoMemoList(photoMemoItemList = testPhotoMemoList)
        }
    }
}

private val favoriteCollectionsData = listOf(
    R.drawable.empty_photo,
    R.drawable.empty_photo,
    R.drawable.empty_photo,
    R.drawable.empty_photo,
    R.drawable.empty_photo,
)

private val testPhotoMemoList = listOf(
    PhotoMemoData("textMemo1", date = Timestamp.now(), favoriteCollectionsData),
    PhotoMemoData("textMemo2", date = Timestamp.now(), favoriteCollectionsData),
    PhotoMemoData("textMemo3", date = Timestamp.now(), favoriteCollectionsData),
    PhotoMemoData("textMemo4", date = Timestamp.now(), favoriteCollectionsData)
)