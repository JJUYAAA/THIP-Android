package com.texthip.thip.data.model.group.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoomJoinRequest(
    @SerialName("type") val type: String
)