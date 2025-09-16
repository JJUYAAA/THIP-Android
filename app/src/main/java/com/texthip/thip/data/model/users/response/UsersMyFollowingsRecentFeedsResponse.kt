package com.texthip.thip.data.model.users.response

import kotlinx.serialization.Serializable

@Serializable
data class UsersMyFollowingsRecentFeedsResponse(
    val myFollowingUsers: List<RecentWriterList>
)

@Serializable
data class RecentWriterList(
    val userId: Long,
    val nickname: String,
    val profileImageUrl: String
)