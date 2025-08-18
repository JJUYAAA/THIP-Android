package com.texthip.thip.data.repository

import com.texthip.thip.data.manager.TokenManager
import com.texthip.thip.data.model.base.handleBaseResponse
import com.texthip.thip.data.model.users.request.FollowRequest
import com.texthip.thip.data.model.users.response.MyFollowingsResponse
import com.texthip.thip.data.model.users.response.MyPageInfoResponse
import com.texthip.thip.data.model.users.request.NicknameRequest
import com.texthip.thip.data.model.users.request.ProfileUpdateRequest
import com.texthip.thip.data.model.users.request.SignupRequest
import com.texthip.thip.data.model.users.response.AliasChoiceResponse
import com.texthip.thip.data.model.users.response.FollowResponse
import com.texthip.thip.data.model.users.response.MyRecentFollowingsResponse
import com.texthip.thip.data.model.users.response.NicknameResponse
import com.texthip.thip.data.model.users.response.OthersFollowersResponse
import com.texthip.thip.data.model.users.response.SignupResponse
import com.texthip.thip.data.service.UserService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userService: UserService,
    private val tokenManager: TokenManager
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

    suspend fun getRecentWriters(): Result<MyRecentFollowingsResponse?> = runCatching {
        userService.getRecentWriters()
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

    suspend fun checkNickname(nickname: String): Result<NicknameResponse?> = runCatching {
        userService.checkNickname(NicknameRequest(nickname))
            .handleBaseResponse()
            .getOrThrow()
    }

    suspend fun getAliasChoices(): Result<AliasChoiceResponse?> = runCatching {
        userService.getAliasChoices()
            .handleBaseResponse()
            .getOrThrow()
    }

    suspend fun toggleFollow(
        followingUserId: Long,
        isFollowing: Boolean
    ): Result<FollowResponse?> = runCatching {
        val request = FollowRequest(type = isFollowing)
        userService.toggleFollow(followingUserId, request)
            .handleBaseResponse()
            .getOrThrow()
    }

    suspend fun updateProfile(request: ProfileUpdateRequest): Result<Unit?> = runCatching {
        userService.updateProfile(request)
            .handleBaseResponse()
            .getOrThrow()
    }

    suspend fun signup(request: SignupRequest): Result<SignupResponse?> {
        val tempToken = tokenManager.getTempToken()
            ?: return Result.failure(Exception("임시 토큰이 없습니다."))

        return runCatching {
            userService.signup("Bearer $tempToken", request)
                .handleBaseResponse()
                .getOrThrow()
        }
    }
}