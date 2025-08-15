package com.texthip.thip.data.model.comments.response

import kotlinx.serialization.Serializable

@Serializable
data class CommentsCreateResponse(
    val commentId: Int,
)