package com.texthip.thip.data.model.rooms.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class RoomSecretRoomResponse(
    @SerialName("matched") val matched: Boolean = false,
    @SerialName("roomId") val roomId: Int = 0
)