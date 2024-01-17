package com.hifi.redeal.memo.components

import android.os.Bundle
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.hifi.redeal.MainActivity
import com.hifi.redeal.R
import com.hifi.redeal.memo.model.RecordMemoData
import com.hifi.redeal.memo.utils.intervalBetweenDateText
import com.hifi.redeal.memo.vm.RecordMemoViewModel
import com.hifi.redeal.theme.RedealTheme
import kotlinx.coroutines.delay
import java.util.Date

private fun Long.convertToText(): String {
    val sec = this / 1000
    val minutes = sec / 60
    val seconds = sec % 60

    val minutesString = if (minutes < 10) {
        "0$minutes"
    } else {
        minutes.toString()
    }
    val secondsString = if (seconds < 10) {
        "0$seconds"
    } else {
        seconds.toString()
    }
    return "$minutesString:$secondsString"
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecordMemoToolbar(
    title: String,
    mainActivity: MainActivity,
    modifier: Modifier = Modifier
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
                    mainActivity.removeFragment(MainActivity.RECORD_MEMO_FRAGMENT)
                }) {
                    Icon(
                        painter = painterResource(R.drawable.arrow_back_ios_24px),
                        contentDescription = null,
                    )
                }
            },
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
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,
                navigationIconContentColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.primary
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
private fun DateText(
    date: Date,
    modifier: Modifier = Modifier
) {
    Text(
        text = intervalBetweenDateText(date),
        style = MaterialTheme.typography.bodySmall.copy(
            fontWeight = FontWeight.Bold
        ),
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
    onClickPlayer:() -> Unit,
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
    isFocusing:Boolean,
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
    text: String,
    isFocusing: Boolean,
    modifier: Modifier = Modifier
){
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        modifier = modifier
    )
}

@Composable
private fun AudioPlayer(
    audioFilename: String,
    duration: String,
    isPlaying: Boolean,
    isFocusing:Boolean,
    sliderPosition:Long,
    currentPosition:Long,
    onClickPlayer:() -> Unit,
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
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {

                Text(
                    text = (currentPosition).convertToText(),
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    color = Color.Black,
                    style = TextStyle(fontWeight = FontWeight.Bold)
                )

                val remainTime = 10000 - currentPosition
                Text(
                    text = if (remainTime >= 0) remainTime.convertToText() else "",
                    modifier = Modifier
                        .padding(8.dp),
                    color = Color.Black,
                    style = TextStyle(fontWeight = FontWeight.Bold)
                )
            }
            if (isFocusing) {
                Slider(
                    value = sliderPosition.toFloat(),
                    onValueChange = {
                        onValueChange(it)
                    },
                    onValueChangeFinished = {
                        onValueChangeFinished()
                    },
                    valueRange = 0f..10000f, // Define the range of values todo: 범위 변경
                    steps = 1, // Define the step size
                    modifier = Modifier
                )
            }
        }
    }
}

@Preview(name = "AudioPlayer", showBackground = true)
@Composable
private fun AudioPlayerPreview(){
    RedealTheme {
        AudioPlayer(
            audioFilename = "테스트 파일",
            duration = "03:24",
            isPlaying = false,
            isFocusing = true,
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
    item: RecordMemoData,
    isPlaying:Boolean,
    isFocusing:Boolean,
    sliderPosition:Long,
    currentPosition:Long,
    onClickPlayer:() -> Unit,
    onValueChange: (newValue: Float) -> Unit,
    onValueChangeFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        DateText(
            date = item.date,
            modifier = Modifier
                .fillMaxWidth()
        )
        MemoMultiText(modifier = Modifier.padding(top = 8.dp), text = item.context)

        AudioPlayer(
            audioFilename = item.audioFilename,
            isPlaying = isPlaying,
            isFocusing = isFocusing,
            sliderPosition = sliderPosition,
            currentPosition = currentPosition,
            duration = item.duration,
            onClickPlayer = onClickPlayer,
            onValueChange = onValueChange,
            onValueChangeFinished = onValueChangeFinished,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        )
    }
}

@Composable
private fun RecordMemoList(
    recordMemoItemList: List<RecordMemoData>,
    mainActivity: MainActivity,
    modifier: Modifier = Modifier,
) {
    var currentPosition by remember { mutableLongStateOf(0) }
    var sliderPosition by remember { mutableLongStateOf(0) }
    var currentAudioIndex by remember { mutableIntStateOf(-1) }
    var isPlaying by remember { mutableStateOf(false) }
    val player = remember { ExoPlayer.Builder(mainActivity).build() }

    LaunchedEffect(key1 = player.currentPosition, key2 = player.isPlaying, key3 = isPlaying) {
        delay(1000)
        currentPosition = player.currentPosition
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
            itemsIndexed(recordMemoItemList) { idx, item ->
                RecordMemoItem(
                    item = item,
                    isPlaying = isPlaying,
                    isFocusing = currentAudioIndex == idx,
                    sliderPosition = sliderPosition,
                    currentPosition = currentPosition,
                    onClickPlayer = {
                        if(currentAudioIndex == idx){
                            if(isPlaying) {
                                player.pause()
                            }else {
                                player.play()
                            }
                            isPlaying = player.isPlaying
                        }else{
                            currentPosition = 0
                            player.setMediaItem(MediaItem.fromUri(item.audioFileUri!!))
                            player.prepare()
                            player.play()
                            isPlaying = true
                        }
                        currentAudioIndex = idx
                    },
                    onValueChange = {sliderPosition = it.toLong()},
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
fun RecordMemoScreen(
    recordMemoViewModel: RecordMemoViewModel,
    mainActivity: MainActivity,
    clientIdx: Long
) {
    val recordMemoDataList by recordMemoViewModel.recordMemoList.observeAsState()
    Scaffold(
        topBar = {
            RecordMemoToolbar(
                title = stringResource(id = R.string.record_memo_toolbar),
                mainActivity = mainActivity
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val newBundle = Bundle()
                    newBundle.putLong("clientIdx", clientIdx)
                    mainActivity.replaceFragment(
                        MainActivity.ADD_RECORD_MEMO_FRAGMENT,
                        true,
                        newBundle
                    )
                },
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
        recordMemoDataList?.let {
            RecordMemoList(
                recordMemoItemList = recordMemoDataList!!,
                mainActivity = mainActivity,
                modifier = Modifier
                    .padding(padding)
                    .padding(vertical = 20.dp, horizontal = 24.dp),
            )
        }
    }
}
