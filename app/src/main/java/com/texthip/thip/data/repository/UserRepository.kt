package com.texthip.thip.data.repository

import com.texthip.thip.data.model.base.handleBaseResponse
import com.texthip.thip.data.model.users.MyFollowingsResponse
import com.texthip.thip.data.service.UserService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository@Inject constructor(
    private val userService: UserService
) {
    suspend fun getMyFollowings(
        cursor: String?,
        size: Int = 10
    ): Result<MyFollowingsResponse?> = runCatching {
        userService.getMyFollowings(cursor = cursor, size = size)
            .handleBaseResponse()
            .getOrThrow()
    }
}