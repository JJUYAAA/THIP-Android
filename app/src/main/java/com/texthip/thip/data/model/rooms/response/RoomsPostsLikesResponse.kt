package com.texthip.thip.data.model.rooms.response

import kotlinx.serialization.Serializable

@Serializable
data class RoomsPostsLikesResponse(
    val postId: Int,
    val isLiked: Boolean,
)
