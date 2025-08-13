package com.texthip.thip.data.model.book.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecruitingRoomsResponse(
    @SerialName("recruitingRoomList") val recruitingRoomList: List<RecruitingRoomItem>,
    @SerialName("totalRoomCount") val totalRoomCount: Int,
    @SerialName("nextCursor") val nextCursor: String?,
    @SerialName("isLast") val isLast: Boolean
)

@Serializable
data class RecruitingRoomItem(
    @SerialName("roomId") val roomId: Int,
    @SerialName("bookImageUrl") val bookImageUrl: String,
    @SerialName("roomName") val roomName: String,
    @SerialName("memberCount") val memberCount: Int,
    @SerialName("recruitCount") val recruitCount: Int,
    @SerialName("deadlineEndDate") val deadlineEndDate: String
)