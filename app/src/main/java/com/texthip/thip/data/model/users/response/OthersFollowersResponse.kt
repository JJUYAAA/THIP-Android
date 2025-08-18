package com.texthip.thip.data.model.users.response

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class OthersFollowersResponse(
  @SerializedName("followers") val followers: List<FollowerList>,
  @SerializedName("totalFollowerCount") val totalFollowerCount: Int,
  @SerializedName("nextCursor") val nextCursor: String?,
  @SerializedName("isLast") val isLast: Boolean
)

@Serializable
data class FollowerList(
  @SerializedName("userId") val userId: Long,
  @SerializedName("nickname") val nickname: String,
  @SerializedName("profileImageUrl") val profileImageUrl: String?,
  @SerializedName("aliasName") val aliasName: String,
  @SerializedName("aliasColor") val aliasColor: String,
  @SerializedName("followerCount") val followerCount: Int
)