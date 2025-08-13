package com.texthip.thip.utils

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt

fun String.toComposeColor(defaultColor: Color = Color.Gray): Color {
    return try {
        Color(this.toColorInt())
    } catch (e: IllegalArgumentException) {
        defaultColor
    }
}