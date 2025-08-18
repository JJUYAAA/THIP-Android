package com.texthip.thip.data.model.feed.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeedMineInfoResponse(
    @SerialName("creatorId") val creatorId: Int,
    @SerialName("profileImageUrl") val profileImageUrl: String?,
    @SerialName("nickname") val nickname: String,
    @SerialName("aliasName") val aliasName: String,
    @SerialName("aliasColor") val aliasColor: String,
    @SerialName("followerCount") val followerCount: Int,
    @SerialName("totalFeedCount") val totalFeedCount: Int,
    @SerialName("isFollowing") val isFollowing: Boolean,
    @SerialName("latestFollowerProfileImageUrls") val latestFollowerProfileImageUrls: List<String>
)