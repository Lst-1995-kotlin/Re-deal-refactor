package com.hifi.redeal.memo.components

import android.os.Bundle
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.hifi.redeal.MainActivity
import com.hifi.redeal.R
import com.hifi.redeal.memo.model.PlayState
import com.hifi.redeal.memo.model.RecordMemoData
import com.hifi.redeal.memo.utils.intervalBetweenDateText
import com.hifi.redeal.memo.vm.RecordMemoViewModel

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
private fun MemoBox(
    modifier: Modifier = Modifier,
    text: String = "새로운 메모"
) {
    Surface(
        modifier = modifier,
        color = Color.White
    )
    {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp))
                .padding(12.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
private fun RecordMemoItem(
    item: RecordMemoData,
    state: PlayState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            text = intervalBetweenDateText(item.date.toDate()),
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.ExtraBold
            ),
            modifier = Modifier
                .fillMaxWidth()
        )
        MemoBox(modifier = Modifier.padding(top = 8.dp), text = item.context)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            OutlinedIconButton(
                onClick = {
                    onClick()
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = if (state == PlayState.PLAYING) MaterialTheme.colorScheme.primary else Color.White,
                    contentColor = if (state == PlayState.PLAYING) Color.White else MaterialTheme.colorScheme.primary
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .size(36.dp)
            ) {
                Icon(
                    imageVector = if (state == PlayState.PLAYING) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = item.audioFilename,
                    color = if (state != PlayState.STOP) MaterialTheme.colorScheme.primary else Color.Black,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                )

                Text(
                    text = "",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )

                if (state != PlayState.STOP) {
                    Slider(
                        value = 0.0f,
                        onValueChange = {
                        },
                        valueRange = 0f..100f, // Define the range of values
                        steps = 1, // Define the step size
                        modifier = Modifier
                    )
                }
            }
        }
    }
}

@Composable
private fun RecordMemoList(
    recordMemoItemList: List<RecordMemoData>,
    mainActivity: MainActivity,
    modifier: Modifier = Modifier,
) {
    val player by remember { mutableStateOf(ExoPlayer.Builder(mainActivity).build()) }
    var currentPlayingIndex by remember { mutableStateOf(-1) }
    var playState by remember { mutableStateOf(PlayState.STOP) }


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
                    state = if(currentPlayingIndex == idx) playState else PlayState.STOP,
                    onClick = {
                        currentPlayingIndex.takeIf { it != -1 }?.let { player.stop() }
                        when(playState){
                            PlayState.STOP -> {
                                player.setMediaItem(MediaItem.fromUri(item.audioFileUri!!))
                                player.prepare()
                                player.play()
                                playState = PlayState.PLAYING
                            }
                            PlayState.PLAYING -> {
                                playState = if(currentPlayingIndex != idx){
                                    player.setMediaItem(MediaItem.fromUri(item.audioFileUri!!))
                                    player.prepare()
                                    player.play()
                                    PlayState.PLAYING
                                }else {
                                    player.pause()
                                    PlayState.PAUSE
                                }
                            }
                            PlayState.PAUSE -> {
                                playState = if(currentPlayingIndex != idx){
                                    player.setMediaItem(MediaItem.fromUri(item.audioFileUri!!))
                                    player.prepare()
                                    player.play()
                                    PlayState.PLAYING
                                }else {
                                    player.prepare()
                                    player.play()
                                    PlayState.PLAYING
                                }
                            }
                            else -> {

                            }
                        }
                        currentPlayingIndex = idx
                    },
                    modifier = Modifier.padding(horizontal = 24.dp),
                )
                Divider(Modifier.padding(vertical = 16.dp))
            }
        },
        modifier = modifier
    )
}

//@Composable
//private fun RecordMemoListTest(
//    recordMemoItemList: List<RecordMemoData>,
//    modifier: Modifier = Modifier,
//) {
//    LazyColumn(
//        content = {
//            itemsIndexed(recordMemoItemList) { idx, item ->
//                RecordMemoItemTest(
//                    item = item,
//                    isPlaying = idx == 1,
//                    modifier = Modifier.padding(horizontal = 24.dp),
//                )
//                Divider(Modifier.padding(vertical = 16.dp))
//            }
//        },
//        modifier = modifier
//    )
//}

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
                    .padding(top = 8.dp),
            )
        }
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//private fun RecordMemoToolbarTest(
//    title: String,
//    modifier: Modifier = Modifier
//) {
//    Box {
//        TopAppBar(
//            title = {
//                Text(
//                    text = title,
//                    style = MaterialTheme.typography.bodyLarge.copy(
//                        fontSize = 20.sp,
//                        fontWeight = FontWeight.ExtraBold
//                    )
//                )
//            },
//            navigationIcon = {
//                IconButton(onClick = {
//                }) {
//                    Icon(
//                        painter = painterResource(R.drawable.arrow_back_ios_24px),
//                        contentDescription = null,
//                    )
//                }
//            },
//            actions = {
//                IconButton(
//                    onClick = {
//                        // todo : 편집 버튼 클릭시 삭제, 이름 변경, 즐겨찾기 등록할 수 있도록
//                    },
//                    colors = IconButtonDefaults.iconButtonColors(
//                        contentColor = MaterialTheme.colorScheme.primary
//                    )
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.EditNote,
//                        contentDescription = null,
//                    )
//                }
//            },
//            colors = TopAppBarDefaults.topAppBarColors(
//                containerColor = Color.White,
//                navigationIconContentColor = MaterialTheme.colorScheme.primary,
//                titleContentColor = MaterialTheme.colorScheme.primary
//            ),
//            modifier = modifier
//        )
//
//        Divider(
//            thickness = 2.dp,
//            color = MaterialTheme.colorScheme.primary,
//            modifier = Modifier.align(Alignment.BottomStart)
//        )
//    }
//}

@Composable
private fun RecordMemoItemTest(
    item: RecordMemoData,
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    val time = if (isPlaying) "00:00:01 / 00:00:15" else "00:00:15"
    Column(modifier) {
        Text(
            text = intervalBetweenDateText(item.date.toDate()),
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.ExtraBold
            ),
            modifier = Modifier
                .fillMaxWidth()
        )
        MemoBox(modifier = Modifier.padding(top = 8.dp), text = item.context)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            OutlinedIconButton(
                onClick = { /*TODO*/ },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = if (isPlaying) MaterialTheme.colorScheme.primary else Color.White,
                    contentColor = if (isPlaying) Color.White else MaterialTheme.colorScheme.primary
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .size(36.dp)
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = item.audioFilename,
                    color = if (isPlaying) MaterialTheme.colorScheme.primary else Color.Black,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                )

                Text(
                    text = time,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )

                if (isPlaying) {
                    Slider(
                        value = 0.0f,
                        onValueChange = {
                        },
                        valueRange = 0f..100f, // Define the range of values
                        steps = 1, // Define the step size
                        modifier = Modifier
                    )
                }
            }
        }
    }
}

//@Preview(name = "RecordMemoScreen")
//@Composable
//fun RecordMemoScreenPreView() {
//    RedealTheme {
//        Scaffold(
//            topBar = {
//                RecordMemoToolbarTest(
//                    title = stringResource(id = R.string.record_memo_toolbar),
//                )
//            },
//            floatingActionButton = {
//                FloatingActionButton(
//                    onClick = {
//                    },
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Add,
//                        contentDescription = null
//                    )
//                }
//            },
//            floatingActionButtonPosition = FabPosition.End,
//            containerColor = Color.White
//        ) { padding ->
//            testDataList.let {
//                RecordMemoListTest(
//                    recordMemoItemList = testDataList,
//                    modifier = Modifier
//                        .padding(padding)
//                        .padding(vertical = 20.dp),
//                )
//            }
//        }
//    }
//}

//val testDataList: List<RecordMemoData> = listOf(
//    RecordMemoData(
//        "Test Context 1",
//        Timestamp(Date()),
//        null,
//        "test_audio1.mp3"
//    ),
//    RecordMemoData(
//        "Test Context 2",
//        Timestamp(Date()),
//        null,
//        "test_audio2.mp3"
//    ),
//    // Add more data as needed...
//)
