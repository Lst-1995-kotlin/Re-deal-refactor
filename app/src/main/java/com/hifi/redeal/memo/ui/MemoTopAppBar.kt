package com.hifi.redeal.memo.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hifi.redeal.R.string
import com.hifi.redeal.theme.RedealTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoTopAppBar(
    @StringRes titleRes: Int,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    actions: @Composable (RowScope.() -> Unit) = {},
    onNavigationClick: () -> Unit = {},
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = titleRes),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            )
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = onNavigationClick) {
                    Icon(
                        imageVector = Filled.ArrowBackIos,
                        contentDescription = stringResource(id = string.back_button)
                    )
                }
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
            navigationIconContentColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.primary,
            actionIconContentColor = MaterialTheme.colorScheme.primary
        ),
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.primary)
            .padding(bottom = 2.dp)
            .testTag("memoTopAppBar")
    )
}

@Preview("Top App Bar")
@Composable
private fun MemoTopAppBarPreview() {
    RedealTheme {
        MemoTopAppBar(
            titleRes = string.untitled,
            canNavigateBack = true
        )
    }
}