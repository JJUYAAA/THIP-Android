package com.texthip.thip.data.model.users.response

import kotlinx.serialization.Serializable

@Serializable
data class OthersFollowersResponse(
    val followers: List<FollowerList>,
    val totalFollowerCount: Int,
    val nextCursor: String?,
    val isLast: Boolean
)

@Serializable
data class FollowerList(
    val userId: Long,
    val nickname: String,
    val profileImageUrl: String?,
    val aliasName: String,
    val aliasColor: String,
    val followerCount: Int,
)

