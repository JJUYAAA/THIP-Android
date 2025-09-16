package com.texthip.thip.data.model.users.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MyFollowingsResponse(
    @SerialName("followings") val followings: List<FollowingList>,
    @SerialName("totalFollowingCount") val totalFollowingCount: Int,
    @SerialName("nextCursor") val nextCursor: String?,
    @SerialName("isLast") val isLast: Boolean
)

@Serializable
data class FollowingList(
    @SerialName("userId") val userId: Long,
    @SerialName("nickname") val nickname: String,
    @SerialName("profileImageUrl") val profileImageUrl: String?,
    @SerialName("aliasName") val aliasName: String,
    @SerialName("aliasColor") val aliasColor: String,
    @SerialName("isFollowing") val isFollowing: Boolean
)