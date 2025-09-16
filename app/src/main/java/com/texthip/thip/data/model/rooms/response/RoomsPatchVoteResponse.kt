package com.texthip.thip.data.model.rooms.response

import kotlinx.serialization.Serializable

@Serializable
data class RoomsPatchVoteResponse(
    val roomId: Int,
)
