package com.hifi.redeal.memo.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.hifi.redeal.theme.RedealTheme
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
internal fun PhotosLazyRow(
    modifier: Modifier = Modifier,
    imageUris: List<String> = emptyList(),
    onClickPhoto: (photoMemoUris: String, order: Int) -> Unit = { _, _ -> }
) {
    LazyRow(
        modifier = modifier,
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
        }
    )
}

@Preview("Photos Lazy Row")
@Composable
private fun PhotosLazyRowPreview(){
    RedealTheme {
        PhotosLazyRow()
    }
}