package com.texthip.thip.data.model.rooms.response

import kotlinx.serialization.Serializable

@Serializable
data class RoomsPostsResponse(
    val postList: List<PostList>,
    val roomId: Int,
    val isbn: String,
    val isOverviewEnabled: Boolean,
    val nextCursor: String?,
    val isLast: Boolean,
)

@Serializable
data class PostList(
    val postId: Int,
    val postDate: String,
    val postType: String,
    val page: Int,
    val userId: Long,
    val nickName: String,
    val profileImageUrl: String?,
    val content: String,
    val likeCount: Int,
    val commentCount: Int,
    val isOverview: Boolean,
    val isLiked: Boolean,
    val isWriter: Boolean,
    val isLocked: Boolean,
    val voteItems: List<VoteItems>,
)

@Serializable
data class VoteItems(
    val voteItemId: Int,
    val itemName: String,
    val percentage: Int,
    val isVoted: Boolean,
)