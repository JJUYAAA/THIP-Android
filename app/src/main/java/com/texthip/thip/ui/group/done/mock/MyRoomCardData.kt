package com.texthip.thip.ui.group.done.mock

import com.texthip.thip.R

data class MyRoomCardData(
    val roomId: Int,
    val bookImageUrl: String?, // API에서 받은 이미지 URL
    val imageRes: Int = R.drawable.bookcover_sample, // 스켈레톤 이미지 (fallback)
    val bookTitle: String,
    val memberCount: Int,
    val endDate: String? = null
)

data class MyRoomsPaginationResult(
    val data: List<MyRoomCardData>,
    val nextCursor: String?,
    val isLast: Boolean
)