package com.hifi.redeal.memo.ui.photoMemo

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hifi.redeal.R
import com.hifi.redeal.memo.datastore.saveImages
import com.hifi.redeal.memo.model.PhotoMemo
import com.hifi.redeal.memo.navigation.NavigationDestination
import com.hifi.redeal.memo.ui.MemoTopAppBar
import com.hifi.redeal.memo.ui.PhotosLazyRow
import com.hifi.redeal.theme.RedealTheme
import kotlinx.coroutines.launch

object PhotoMemoEntryDestination : NavigationDestination {
    override val route = "client_photo_memo_entry"
    override val titleRes = R.string.add_photo_memo_toolbar
    const val clientIdArg = "clientId"
    val routeWithArgs = "$route/{$clientIdArg}"
}

@Composable
internal fun PhotoMemoEntryRoute(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onClickPhoto: (photoMemoUris: String, order: Int) -> Unit = { _, _ -> },
    viewModel: PhotoMemoEntryViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    PhotoMemoEntryScreen(
        photoMemoUiState = viewModel.photoMemoUiState,
        updateUiState = {
            viewModel.updateUiState(it)
        },
        onSaveClick = {
            coroutineScope.launch {
                val saveImageUris =
                    saveImages(context, viewModel.photoMemoUiState.photoMemo.imageUris)
                viewModel.updateUiState(
                    viewModel.photoMemoUiState.photoMemo.copy(
                        imageUris = saveImageUris
                    )
                )
                viewModel.savePhotoMemo()
                onBackClick()
            }
        },
        onBackClick = onBackClick,
        onClickPhoto = onClickPhoto,
        modifier = modifier
    )
}

@Composable
internal fun PhotoMemoEntryScreen(
    modifier: Modifier = Modifier,
    photoMemoUiState: PhotoMemoUiState = PhotoMemoUiState(),
    onSaveClick: () -> Unit = {},
    updateUiState: (PhotoMemo) -> Unit = {},
    onBackClick: () -> Unit = {},
    onClickPhoto: (photoMemoUris: String, order: Int) -> Unit = { _, _ -> },
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            MemoTopAppBar(
                titleRes = PhotoMemoEntryDestination.titleRes,
                canNavigateBack = true,
                onNavigationClick = onBackClick,
                actions = {
                    TopBarActions(
                        onAlbumLauncherResult = { uris ->
                            updateUiState(photoMemoUiState.photoMemo.copy(
                                imageUris = uris.map { it.toString() }
                            ))
                        }
                    )
                }
            )
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
        ) {
            PhotoMemoEntryBody(
                photoMemo = photoMemoUiState.photoMemo,
                onPhotoMemoValueChange = updateUiState,
                imageUris = photoMemoUiState.photoMemo.imageUris,
                onClickPhoto = onClickPhoto,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp)
            )

            SaveButton(
                enabled = photoMemoUiState.isEntryValid,
                onSaveClick = onSaveClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp, horizontal = 28.dp)
            )
        }
    }
}

@Composable
private fun PhotoMemoEntryBody(
    photoMemo: PhotoMemo,
    onPhotoMemoValueChange: (PhotoMemo) -> Unit,
    imageUris: List<String>,
    modifier: Modifier = Modifier,
    onClickPhoto: (photoMemoUris: String, order: Int) -> Unit = { _, _ -> }
) {
    Column(
        modifier = modifier
    ) {
        if (imageUris.isEmpty()) {
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
            PhotosLazyRow(
                imageUris = imageUris,
                onClickPhoto = onClickPhoto,
                modifier = Modifier.padding(vertical = 32.dp)
            )
        }

        OutlinedTextField(
            value = photoMemo.memo,
            onValueChange = {
                onPhotoMemoValueChange(photoMemo.copy(memo = it))
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
private fun SaveButton(
    enabled: Boolean,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onSaveClick,
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
            text = stringResource(id = R.string.add_photo_memo_bottom_button),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
private fun TopBarActions(
    onAlbumLauncherResult: (uris: List<Uri>) -> Unit = {},
) {
    val albumLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = onAlbumLauncherResult
    )
    Row {
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
    }
}

@Preview("Photo Memo Entry Screen")
@Composable
private fun PhotoMemoEntryScreenPreview(){
    RedealTheme {
        PhotoMemoEntryScreen()
    }
}