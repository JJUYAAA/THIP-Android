package com.texthip.thip.data.model.comments.response

import kotlinx.serialization.Serializable

@Serializable
data class CommentsCreateResponse(
    val commentId: Int?,
    val creatorId: Long?,
    val creatorProfileImageUrl: String?,
    val creatorNickname: String?,
    val aliasName: String?,
    val aliasColor: String?,
    val postDate: String?,
    val content: String?,
    val likeCount: Int,
    val isDeleted: Boolean,
    val isWriter: Boolean,
    val isLike: Boolean,
    val replyList: List<ReplyList>,
)