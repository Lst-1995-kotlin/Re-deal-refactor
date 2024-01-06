package com.hifi.redeal.memo.components

import android.os.Bundle
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Timestamp
import com.hifi.redeal.MainActivity
import com.hifi.redeal.R
import com.hifi.redeal.memo.model.RecordMemoData
import com.hifi.redeal.memo.repository.PhotoMemoRepository
import com.hifi.redeal.memo.utils.intervalBetweenDateText
import com.hifi.redeal.memo.vm.PhotoMemoViewModel
import com.hifi.redeal.theme.RedealTheme
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecordMemoToolbar(
    title: String,
    modifier: Modifier = Modifier,
    mainActivity: MainActivity
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
fun RecordMemoScreen(
    photoMemoViewModel: PhotoMemoViewModel,
    repository: PhotoMemoRepository,
    mainActivity: MainActivity,
    clientIdx: Long
) {
    val photoMemoDataList by photoMemoViewModel.photoMemoList.observeAsState()
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
                        MainActivity.ADD_PHOTO_MEMO_FRAGMENT,
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
        photoMemoDataList?.let {
            RecordMemoListTest(
                recordMemoItemList = testDataList,
                modifier = Modifier
                    .padding(padding)
                    .padding(top = 8.dp),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecordMemoToolbarTest(
    title: String,
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
                }) {
                    Icon(
                        painter = painterResource(R.drawable.arrow_back_ios_24px),
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
private fun RecordMemoItemTest(
    item: RecordMemoData,
    isPlay:Boolean,
    modifier: Modifier = Modifier
) {
    val time = if(isPlay) "00:00:01 / 00:00:15" else "00:00:15"
    Column(modifier) {
        Text(
            text = intervalBetweenDateText(item.date.toDate()),
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.ExtraBold
            ),
            modifier = Modifier
                .fillMaxWidth()
        )
        MemoBox(modifier = Modifier.padding(top = 6.dp), text = item.context)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ){
            OutlinedIconButton(
                onClick = { /*TODO*/ },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = if(isPlay) MaterialTheme.colorScheme.primary else Color.White,
                    contentColor = if(isPlay) Color.White else MaterialTheme.colorScheme.primary
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .size(36.dp)
            ) {
                Icon(
                    imageVector = if(isPlay) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ){
                Text(
                    text = item.audioFilename,
                    color = if(isPlay) MaterialTheme.colorScheme.primary else Color.Black,
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

                if(isPlay) {
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
private fun RecordMemoListTest(
    recordMemoItemList: List<RecordMemoData>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        content = {
            itemsIndexed(recordMemoItemList) { idx, item ->
                RecordMemoItemTest(
                    item = item,
                    isPlay = idx == 1,
                    modifier = Modifier.padding(horizontal = 24.dp),
                )
                Divider(Modifier.padding(vertical = 16.dp))
            }
        },
        modifier = modifier
    )
}

@Preview(name = "RecordMemoScreen")
@Composable
fun RecordMemoScreenPreView() {
    RedealTheme {
        Scaffold(
            topBar = {
                RecordMemoToolbarTest(
                    title = stringResource(id = R.string.record_memo_toolbar),
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
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
            testDataList.let {
                RecordMemoListTest(
                    recordMemoItemList = testDataList,
                    modifier = Modifier
                        .padding(padding)
                        .padding(vertical = 20.dp),
                )
            }
        }
    }
}

val testDataList: List<RecordMemoData> = listOf(
    RecordMemoData(
        "Test Context 1",
        Timestamp(Date()),
        null,
        "test_audio1.mp3"
    ),
    RecordMemoData(
        "Test Context 2",
        Timestamp(Date()),
        null,
        "test_audio2.mp3"
    ),
    // Add more data as needed...
)
