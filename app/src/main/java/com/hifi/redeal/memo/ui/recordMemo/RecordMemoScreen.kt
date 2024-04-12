package com.hifi.redeal.memo.ui.recordMemo

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hifi.redeal.R
import com.hifi.redeal.memo.navigation.NavigationDestination
import com.hifi.redeal.memo.ui.MemoTopAppBar
import com.hifi.redeal.memo.ui.RecordMemoLazyColumn
import com.hifi.redeal.theme.RedealTheme

object RecordMemoDestination : NavigationDestination {
    override val route = "client_record_memo"
    override val titleRes = R.string.record_memo_toolbar
    const val clientId = "clientId"
    val routeWithArgs = "$route/{$clientId}"
}

@Composable
internal fun RecordMemoRoute(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onFabClick: () -> Unit = {},
    viewModel: RecordMemoViewModel = hiltViewModel()
) {
    val recordMemosUiState by viewModel.recordMemosUiState.collectAsStateWithLifecycle()

    RecordMemoScreen(
        recordMemosUiState = recordMemosUiState,
        onBackClick = onBackClick,
        onFabClick = onFabClick,
        modifier = modifier
    )
}

@Composable
internal fun RecordMemoScreen(
    modifier: Modifier = Modifier,
    recordMemosUiState: RecordMemosUiState = RecordMemosUiState(),
    onBackClick: () -> Unit = {},
    onFabClick: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            MemoTopAppBar(
                titleRes = R.string.record_memo_toolbar,
                canNavigateBack = true,
                onNavigationClick = onBackClick,
                actions = {
                    IconButton(
                        onClick = {
                            // todo : 편집 버튼 클릭시 삭제, 이름 변경, 즐겨찾기 등록할 수 있도록
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.EditNote,
                            contentDescription = null,
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onFabClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        containerColor = Color.White
    ) { padding ->
        RecordMemoLazyColumn(
            recordMemos = recordMemosUiState.recordMemos,
            modifier = Modifier
                .padding(padding)
                .padding(vertical = 20.dp, horizontal = 24.dp),
        )
    }
}

@Preview("Record Memo Screen")
@Composable
private fun RecordMemoScreenPreview(){
    RedealTheme {
        RecordMemoScreen()
    }
}