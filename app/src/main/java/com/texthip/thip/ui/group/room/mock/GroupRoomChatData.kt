package com.texthip.thip.ui.group.room.mock

import androidx.compose.ui.graphics.painter.Painter

data class GroupRoomChatData(
    val profileImage: Painter?,
    val nickname: String,
    val date: String,
    val content: String,
    val isMine: Boolean
)

val mockMessages = listOf(
    GroupRoomChatData(
        null,
        "user.01",
        "2024.04.29",
        "공백 포함 글자 입력입니다. 공백 포함 글자 입력입니다. 공백 포함 글자 입력입니다. 공백 포함 글자 입력입니다. 공백 포함 글자 입력입니다.",
        isMine = true
    ),
    GroupRoomChatData(
        null,
        "user.01",
        "2024.04.28",
        "공백 포함 글자 입력입니다. 공백 포함 글자 입력입니다. 공백 포함 글자 입력입니다. 공백 포함 글자 입력입니다. 공백 포함 글자 입력입니다.",
        isMine = true
    ),
    GroupRoomChatData(null, "user.01", "2024.04.30", "공백 포함 글자 입력입니다.", isMine = false),
    GroupRoomChatData(
        null,
        "user.01",
        "2024.04.30",
        "공백 포함 글자 입력입니다. 공백 포함 글자 입력입니다.",
        isMine = true
    ),
    GroupRoomChatData(
        null,
        "user.01",
        "2024.04.30",
        "공백 포함 글자 입력입니다. 공백 포함 글자 입력입니다. 공백 포함 글자 입력입니다. 공백 포함 글자 입력입니다. 공백 포함 글자 입력입니다.",
        isMine = false
    ),
    GroupRoomChatData(
        null,
        "user.01",
        "2024.04.27",
        "공백 포함 글자 입력입니다. 공백 포함 글자 입력입니다. 공백 포함 글자 입력입니다. 공백 포함 글자 입력입니다. 공백 포함 글자 입력입니다.",
        isMine = true
    ),
    GroupRoomChatData(
        null,
        "user.01",
        "2024.04.27",
        "공백 포함 글자 입력입니다. 공백 포함 글자 입력입니다. 공백 포함 글자 입력입니다. 공백 포함 글자 입력입니다. 공백 포함 글자 입력입니다.",
        isMine = true
    ),
).sortedByDescending { it.date }