package com.texthip.thip.data.model.users

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class MyFollowingsResponse(
    @SerializedName("followings") val followings: List<FollowingDto>,
    @SerializedName("totalFollowingCount") val totalFollowingCount: Int,
    @SerializedName("nextCursor") val nextCursor: String?,
    @SerializedName("isLast") val isLast: Boolean
)

@Serializable
data class FollowingDto(
    @SerializedName("userId") val userId: Int,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("profileImageUrl") val profileImageUrl: String?,
    @SerializedName("aliasName") val aliasName: String,
    @SerializedName("aliasColor") val aliasColor: String,
    @SerializedName("isFollowing") val isFollowing: Boolean
)
