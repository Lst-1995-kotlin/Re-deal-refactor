package com.hifi.redeal.memo.ui.userMemo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hifi.redeal.R
import com.hifi.redeal.memo.navigation.NavigationDestination
import com.hifi.redeal.memo.ui.MemoTopAppBar
import com.hifi.redeal.memo.ui.PhotoMemoLazyColumn
import com.hifi.redeal.memo.ui.RecordMemoLazyColumn
import com.hifi.redeal.memo.ui.photoMemo.PhotoMemosUiState
import com.hifi.redeal.memo.ui.recordMemo.RecordMemosUiState
import com.hifi.redeal.theme.RedealTheme

enum class TabType {
    PHOTO_MEMO,
    RECORD_MEMO
}

object UserMemoDestination : NavigationDestination {
    override val route = "user_memo"
    override val titleRes = R.string.memo_toolbar_title

}

@Composable
fun UserMemoRoute(
    modifier: Modifier = Modifier,
    viewModel: UserMemoViewModel = hiltViewModel(),
    onClickPhoto: (photoMemoUris: String, order: Int) -> Unit = { _, _ -> }
) {
    val photoMemosUiState by viewModel.photoMemosUiState.collectAsStateWithLifecycle()
    val recordMemosUiState by viewModel.recordMemosUiState.collectAsStateWithLifecycle()

    UserMemoScreen(
        photoMemosUiState = photoMemosUiState,
        recordMemosUiState = recordMemosUiState,
        onClickPhoto = onClickPhoto,
        modifier = modifier
    )
}

@Composable
fun UserMemoScreen(
    modifier: Modifier = Modifier,
    photoMemosUiState: PhotoMemosUiState = PhotoMemosUiState(),
    recordMemosUiState: RecordMemosUiState = RecordMemosUiState(),
    onClickPhoto: (photoMemoUris: String, order: Int) -> Unit = { _, _ -> }
) {
    var currentTab by remember { mutableStateOf(TabType.PHOTO_MEMO) }
    Scaffold(
        modifier = modifier,
        topBar = {
            MemoTopAppBar(titleRes = UserMemoDestination.titleRes)
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = modifier.padding(padding)
        ) {
            TabRow(
                containerColor = Color.White,
                selectedTabIndex = currentTab.ordinal,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[currentTab.ordinal])
                    )
                },
            ) {
                Tab(
                    selected = currentTab == TabType.PHOTO_MEMO,
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    onClick = { currentTab = TabType.PHOTO_MEMO },
                    modifier = Modifier.height(52.dp)
                ) {
                    Text("포토 메모")
                }

                Tab(
                    selected = currentTab == TabType.RECORD_MEMO,
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = Color.Gray,
                    onClick = { currentTab = TabType.RECORD_MEMO }
                ) {
                    Text("음성 메모")
                }
            }
            when (currentTab) {
                TabType.PHOTO_MEMO ->
                    PhotoMemoLazyColumn(
                        photoMemos = photoMemosUiState.photoMemos,
                        onClickPhoto = onClickPhoto
                    )

                TabType.RECORD_MEMO ->
                    RecordMemoLazyColumn(
                        recordMemos = recordMemosUiState.recordMemos
                    )
            }
        }
    }
}

@Preview("User Memo Screen")
@Composable
private fun UserMemoScreenPreview() {
    RedealTheme {
        UserMemoScreen()
    }
}
