package com.texthip.thip.data.model.rooms.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class RoomSecreteRoomRequest(
    @SerialName("password") val password: String
)