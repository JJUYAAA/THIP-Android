package com.texthip.thip.ui.group.done.mock

import com.texthip.thip.R

data class MyRoomCardData(
    val roomId: Int,
    val bookImageUrl: String?, // API에서 받은 이미지 URL
    val bookTitle: String,
    val memberCount: Int,
    val endDate: String? = null
)

data class MyRoomsPaginationResult(
    val data: List<MyRoomCardData>,
    val nextCursor: String?,
    val isLast: Boolean
)