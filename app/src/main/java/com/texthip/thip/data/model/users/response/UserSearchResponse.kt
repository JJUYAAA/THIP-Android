package com.texthip.thip.data.model.users.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class UserSearchResponse(
    val userList: List<UserItem>
)
@Serializable
data class UserItem(
    @SerialName("userId") val userId: Int,
    @SerialName("nickname") val nickname: String,
    @SerialName("profileImageUrl") val profileImageUrl: String?,
    @SerialName("aliasName") val aliasName: String,
    @SerialName("aliasColor") val aliasColor: String,
    @SerialName("followerCount") val followerCount: Int
)