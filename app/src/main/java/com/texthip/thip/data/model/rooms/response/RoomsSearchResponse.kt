package com.texthip.thip.data.model.rooms.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoomsSearchResponse(
    @SerialName("roomList") val roomList: List<SearchRoomItem> = emptyList(),
    @SerialName("nextCursor") val nextCursor: String? = null,
    @SerialName("isLast") val isLast: Boolean = true
)

@Serializable
data class SearchRoomItem(
    @SerialName("roomId") val roomId: Int = 0,
    @SerialName("bookImageUrl") val bookImageUrl: String? = null,
    @SerialName("roomName") val roomName: String = "",
    @SerialName("memberCount") val memberCount: Int = 0,
    @SerialName("recruitCount") val recruitCount: Int = 0,
    @SerialName("deadlineDate") val deadlineDate: String = "",
    @SerialName("isPublic") val isPublic: Boolean = true
)