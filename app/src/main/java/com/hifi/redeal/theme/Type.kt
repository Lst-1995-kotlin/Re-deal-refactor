package com.hifi.redeal.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.hifi.redeal.R

val nanumsquareneo = FontFamily(
    listOf(
        Font(R.font.nanumsquareneo_extrabold, FontWeight.ExtraBold, FontStyle.Normal),
        Font(R.font.nanumsquareneo_bold, FontWeight.Bold, FontStyle.Normal),
        Font(R.font.nanumsquareneo_regular, FontWeight.Normal, FontStyle.Normal),
        Font(R.font.nanumsquareneo_lite, FontWeight.Light, FontStyle.Normal)
    )
)

val typography = Typography(
    displayLarge = TextStyle(
        fontFamily = nanumsquareneo,
        fontWeight = FontWeight.Light,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontFamily = nanumsquareneo,
        fontSize = 45.sp,
        lineHeight = 52.sp
    ),
    displaySmall = TextStyle(
        fontFamily = nanumsquareneo,
        fontSize = 36.sp,
        lineHeight = 44.sp
    ),
    titleMedium = TextStyle(
        fontFamily = nanumsquareneo,
        fontWeight = FontWeight(500),
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = (0.15).sp
    ),
    bodySmall = TextStyle(
        fontFamily = nanumsquareneo,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = (0.4).sp
    ),
    bodyMedium = TextStyle(
        fontFamily = nanumsquareneo,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = (0.25).sp
    ),
    bodyLarge = TextStyle(
        fontFamily = nanumsquareneo,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = (0.5).sp
    ),
    labelMedium = TextStyle(
        fontFamily = nanumsquareneo,
        fontWeight = FontWeight(500),
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = (0.5).sp,
        textAlign = TextAlign.Center
    ),
    labelLarge = TextStyle(
        fontFamily = nanumsquareneo,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = (0.1).sp
    )
)