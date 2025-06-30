package com.texthip.thip.ui.group.room.mock

data class GroupRoomBodyData(
    val bookTitle: String,
    val bookAuthor: String,
    val currentPage: Int,
    val percentage: Int,
    val voteList: List<VoteData>
)
