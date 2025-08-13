package com.texthip.thip.data.model.rooms.response

import kotlinx.serialization.Serializable

@Serializable
data class RoomsUsersResponse(
    val userList: List<UserList>
)

@Serializable
data class UserList(
    val userId: Int,
    val nickname: String,
    val imageUrl: String,
    val aliasName: String,
    val followerCount: Int,
)