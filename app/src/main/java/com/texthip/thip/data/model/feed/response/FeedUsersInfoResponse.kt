package com.texthip.thip.data.model.feed.response

import kotlinx.serialization.Serializable

@Serializable
data class FeedUsersInfoResponse(
    val creatorId: Long,
    val profileImageUrl: String,
    val nickname: String,
    val aliasName: String,
    val aliasColor: String,
    val followerCount: Int,
    val totalFeedCount: Int,
    val isFollowing: Boolean,
    val latestFollowerProfileImageUrls: List<String>
)
