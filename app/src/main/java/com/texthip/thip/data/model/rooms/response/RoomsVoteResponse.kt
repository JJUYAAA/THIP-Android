package com.texthip.thip.data.model.rooms.response

import kotlinx.serialization.Serializable

@Serializable
data class RoomsVoteResponse(
    val voteItemId: Int,
    val roomId: Int,
    val type: Boolean,
)
