package com.texthip.thip.data.model.comments.response

import kotlinx.serialization.Serializable

@Serializable
data class CommentsResponse(
    val commentList: List<CommentList>,
    val nextCursor: String?,
    val isLast: Boolean,
)

@Serializable
data class CommentList(
    val commentId: Int?,
    val creatorId: Int?,
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

@Serializable
data class ReplyList(
    val commentId: Int,
    val parentCommentCreatorNickname: String,
    val creatorId: Int,
    val creatorProfileImageUrl: String,
    val creatorNickname: String,
    val aliasName: String,
    val aliasColor: String,
    val postDate: String,
    val content: String,
    val likeCount: Int,
    val isLike: Boolean,
    val isWriter: Boolean,
)