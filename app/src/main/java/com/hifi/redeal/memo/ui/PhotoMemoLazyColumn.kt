package com.hifi.redeal.memo.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hifi.redeal.memo.model.PhotoMemo
import com.hifi.redeal.theme.RedealTheme

@Composable
internal fun PhotoMemoLazyColumn(
    modifier: Modifier = Modifier,
    photoMemos: List<PhotoMemo> = emptyList(),
    onClickPhoto: (photoMemoUris: String, order: Int) -> Unit = { _, _ -> }
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
            .padding(vertical = 16.dp, horizontal = 20.dp),
    )
    // todo : 리스트 마지막 부분에 맨위로 가는 기능 등 처리 해주기
}

@Composable
internal fun PhotoMemoItem(
    modifier: Modifier = Modifier,
    photoMemo: PhotoMemo = PhotoMemo(),
    onClickPhoto: (photoMemoUris: String, order: Int) -> Unit = { _, _ -> }
) {
    Column(
        modifier = modifier
    ) {
        DateText(
            timestamp = photoMemo.timestamp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp)
        )
        PhotosLazyRow(
            imageUris = photoMemo.imageUris,
            onClickPhoto = onClickPhoto,
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
        )
        Text(
            text = photoMemo.memo,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 20.dp, start = 4.dp)
        )
    }
}

@Preview("photo memo item")
@Composable
private fun PhotoMemoItemPreview() {
    RedealTheme {
        PhotoMemoItem()
    }
}