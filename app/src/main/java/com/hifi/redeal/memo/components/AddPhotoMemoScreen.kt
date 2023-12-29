package com.hifi.redeal.memo.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.outlined.PhotoLibrary
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hifi.redeal.theme.RedealTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddPhotoMemoToolbar(
    title: String,
    modifier: Modifier = Modifier,
    onClickAlbum:() -> Unit
//    mainActivity: MainActivity
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
                    // mainActivity.removeFragment(MainActivity.PHOTO_DETAIL_FRAGMENT)
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIos,
                        contentDescription = null,
                    )
                }
            },
            actions = {
                IconButton(onClick = {
                    onClickAlbum()
                }) {
                    Icon(
                        imageVector = Icons.Outlined.PhotoLibrary,
                        contentDescription = null,
                    )
                }
                IconButton(onClick = {
                    // todo : device Camara 불러오기
                }) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = null,
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,
                navigationIconContentColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.primary,
                actionIconContentColor = MaterialTheme.colorScheme.primary
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
fun AddPhotoMemoScreen(
    onClickAlbum: () -> Unit
){
    RedealTheme {
        Scaffold(
            topBar = {
                AddPhotoMemoToolbar(
                    title = "포토메모 등록",
                    onClickAlbum = onClickAlbum
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier.padding(padding)
            ) {
            }
        }
    }
}
@Preview
@Composable
fun PreviewToolbar(){
    RedealTheme {
        Scaffold(
            topBar = {
                AddPhotoMemoToolbar(
                    title = "포토메모 등록",
                    onClickAlbum = {}
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier.padding(padding)
            ) {
            }
        }
    }
}

