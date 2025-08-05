package com.texthip.thip.ui.group.done.mock

data class MyRoomCardData(
    val roomId: Int,
    val bookImageUrl: String,
    val bookTitle: String,
    val memberCount: Int,
    val endDate: String? = null
)

data class MyRoomsPaginationResult(
    val data: List<MyRoomCardData>,
    val nextCursor: String?,
    val isLast: Boolean
)