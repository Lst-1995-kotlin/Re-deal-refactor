package com.hifi.redeal.memo.ui.recordMemo

import android.content.ContentResolver
import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.hifi.redeal.R
import com.hifi.redeal.memo.datastore.saveAudioFile
import com.hifi.redeal.memo.model.RecordMemo
import com.hifi.redeal.memo.navigation.NavigationDestination
import com.hifi.redeal.memo.utils.VoiceMemoRecorder
import com.hifi.redeal.memo.ui.MemoTopAppBar
import com.hifi.redeal.memo.ui.SaveDialog
import com.hifi.redeal.memo.utils.convertToDurationTime
import com.hifi.redeal.memo.utils.createAudioUri
import com.hifi.redeal.memo.utils.formatRecordTime
import com.hifi.redeal.theme.RedealTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private enum class RecordState {
    BEFORE_RECORDING,
    ON_RECORDING,
    AFTER_RECORDING,
}

object RecordMemoEntryDestination : NavigationDestination {
    override val route = "client_record_memo_entry"
    override val titleRes = R.string.add_record_memo_toolbar
    const val clientId = "clientId"
    val routeWithArgs = "$route/{$clientId}"
}

@Composable
internal fun RecordMemoEntryRoute(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    viewModel: RecordMemoEntryViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    RecordMemoEntryScreen(
        onBackClick = onBackClick,
        recordMemoUiState = viewModel.recordMemoUiState,
        updateUiState = {
            viewModel.updateUiState(it)
        },
        onSaveClick = {
            coroutineScope.launch {
                val saveAudioFileUri = saveAudioFile(
                    context,
                    viewModel.recordMemoUiState.recordMemo.audioFileUri
                )
                viewModel.updateUiState(
                    viewModel.recordMemoUiState.recordMemo.copy(
                        audioFileUri = saveAudioFileUri
                    )
                )
                viewModel.saveRecordMemo()
                onBackClick()
            }
        },
        modifier = modifier
    )
}

@Composable
internal fun RecordMemoEntryScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    recordMemoUiState: RecordMemoUiState = RecordMemoUiState(),
    updateUiState: (RecordMemo) -> Unit = {},
    onSaveClick: () -> Unit = {}
) {
    var recordState by remember { mutableStateOf(RecordState.ON_RECORDING) }
    val albumLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            recordState = RecordState.AFTER_RECORDING
            updateUiState(
                recordMemoUiState.recordMemo.copy(
                    audioFileUri = uri.toString()
                )
            )
        }
    )

    Scaffold(
        modifier = modifier,
        topBar = {
            MemoTopAppBar(
                titleRes = RecordMemoEntryDestination.titleRes,
                canNavigateBack = true,
                onNavigationClick = onBackClick
            )
        },
        bottomBar = {
            SaveButton(
                enabled = recordMemoUiState.isEntryValid,
                onClick = onSaveClick,
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
            OutlinedTextField(
                value = recordMemoUiState.recordMemo.memo,
                onValueChange = {
                    updateUiState(recordMemoUiState.recordMemo.copy(memo = it))
                },
                textStyle = MaterialTheme.typography.bodyMedium,
                label = {
                    Text(text = stringResource(id = R.string.add_record_memo_body_text_field_placeholder))
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.outline
                ),
                modifier = Modifier.fillMaxWidth()
            )
            if (recordMemoUiState.recordMemo.audioFileUri.isBlank()) {
                AddFileButton(
                    onClick = {
                        albumLauncher.launch("audio/*")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .height(52.dp)
                )
            }
            AudioMemoPlayer(
                uri = recordMemoUiState.recordMemo.audioFileUri.toUri(),
                setUri = {
                    updateUiState(
                        recordMemoUiState.recordMemo.copy(
                            audioFileUri = it.toString()
                        )
                    )
                },
                setDuration = {
                    updateUiState(
                        recordMemoUiState.recordMemo.copy(
                            duration = it
                        )
                    )
                },
                setRecordedFilename = {
                    updateUiState(
                        recordMemoUiState.recordMemo.copy(
                            audioFilename = it
                        )
                    )
                },
                state = recordState,
                changeState = {
                    recordState = it
                }
            )
        }
    }
}

@Composable
private fun AudioMemoPlayer(
    uri: Uri?,
    setUri: (uri: Uri?) -> Unit,
    setDuration: (duration: Long) -> Unit,
    setRecordedFilename: (name: String) -> Unit,
    state: RecordState,
    changeState: (state: RecordState) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var fileUri by remember { mutableStateOf<Uri?>(null) }
    val voiceRecorder by remember { mutableStateOf(VoiceMemoRecorder(context)) }
    var showDialog by remember { mutableStateOf(false) }

    val onSaveDialog = { title: String ->
        voiceRecorder.stop()
        changeState(RecordState.AFTER_RECORDING)
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, title)
        }

        context.contentResolver.update(
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
            val contentResolver: ContentResolver = context.contentResolver
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
                            fileUri = createAudioUri(context)
                            voiceRecorder.start(context, fileUri!!)
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
private fun AddFileButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    FilledIconButton(
        onClick = onClick,
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
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var currentPosition by remember { mutableLongStateOf(0) }
    var isPlaying by remember { mutableStateOf(false) }
    var sliderPosition by remember { mutableLongStateOf(0) }
    var duration by remember { mutableLongStateOf(0) }
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
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


@Composable
private fun SaveButton(
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
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
            text = stringResource(id = R.string.add_record_memo_bottom_button),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Preview("Record Memo Entry Screen")
@Composable
private fun RecordMemoEntryScreenPreview(){
    RedealTheme {
        RecordMemoEntryScreen()
    }
}