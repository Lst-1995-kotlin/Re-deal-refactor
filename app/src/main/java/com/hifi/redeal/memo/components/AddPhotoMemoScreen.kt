package com.hifi.redeal.memo.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hifi.redeal.R
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
private fun AddPhotoMemoBody(
    modifier:Modifier = Modifier
){
    var textFieldValue by remember {mutableStateOf("")}
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(id = R.string.add_photo_memo_body_empty_photo),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 20.sp
            ),
            modifier = Modifier
                .padding(vertical = 32.dp)
                .align(Alignment.CenterHorizontally)
        )

        OutlinedTextField(
            value = textFieldValue,
            onValueChange = {
                textFieldValue = it
            },
            label = {
                Text(
                    text = stringResource(id = R.string.add_photo_memo_body_text_field_placeholder),
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.outline
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )
    }
}
@Composable
private fun BottomButton(
    enabled:Boolean,
    modifier:Modifier = Modifier
){
    val buttonText  = if(enabled) stringResource(id = R.string.add_photo_memo_bottom_button) else
        stringResource(id = R.string.add_photo_memo_bottom_button_disable)
    Button(
        onClick = { /*TODO*/ },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            disabledContentColor = MaterialTheme.colorScheme.onSecondary,
            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        enabled = enabled,
        shape = RoundedCornerShape(4.dp),
        modifier = modifier
            .height(48.dp)
    ) {
        Text(
            text = buttonText,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
fun AddPhotoMemoScreen(
    onClickAlbum: () -> Unit,
    modifier:Modifier = Modifier
){
    RedealTheme {
        Scaffold(
            topBar = {
                AddPhotoMemoToolbar(
                    title = "포토메모 등록",
                    onClickAlbum = onClickAlbum
                )
            },
            containerColor = Color.White,
            modifier = modifier
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding)
            ) {
                AddPhotoMemoBody(
                    Modifier
                        .weight(1f)
                )

                BottomButton(
                    enabled = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp, horizontal = 28.dp)
                        .imePadding()
                )
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
            },
            containerColor = Color.White,
            modifier = Modifier
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxWidth()
            ) {
                AddPhotoMemoBody(
                    Modifier
                        .weight(1f)
                )

                BottomButton(
                    enabled = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp, horizontal = 28.dp)
                )
            }
        }
    }
}

