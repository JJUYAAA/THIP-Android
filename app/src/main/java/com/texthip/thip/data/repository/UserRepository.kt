package com.texthip.thip.data.repository

import com.texthip.thip.data.model.base.handleBaseResponse
import com.texthip.thip.data.model.users.MyFollowingsResponse
import com.texthip.thip.data.model.users.MyPageInfoResponse
import com.texthip.thip.data.model.users.NicknameRequest
import com.texthip.thip.data.model.users.OthersFollowersResponse
import com.texthip.thip.data.service.UserService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository@Inject constructor(
    private val userService: UserService
) {
    //내 팔로잉 목록 조회
    suspend fun getMyFollowings(
        cursor: String?,
        size: Int = 10
    ): Result<MyFollowingsResponse?> = runCatching {
        userService.getMyFollowings(cursor = cursor, size = size)
            .handleBaseResponse()
            .getOrThrow()
    }

    //다른 유저 팔로워 목록 조회
    suspend fun getOthersFollowers(
        userId: Long,
        cursor: String?,
        size: Int = 10
    ): Result<OthersFollowersResponse?> = runCatching {
        userService.getUserFollowers(userId = userId, cursor = cursor, size = size)
            .handleBaseResponse()
            .getOrThrow()
    }

    //마이페이지 정보 조회
    suspend fun getMyPageInfo(): Result<MyPageInfoResponse?> = runCatching {
        userService.getMyPage()
            .handleBaseResponse()
            .getOrThrow()
    }

    suspend fun checkNickname(nickname: String) =
        userService.checkNickname(NicknameRequest(nickname))
}