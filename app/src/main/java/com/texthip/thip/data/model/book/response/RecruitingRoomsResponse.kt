package com.texthip.thip.data.model.book.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecruitingRoomsResponse(
    @SerialName("recruitingRoomList") val recruitingRoomList: List<RecruitingRoomItem> = emptyList(),
    @SerialName("totalRoomCount") val totalRoomCount: Int = 0,
    @SerialName("nextCursor") val nextCursor: String? = null,
    @SerialName("isLast") val isLast: Boolean = true
)

@Serializable
data class RecruitingRoomItem(
    @SerialName("roomId") val roomId: Int = 0,
    @SerialName("bookImageUrl") val bookImageUrl: String? = null,
    @SerialName("roomName") val roomName: String = "",
    @SerialName("memberCount") val memberCount: Int = 0,
    @SerialName("recruitCount") val recruitCount: Int = 0,
    @SerialName("deadlineEndDate") val deadlineEndDate: String = ""
)