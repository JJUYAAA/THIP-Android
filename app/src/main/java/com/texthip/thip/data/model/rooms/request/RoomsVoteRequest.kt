package com.texthip.thip.data.model.rooms.request

import kotlinx.serialization.Serializable

@Serializable
data class RoomsVoteRequest(
    val page: Int,
    val isOverview: Boolean,
    val content: String,
    val voteItemList: List<VoteItem>
)

@Serializable
data class VoteItem(
    val itemName: String
)