package com.texthip.thip.data.model.group.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class RoomListDto(
    @SerialName("roomId") val roomId: Int,
    @SerialName("bookImageUrl") val bookImageUrl: String?,
    @SerialName("roomName") val roomName: String,
    @SerialName("recruitCount") val recruitCount: Int,
    @SerialName("memberCount") val memberCount: Int,
    @SerialName("deadlineDate") val deadlineDate: String
)

@Serializable
data class RoomsHomeDto(
    @SerialName("deadlineRoomList") val deadlineRoomList: List<RoomListDto> = emptyList(),
    @SerialName("popularRoomList") val popularRoomList: List<RoomListDto> = emptyList()
)