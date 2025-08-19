package com.texthip.thip.data.model.users.response

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class UserSearchResponse(
    val userList: List<UserItem>
)
@Serializable
data class UserItem(
    @SerializedName("userId") val userId: Int,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("profileImageUrl") val profileImageUrl: String?,
    @SerializedName("aliasName") val aliasName: String,
    @SerializedName("aliasColor") val aliasColor: String,
    @SerializedName("followerCount") val followerCount: Int
)