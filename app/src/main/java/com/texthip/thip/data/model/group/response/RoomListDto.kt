package com.texthip.thip.data.model.group.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class RoomListDto(
    @SerialName("roomId") val roomId: Int,
    @SerialName("imageUrl") val imageUrl: String?,
    @SerialName("bookTitle") val bookTitle: String,
    @SerialName("memberCount") val memberCount: Int,
    @SerialName("userPercentage") val userPercentage: Float,
    @SerialName("deadlineDate") val deadlineDate: String
)

@Serializable
data class RoomsHomeDto(
    @SerialName("deadlineRoomList") val deadline: List<RoomListDto> = emptyList(),
    @SerialName("popularityRoomList") val popularity: List<RoomListDto> = emptyList(),
    @SerialName("influencerRoomList") val influencer: List<RoomListDto> = emptyList()
)