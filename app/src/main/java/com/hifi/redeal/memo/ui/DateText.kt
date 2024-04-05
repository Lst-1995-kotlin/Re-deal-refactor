package com.hifi.redeal.memo.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.hifi.redeal.memo.utils.intervalBetweenDateText

@Composable
fun DateText(
    timestamp: Long,
    modifier: Modifier = Modifier
) {
    Text(
        text = intervalBetweenDateText(timestamp),
        style = MaterialTheme.typography.bodySmall.copy(
            fontWeight = FontWeight.Bold
        ),
        modifier = modifier
    )
}