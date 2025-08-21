package com.texthip.thip.data.model.users.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OthersFollowersResponse(
  @SerialName("followers") val followers: List<FollowerList>,
  @SerialName("totalFollowerCount") val totalFollowerCount: Int,
  @SerialName("nextCursor") val nextCursor: String?,
  @SerialName("isLast") val isLast: Boolean
)

@Serializable
data class FollowerList(
  @SerialName("userId") val userId: Long,
  @SerialName("nickname") val nickname: String,
  @SerialName("profileImageUrl") val profileImageUrl: String?,
  @SerialName("aliasName") val aliasName: String,
  @SerialName("aliasColor") val aliasColor: String,
  @SerialName("followerCount") val followerCount: Int,
  @SerialName("isMyself") val isMyself: Boolean
)