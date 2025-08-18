package com.texthip.thip.data.model.comments.request

import kotlinx.serialization.Serializable

@Serializable
data class CommentsCreateRequest(
    val content: String,
    val isReplyRequest: Boolean,
    val parentId: Int? = null,
    val postType: String,
)
