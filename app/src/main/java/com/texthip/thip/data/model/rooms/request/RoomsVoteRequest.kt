package com.texthip.thip.data.model.rooms.request

import kotlinx.serialization.Serializable

@Serializable
data class RoomsVoteRequest(
    val voteItemId: Int,
    val type: Boolean
)
