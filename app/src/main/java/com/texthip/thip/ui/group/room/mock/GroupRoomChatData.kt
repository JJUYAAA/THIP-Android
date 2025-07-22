package com.texthip.thip.ui.group.room.mock

import androidx.compose.ui.graphics.painter.Painter

data class GroupRoomChatData(
    val profileImage: Painter?,
    val nickname: String,
    val date: String,
    val content: String,
    val isMine: Boolean
)
