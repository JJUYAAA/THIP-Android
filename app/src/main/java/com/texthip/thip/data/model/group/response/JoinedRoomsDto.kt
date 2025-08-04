package com.texthip.thip.data.model.group.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class JoinedRoomsDto(
    @SerialName("roomList") val roomList: List<JoinedRoomDto>,
    @SerialName("nickname") val nickname: String,
    @SerialName("page") val page: Int,
    @SerialName("size") val size: Int,
    @SerialName("last") val last: Boolean,
    @SerialName("first") val first: Boolean
)

@Serializable
data class JoinedRoomDto(
    @SerialName("roomId") val roomId: Int,
    @SerialName("bookImageUrl") val bookImageUrl: String?,
    @SerialName("bookTitle") val bookTitle: String,
    @SerialName("memberCount") val memberCount: Int,
    @SerialName("userPercentage") val userPercentage: Int
)
