package com.texthip.thip.ui.group.room.mock

import androidx.compose.ui.graphics.Color

data class MenuBottomSheetItem(
    val text: String,
    val color: Color = Color.White,
    val onClick: () -> Unit,
)

