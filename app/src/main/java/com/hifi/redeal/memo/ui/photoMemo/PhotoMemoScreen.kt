package com.hifi.redeal.memo.ui.photoMemo

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hifi.redeal.R
import com.hifi.redeal.memo.navigation.NavigationDestination
import com.hifi.redeal.memo.ui.MemoTopAppBar
import com.hifi.redeal.memo.ui.PhotoMemoLazyColumn
import com.hifi.redeal.theme.RedealTheme

object PhotoMemoDestination : NavigationDestination {
    override val route = "client_photo_memo"
    override val titleRes = R.string.photo_memo_toolbar
    const val clientIdArg = "clientId"
    val routeWithArgs = "$route/{$clientIdArg}"
}

@Composable
internal fun PhotoMemoRoute(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onFabClick: () -> Unit = {},
    onClickPhoto: (photoMemoUris: String, order: Int) -> Unit = { _, _ -> },
    viewModel: PhotoMemoViewModel = hiltViewModel(),
) {
    val photoMemosUiState by viewModel.photoMemosUiState.collectAsStateWithLifecycle()

    PhotoMemoScreen(
        photoMemosUiState = photoMemosUiState,
        onBackClick = onBackClick,
        onFabClick = onFabClick,
        onClickPhoto = onClickPhoto,
        modifier = modifier
    )
}

@Composable
internal fun PhotoMemoScreen(
    modifier: Modifier = Modifier,
    photoMemosUiState: PhotoMemosUiState = PhotoMemosUiState(),
    onBackClick: () -> Unit = {},
    onFabClick: () -> Unit = {},
    onClickPhoto: (photoMemoUris: String, order: Int) -> Unit = { _, _ -> }
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            MemoTopAppBar(
                titleRes = PhotoMemoDestination.titleRes,
                canNavigateBack = true,
                onNavigationClick = onBackClick
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onFabClick,
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
        PhotoMemoLazyColumn(
            photoMemos = photoMemosUiState.photoMemos,
            modifier = Modifier.padding(padding),
            onClickPhoto = onClickPhoto
        )
    }
}

@Preview(name = "Photo Memo Screen")
@Composable
private fun PhotoMemoScreenPreview() {
    RedealTheme {
        PhotoMemoScreen()
    }
}