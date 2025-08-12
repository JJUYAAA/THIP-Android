package com.texthip.thip.data.model.rooms.response

import kotlinx.serialization.Serializable

@Serializable
data class RoomsCreateVoteResponse(
    val voteId: Int,
    val roomId: Int
)
