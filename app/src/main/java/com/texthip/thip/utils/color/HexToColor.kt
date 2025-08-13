package com.texthip.thip.utils.color

import androidx.compose.ui.graphics.Color

fun hexToColor(hex: String): Color {
    return try {
        Color(android.graphics.Color.parseColor(hex))
    } catch (e: IllegalArgumentException) {
        //잘못된 형식이면 기본 색
        Color.White
    }
}