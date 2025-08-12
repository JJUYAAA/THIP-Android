package com.texthip.thip.data.model.book.response

import kotlinx.serialization.Serializable

@Serializable
data class RecruitingRoomsResponse(
    val recruitingRoomList: List<RecruitingRoomItem>,
    val totalRoomCount: Int,
    val nextCursor: String?,
    val isLast: Boolean
)

@Serializable
data class RecruitingRoomItem(
    val roomId: Int,
    val bookImageUrl: String,
    val roomName: String,
    val memberCount: Int,
    val recruitCount: Int,
    val deadlineEndDate: String
)