package com.texthip.thip.data.model.comments.request

import kotlinx.serialization.Serializable

@Serializable
data class CommentsLikesRequest(
    val type: Boolean
)
