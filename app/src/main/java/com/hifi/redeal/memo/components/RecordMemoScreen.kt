package com.hifi.redeal.memo.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Divider
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.hifi.redeal.R
import com.hifi.redeal.memo.model.RecordMemo
import com.hifi.redeal.memo.navigation.NavigationDestination
import com.hifi.redeal.memo.ui.DateText
import com.hifi.redeal.memo.ui.MemoTopAppBar
import com.hifi.redeal.memo.utils.convertToDurationTime
import com.hifi.redeal.memo.vm.RecordMemoViewModel
import com.hifi.redeal.theme.RedealTheme
import kotlinx.coroutines.delay

object RecordMemoDestination : NavigationDestination {
    override val route = "client_record_memo"
    override val titleRes = R.string.record_memo_toolbar
    const val clientId = "clientId"
    val routeWithArgs = "$route/{$clientId}"
}

@Composable
fun RecordMemoScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onFabClick: () -> Unit = {},
    viewModel: RecordMemoViewModel = hiltViewModel()
) {
    val recordMemosUiState by viewModel.recordMemosUiState.collectAsStateWithLifecycle()
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
        RecordMemoList(
            recordMemos = recordMemosUiState.recordMemos,
            modifier = Modifier
                .padding(padding)
                .padding(vertical = 20.dp, horizontal = 24.dp),
        )
    }
}


@Composable
private fun RecordMemoList(
    recordMemos: List<RecordMemo>,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var currentPosition by remember { mutableLongStateOf(0) }
    var sliderPosition by remember { mutableLongStateOf(0) }
    var currentAudioIndex by remember { mutableIntStateOf(-1) }
    var isPlaying by remember { mutableStateOf(false) }
    val player = remember { ExoPlayer.Builder(context).build() }

    LaunchedEffect(key1 = player.currentPosition, key2 = player.isPlaying, key3 = isPlaying) {
        delay(1000)
        currentPosition = player.currentPosition
        if (currentPosition >= player.duration) {
            isPlaying = false
            currentAudioIndex = -1
        }
    }

    LaunchedEffect(currentPosition) {
        sliderPosition = currentPosition
    }

    DisposableEffect(player) {
        onDispose {
            player.release()
        }
    }

    LazyColumn(
        content = {
            itemsIndexed(recordMemos) { idx, item ->
                RecordMemoItem(
                    recordMemo = item,
                    isPlaying = isPlaying,
                    isFocusing = currentAudioIndex == idx,
                    sliderPosition = sliderPosition,
                    currentPosition = currentPosition,
                    onClickPlayer = {
                        if (currentAudioIndex == idx) {
                            if (isPlaying) {
                                player.pause()
                            } else {
                                player.play()
                            }
                            isPlaying = player.isPlaying
                        } else {
                            currentPosition = 0
                            player.setMediaItem(MediaItem.fromUri(item.audioFileUri))
                            player.prepare()
                            player.play()
                            isPlaying = true
                        }
                        currentAudioIndex = idx
                    },
                    onValueChange = { sliderPosition = it.toLong() },
                    onValueChangeFinished = {
                        currentPosition = sliderPosition
                        player.seekTo(sliderPosition)
                    },
                )
                Divider(modifier = Modifier.padding(vertical = 16.dp))
            }
        },
        modifier = modifier
    )
}

@Composable
private fun MemoMultiText(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier
    )
}

@Composable
private fun AudioPlayerToggleButton(
    isPlaying: Boolean,
    isFocusing: Boolean,
    onClickPlayer: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedIconButton(
        onClick = {
            onClickPlayer()
        },
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = if (isFocusing && isPlaying) MaterialTheme.colorScheme.primary else Color.White,
            contentColor = if (isFocusing && isPlaying) Color.White else MaterialTheme.colorScheme.primary
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        modifier = modifier
    ) {
        Icon(
            imageVector = if (isFocusing && isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
            contentDescription = null
        )
    }
}

@Composable
private fun AudioPlayerFileName(
    text: String,
    isFocusing: Boolean,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        color = if (isFocusing) MaterialTheme.colorScheme.primary else Color.Black,
        style = MaterialTheme.typography.bodyLarge.copy(
            fontWeight = FontWeight.Bold
        ),
        modifier = modifier
    )
}

@Composable
private fun AudioPlayerDurationTimeText(
    duration: Long,
    currentPosition: Long,
    isFocusing: Boolean,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
    ) {
        if (isFocusing) {
            Text(
                text = "${(currentPosition).convertToDurationTime()} / ",
                style = MaterialTheme.typography.bodySmall,
            )
        }
        Text(
            text = duration.convertToDurationTime(),
            modifier = Modifier,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
private fun AudioPlayer(
    audioFilename: String,
    duration: Long,
    isPlaying: Boolean,
    isFocusing: Boolean,
    sliderPosition: Long,
    currentPosition: Long,
    onClickPlayer: () -> Unit,
    onValueChange: (newValue: Float) -> Unit,
    onValueChangeFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        AudioPlayerToggleButton(
            isPlaying = isPlaying,
            isFocusing = isFocusing,
            onClickPlayer = onClickPlayer,
            modifier = Modifier.size(36.dp)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        ) {
            AudioPlayerFileName(text = audioFilename, isFocusing = isFocusing)
            AudioPlayerDurationTimeText(
                duration = duration,
                isFocusing = isFocusing,
                currentPosition = currentPosition,
                modifier = Modifier.padding(top = 4.dp)
            )
            if (isFocusing) {
                Slider(
                    value = sliderPosition.toFloat(),
                    onValueChange = {
                        onValueChange(it)
                    },
                    onValueChangeFinished = {
                        onValueChangeFinished()
                    },
                    valueRange = 0f..duration.toFloat(),
                )
            }
        }
    }
}

@Preview(name = "AudioPlayer", showBackground = true)
@Composable
private fun AudioPlayerPreview() {
    RedealTheme {
        AudioPlayer(
            audioFilename = "테스트 파일",
            duration = 30000000L,
            isPlaying = false,
            isFocusing = false,
            currentPosition = 0,
            sliderPosition = 0,
            onClickPlayer = {},
            onValueChange = {},
            onValueChangeFinished = {}
        )
    }
}

@Composable
private fun RecordMemoItem(
    recordMemo: RecordMemo,
    isPlaying: Boolean,
    isFocusing: Boolean,
    sliderPosition: Long,
    currentPosition: Long,
    onClickPlayer: () -> Unit,
    onValueChange: (newValue: Float) -> Unit,
    onValueChangeFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        DateText(
            //todo : 수정
            timestamp = 1000000,
            modifier = Modifier
                .fillMaxWidth()
        )
        MemoMultiText(modifier = Modifier.padding(top = 8.dp), text = recordMemo.memo)

        AudioPlayer(
            audioFilename = recordMemo.audioFilename,
            isPlaying = isPlaying,
            isFocusing = isFocusing,
            sliderPosition = sliderPosition,
            currentPosition = currentPosition,
            duration = recordMemo.duration,
            onClickPlayer = onClickPlayer,
            onValueChange = onValueChange,
            onValueChangeFinished = onValueChangeFinished,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        )
    }
}

