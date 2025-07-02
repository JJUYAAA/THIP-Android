package com.texthip.thip.ui.group.room.mock

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter

data class GroupRoomMateData(
    val profileImageUrl: Painter? = null,
    val nickname: String,
    val role: String,
    val roleColor: Color,
    val subscriberCount: Int,
)