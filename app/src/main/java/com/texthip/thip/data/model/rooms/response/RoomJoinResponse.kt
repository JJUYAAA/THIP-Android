package com.texthip.thip.data.model.rooms.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoomJoinResponse(
    @SerialName("roomId") val roomId: Int,
    @SerialName("type") val type: String
)