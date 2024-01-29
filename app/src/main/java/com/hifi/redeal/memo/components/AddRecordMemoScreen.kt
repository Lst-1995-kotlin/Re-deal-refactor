package com.hifi.redeal.memo.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Rectangle
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hifi.redeal.MainActivity
import com.hifi.redeal.R
import com.hifi.redeal.memo.model.BottomButtonState
import com.hifi.redeal.memo.utils.convertToDurationTime
import com.hifi.redeal.memo.utils.formatRecordTime
import com.hifi.redeal.memo.utils.formatRecordTimeToGray
import com.hifi.redeal.theme.RedealTheme
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

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
    modifier: Modifier = Modifier
) {
    FilledIconButton(
        onClick = {},
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
                "새로 파일을 등록하거나 녹음 하실 수 있어요",
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
    time: Long,
    isRecording: Boolean,
    onClickRecord: (recordState: RecordState) -> Unit,
    modifier: Modifier = Modifier
) {
    val iconImage = if (isRecording) Icons.Rounded.Pause else Icons.Default.Circle

    val tempTime = 120000L
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = formatRecordTime(tempTime),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 40.sp
            )
        )
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp)
        ) {
            OutlinedIconButton(
                onClick = {
                    val nextState =
                        if (isRecording) RecordState.AFTER_RECORDING else RecordState.ON_RECORDING
                    onClickRecord(nextState)
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
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
private fun VoiceMemoPlayer(
    mainActivity: MainActivity,
    path: String,
    modifier: Modifier = Modifier
) {
    // var recordingFilePath by remember { mutableStateOf(path)}
    var recordState by remember { mutableStateOf(RecordState.BEFORE_RECORDING) }
//    val recorder by remember { mutableStateOf(MediaRecorder().apply{
//            setAudioSource(MediaRecorder.AudioSource.MIC)
//            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
//            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
//            setOutputFile(recordingFilePath)
//    }) }
    var job by remember { mutableStateOf<Job?>(null) }
    var time by remember { mutableLongStateOf(0) }

    LaunchedEffect(recordState, time) {
        if (recordState == RecordState.ON_RECORDING) {
            delay(1000)
            time += 1000
        }
    }
    Column(
        modifier = modifier
    ) {
        Crossfade(targetState = recordState, label = "test") { state ->
            when (state) {
                RecordState.BEFORE_RECORDING,
                RecordState.ON_RECORDING -> {
                    VoiceRecorder(
                        time = time,
                        isRecording = true,
                        onClickRecord = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 28.dp)
                    )
                }

                RecordState.AFTER_RECORDING -> {
                    Text("레코딩 완료")
                    Button(onClick = { recordState = RecordState.BEFORE_RECORDING }) {
                        Text("초기화")
                    }
                }
            }
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
            stringResource(id = R.string.add_photo_memo_bottom_button)
        }

        BottomButtonState.PRESSED -> {
            stringResource(id = R.string.add_photo_memo_bottom_button_clicked)
        }

        BottomButtonState.DISABLED -> {
            stringResource(id = R.string.add_photo_memo_bottom_button_disable)
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

@RequiresApi(Build.VERSION_CODES.S)
@Preview(name = "RecordPlayer", showBackground = true)
@Composable
private fun RecordPlayerPreview() {
    RedealTheme() {
        VoiceMemoPlayer(
            MainActivity(), "",
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview(name = "AddRecordMemoScreen", showBackground = true)
@Composable
private fun AddRecordMemoScreenPreview() {
    RedealTheme() {
        Scaffold(
            topBar = {
                AddRecordMemoToolbar(
                    title = "음성메모 등록", onClickNavigation = {})
            },
            bottomBar = {
                BottomButton(
                    state = BottomButtonState.IDLE,
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp, horizontal = 28.dp)
                        .imePadding()
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
                    value = "",
                    onChangeValue = {},
                    modifier = Modifier.fillMaxWidth()
                )
                AddFileButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .height(52.dp)
                )
                VoiceMemoPlayer(MainActivity(), "")
            }
        }
    }
}

