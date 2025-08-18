package com.texthip.thip.utils.color

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt

fun hexToColor(hex: String): Color {
    return try {
        Color(hex.toColorInt())
    } catch (e: IllegalArgumentException) {
        //잘못된 형식이면 기본 색
        Color.White
    }
}