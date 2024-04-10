package com.hifi.redeal.memo.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.hifi.redeal.R
import com.hifi.redeal.theme.RedealTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
internal fun SaveDialog(
    modifier: Modifier = Modifier,
    onSave: (title: String) -> Unit = {},
    onDismiss: (Boolean) -> Unit = {}
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

@Preview("Save Dialog")
@Composable
private fun SaveDialogPreview(){
    RedealTheme {
        SaveDialog()
    }
}