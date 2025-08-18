package com.texthip.thip.data.model.rooms.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MyRoomListResponse(
    @SerialName("roomList") val roomList: List<MyRoomResponse>,
    @SerialName("nextCursor") val nextCursor: String?,
    @SerialName("isLast") val isLast: Boolean
)

@Serializable
data class MyRoomResponse(
    @SerialName("roomId") val roomId: Int,
    @SerialName("bookImageUrl") val bookImageUrl: String,
    @SerialName("roomName") val roomName: String,
    @SerialName("recruitCount") val recruitCount: Int,
    @SerialName("memberCount") val memberCount: Int,
    @SerialName("endDate") val endDate: String,
    @SerialName("type") val type: String
)