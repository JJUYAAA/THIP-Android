package com.texthip.thip.ui.group.done.mock

data class MyRoomCardData(
    val roomId: Int,
    val bookImageUrl: String?, // API에서 받은 이미지 URL
    val roomName: String,
    val recruitCount: Int,
    val memberCount: Int,
    val endDate: String,
    val type: String // "playingAndRecruiting", "recruiting", "playing", "expired"
)

data class MyRoomsPaginationResult(
    val data: List<MyRoomCardData>,
    val nextCursor: String?,
    val isLast: Boolean
)