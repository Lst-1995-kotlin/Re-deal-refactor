package com.hifi.redeal.memo.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.hifi.redeal.MainActivity
import com.hifi.redeal.R
import com.hifi.redeal.memo.model.BottomButtonState
import com.hifi.redeal.memo.repository.PhotoMemoRepository
import com.hifi.redeal.theme.RedealTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddPhotoMemoToolbar(
    title: String,
    modifier: Modifier = Modifier,
    mainActivity: MainActivity,
    onAlbumResult: (uris: List<Uri>) -> Unit
) {
    val albumLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris ->
            onAlbumResult(uris)
        }
    )

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
                    mainActivity.removeFragment(MainActivity.ADD_PHOTO_MEMO_FRAGMENT)
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIos,
                        contentDescription = null,
                    )
                }
            },
            actions = {
                IconButton(onClick = {
                    albumLauncher.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
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
    memoValue: String,
    onChangeMemoValue: (text: String) -> Unit,
    selectedImageList: List<Uri>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        if (selectedImageList.isEmpty()) {
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
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 2.dp),
                modifier = Modifier.padding(vertical = 32.dp),
                content = {
                    items(selectedImageList) { uri ->
                        AsyncImage(
                            model = uri,
                            contentDescription = null,
                            modifier = Modifier.size(100.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            )
        }

        OutlinedTextField(
            value = memoValue,
            onValueChange = {
                onChangeMemoValue(it)
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
        )
    }
}

@Composable
private fun BottomButton(
    state: BottomButtonState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonText = when (state) {
        BottomButtonState.IDLE -> {
            stringResource(id = R.string.add_photo_memo_bottom_button)
        }

        BottomButtonState.PRESSED -> {
            stringResource(id = R.string.add_photo_memo_bottom_button_clicked)
        }

        BottomButtonState.DISABLED -> {
            stringResource(id = R.string.add_photo_memo_bottom_button_disable)
        }
    }
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            disabledContentColor = MaterialTheme.colorScheme.onSecondary,
            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        enabled = state == BottomButtonState.IDLE,
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
    clientIdx: Long,
    repository: PhotoMemoRepository,
    mainActivity: MainActivity,
    modifier: Modifier = Modifier
) {

    var selectedImageList by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var bottomButtonState by remember { mutableStateOf(BottomButtonState.DISABLED) }
    val onAlbumResult = { uriList: List<Uri> ->
        bottomButtonState = BottomButtonState.IDLE
        selectedImageList = uriList
    }
    var memoValue by remember { mutableStateOf("") }
    val onChangeMemoValue = { text: String ->
        memoValue = text
    }

    val onClickBottomButton = {
        bottomButtonState = BottomButtonState.PRESSED
        repository.addPhotoMemo(clientIdx, memoValue, selectedImageList.toList()) {
            mainActivity.removeFragment(MainActivity.ADD_PHOTO_MEMO_FRAGMENT)
        }
    }
    Scaffold(
        topBar = {
            AddPhotoMemoToolbar(
                title = stringResource(id = R.string.add_photo_memo_toolbar),
                onAlbumResult = onAlbumResult,
                mainActivity = mainActivity
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
                memoValue = memoValue,
                onChangeMemoValue = onChangeMemoValue,
                selectedImageList = selectedImageList,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp)
            )

            BottomButton(
                state = bottomButtonState,
                onClick = onClickBottomButton,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp, horizontal = 28.dp)
                    .imePadding()
            )
        }
    }
}

