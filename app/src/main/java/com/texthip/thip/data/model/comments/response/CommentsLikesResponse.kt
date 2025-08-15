package com.texthip.thip.data.model.comments.response

import kotlinx.serialization.Serializable

@Serializable
data class CommentsLikesResponse(
    val commentId: Int,
    val isLiked: Boolean,
)
