package com.hifi.redeal.memo.components

import android.content.ContentResolver
import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
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
import androidx.compose.ui.window.Dialog
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.hifi.redeal.MainActivity
import com.hifi.redeal.R
import com.hifi.redeal.memo.model.BottomButtonState
import com.hifi.redeal.memo.record.VoiceMemoRecorder
import com.hifi.redeal.memo.repository.RecordMemoRepository
import com.hifi.redeal.memo.utils.convertToDurationTime
import com.hifi.redeal.memo.utils.createAudioUri
import com.hifi.redeal.memo.utils.formatRecordTime
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private enum class RecordState {
    BEFORE_RECORDING,
    ON_RECORDING,
    AFTER_RECORDING,
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddRecordMemoToolbar(
    title: String,
    onClickNavigation: () -> Unit,
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
                IconButton(onClick = onClickNavigation) {
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
private fun MemoTextField(
    value: String,
    onChangeValue: (value: String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            onChangeValue(it)
        },
        textStyle = MaterialTheme.typography.bodyMedium,
        label = {
            Text(text = stringResource(id = R.string.add_record_memo_body_text_field_placeholder))
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.outline
        ),
        modifier = modifier
    )
}


@Composable
private fun AddFileButton(
    setRecordUri: (uri: Uri?) -> Unit,
    modifier: Modifier = Modifier
) {
    val albumLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if(uri != null) {
                setRecordUri(uri)
            }
        }
    )
    FilledIconButton(
        onClick = {
            albumLauncher.launch("audio/*")
        },
        shape = RoundedCornerShape(4.dp),
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.secondary
        ),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(imageVector = Icons.Default.UploadFile, contentDescription = null)
            Text(
                text = stringResource(R.string.add_record_add_file_button),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            )
        }
    }
}

@Composable
private fun VoiceRecorder(
    onStart: () -> Unit,
    onToggle: () -> Unit,
    onStop: () -> Unit,
    modifier: Modifier = Modifier
) {
    var time by remember { mutableLongStateOf(0) }
    var isRecording by remember { mutableStateOf(false) }
    var isNewRecord by remember { mutableStateOf(true) }

    LaunchedEffect(isRecording) {
        while (isRecording) {
            delay(10)
            time += 10
        }
    }

    Column(
        modifier = modifier
    ) {
        Text(
            text = formatRecordTime(time),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 24.sp
            ),
            modifier = Modifier.padding(start = 100.dp)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
        ) {
            Spacer(modifier = Modifier.size(48.dp))
            OutlinedIconButton(
                onClick = {
                    if (isNewRecord) {
                        onStart()
                        isRecording = true
                        isNewRecord = false
                    } else {
                        onToggle()
                        isRecording = !isRecording
                    }
                },
                colors = IconButtonDefaults.outlinedIconButtonColors(
                    contentColor = if (!isRecording) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        Color.Black
                    }
                ),
                border = BorderStroke(1.dp, Color.Gray),
                modifier = Modifier.size(64.dp)
            ) {
                if (!isRecording) {
                    Icon(
                        imageVector = Icons.Default.Circle,
                        contentDescription = null,
                        modifier = Modifier.size(56.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Rounded.Pause,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            IconButton(
                onClick = {
                    isRecording = false
                    onStop()
                },
                enabled = !isNewRecord,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Stop,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}

@Composable
private fun AudioPlayerToggleButton(
    isPlaying: Boolean,
    onClickPlayer: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedIconButton(
        onClick = {
            onClickPlayer()
        },
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = if (isPlaying) MaterialTheme.colorScheme.primary else Color.White,
            contentColor = if (isPlaying) Color.White else MaterialTheme.colorScheme.primary
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        modifier = modifier
    ) {
        Icon(
            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
            contentDescription = null
        )
    }
}

@Composable
private fun AudioPlayerFileName(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        color = Color.Black,
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
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
    ) {
        Text(
            text = "${(currentPosition).convertToDurationTime()} / ",
            style = MaterialTheme.typography.bodySmall,
        )
        Text(
            text = duration.convertToDurationTime(),
            modifier = Modifier,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
private fun AudioPlayer(
    uri: Uri,
    filename: String,
    setDuration: (duration: Long) -> Unit,
    mainActivity: MainActivity,
    modifier: Modifier = Modifier
) {
    var currentPosition by remember { mutableLongStateOf(0) }
    var isPlaying by remember { mutableStateOf(false) }
    var sliderPosition by remember { mutableLongStateOf(0) }
    var duration by remember { mutableLongStateOf(0) }
    val player = remember {
        ExoPlayer.Builder(mainActivity).build().apply {
            setMediaItem(MediaItem.fromUri(uri))
            prepare()
        }
    }

    player.addListener(object : Player.Listener {
        override fun onPlaybackStateChanged(state: Int) {
            if (state == Player.STATE_READY) {
                duration = player.duration
                setDuration(player.duration)
            }
        }
    })

    val onClickPlayer = {
        if (isPlaying) {
            player.pause()
        } else {
            player.play()
        }
        isPlaying = !isPlaying
    }

    LaunchedEffect(key1 = player.currentPosition, key2 = isPlaying) {
        delay(1000)
        currentPosition = player.currentPosition
        if (currentPosition >= player.duration) {
            isPlaying = false
            currentPosition = 0
            player.seekTo(0)
            player.pause()
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

    Row(modifier = modifier) {
        AudioPlayerToggleButton(
            isPlaying = isPlaying,
            onClickPlayer = onClickPlayer,
            modifier = Modifier.size(36.dp)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        ) {
            AudioPlayerFileName(text = filename)
            AudioPlayerDurationTimeText(
                duration = duration,
                currentPosition = currentPosition,
                modifier = Modifier.padding(top = 4.dp)
            )
            Slider(
                value = sliderPosition.toFloat(),
                onValueChange = {
                    sliderPosition = it.toLong()
                },
                onValueChangeFinished = {
                    currentPosition = sliderPosition
                    player.seekTo(sliderPosition)
                },
                valueRange = 0f..duration.toFloat(),
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
private fun AudioMemoPlayer(
    uri: Uri?,
    setUri: (uri: Uri?) -> Unit,
    setDuration: (duration: Long) -> Unit,
    setRecordedFilename: (name: String) -> Unit,
    state: RecordState,
    changeState: (state: RecordState) -> Unit,
    mainActivity: MainActivity,
    modifier: Modifier = Modifier
) {
    var fileUri by remember { mutableStateOf<Uri?>(null) }
    val voiceRecorder by remember { mutableStateOf(VoiceMemoRecorder(mainActivity)) }
    var showDialog by remember { mutableStateOf(false) }

    val onSaveDialog = { title: String ->
        voiceRecorder.stop()
        changeState(RecordState.AFTER_RECORDING)
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, title)
        }

        mainActivity.contentResolver.update(
            fileUri!!,
            values,
            null,
            null
        )

        setUri(fileUri)
    }

    var filename by remember { mutableStateOf("") }

    LaunchedEffect(key1 = uri) {
        if (uri != null) {
            val contentResolver: ContentResolver = mainActivity.contentResolver
            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    filename = it.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                    setRecordedFilename(filename)
                }
            }
        }
    }

    Column(
        modifier = modifier
    ) {
        Crossfade(targetState = state, label = "Record and Playback Cross fade") { state ->
            when (state) {
                RecordState.BEFORE_RECORDING,
                RecordState.ON_RECORDING -> {
                    VoiceRecorder(
                        onStart = {
                            fileUri = createAudioUri(mainActivity)
                            voiceRecorder.start(mainActivity, fileUri!!)
                        },
                        onToggle = { voiceRecorder.togglePause() },
                        onStop = {
                            showDialog = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 28.dp)
                    )
                }

                RecordState.AFTER_RECORDING -> {
                    AudioPlayer(
                        uri = uri!!,
                        filename = filename,
                        setDuration = setDuration,
                        mainActivity = mainActivity,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 28.dp)
                    )
                }
            }
        }
        if (showDialog) {
            SaveDialog(
                onSave = { onSaveDialog(it) },
                onDismiss = { showDialog = false }
            )
        }
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
            stringResource(id = R.string.add_record_memo_bottom_button)
        }

        BottomButtonState.PRESSED -> {
            stringResource(id = R.string.add_record_memo_bottom_button_clicked)
        }

        BottomButtonState.DISABLED -> {
            stringResource(id = R.string.add_record_memo_bottom_button_disable)
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
private fun SaveDialog(
    onSave: (title: String) -> Unit,
    onDismiss: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var value by remember {
        mutableStateOf(
            SimpleDateFormat(
                "yyyyMMdd_HHmm ss",
                Locale.getDefault()
            ).format(Date())
        )
    }
    Dialog(onDismissRequest = { onDismiss(false) }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = modifier
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = stringResource(R.string.add_record_save_dialog_title),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                // todo : Dialog 가 처음 띄워질 때 포커싱 & 글자 선택된 상태
                BasicTextField(
                    value = value,
                    onValueChange = { value = it },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                )
                Divider(
                    thickness = 1.dp,
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                ) {
                    TextButton(
                        onClick = {
                            onDismiss(false)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "취소",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Text(
                        text = "|",
                        color = Color.LightGray,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 12.sp
                        )
                    )
                    TextButton(
                        onClick = {
                            onSave(value)
                            onDismiss(false)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "저장",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun AddRecordMemoScreen(
    clientIdx: Long,
    repository: RecordMemoRepository,
    mainActivity: MainActivity
) {
    var recordedUri by remember { mutableStateOf<Uri?>(null) }
    var recordState by remember { mutableStateOf(RecordState.ON_RECORDING) }
    var recordedDuration by remember { mutableLongStateOf(0) }
    var bottomButtonState by remember { mutableStateOf(BottomButtonState.DISABLED) }
    var recordedFilename by remember {
        mutableStateOf(
            SimpleDateFormat(
                "yyyyMMdd_HHmm ss",
                Locale.getDefault()
            ).format(Date())
        )
    }
    var memoTextValue by remember { mutableStateOf("") }

    val onClickBottomButton = {
        bottomButtonState = BottomButtonState.PRESSED
        repository.addRecordMemo(
            clientIdx,
            memoTextValue,
            recordedUri!!,
            recordedFilename,
            recordedDuration
        ) {
            mainActivity.removeFragment(MainActivity.ADD_RECORD_MEMO_FRAGMENT)
        }
    }

    Scaffold(
        topBar = {
            AddRecordMemoToolbar(
                title = stringResource(id = R.string.add_record_memo_toolbar), onClickNavigation = {
                    mainActivity.removeFragment(MainActivity.ADD_RECORD_MEMO_FRAGMENT)
                })
        },
        bottomBar = {
            BottomButton(
                state = bottomButtonState,
                onClick = onClickBottomButton,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp, horizontal = 28.dp)
            )
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 28.dp)
                .padding(top = 40.dp)
        ) {
            MemoTextField(
                value = memoTextValue,
                onChangeValue = {
                    memoTextValue = it
                },
                modifier = Modifier.fillMaxWidth()
            )
            if (recordedUri == null) {
                AddFileButton(
                    setRecordUri = {
                        recordedUri = it
                        recordState = RecordState.AFTER_RECORDING
                        bottomButtonState = BottomButtonState.IDLE
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .height(52.dp)
                )
            }
            AudioMemoPlayer(
                uri = recordedUri,
                setUri = {
                    recordedUri = it
                    bottomButtonState = BottomButtonState.IDLE
                },
                setDuration = {
                    recordedDuration = it
                },
                setRecordedFilename = {
                    recordedFilename = it
                },
                state = recordState,
                changeState = {
                    recordState = it
                },
                mainActivity = mainActivity
            )
        }
    }
}