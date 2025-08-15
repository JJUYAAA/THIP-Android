package com.texthip.thip.data.model.users.response

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class MyFollowingsResponse(
    @SerializedName("followings") val followings: List<FollowingList>,
    @SerializedName("totalFollowingCount") val totalFollowingCount: Int,
    @SerializedName("nextCursor") val nextCursor: String?,
    @SerializedName("isLast") val isLast: Boolean
)

@Serializable
data class FollowingList(
    @SerializedName("userId") val userId: Long,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("profileImageUrl") val profileImageUrl: String?,
    @SerializedName("aliasName") val aliasName: String,
    @SerializedName("aliasColor") val aliasColor: String,
    @SerializedName("isFollowing") val isFollowing: Boolean
)

@Serializable
data class MyRecentFollowingsResponse(
    @SerializedName("recentWriters") val recentWriters: List<RecentWriterList>
)

@Serializable
data class RecentWriterList(
    @SerializedName("userId") val userId: Long,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("profileImageUrl") val profileImageUrl: String?
)