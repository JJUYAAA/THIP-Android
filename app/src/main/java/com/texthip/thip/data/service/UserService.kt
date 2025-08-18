package com.texthip.thip.data.service

import com.texthip.thip.data.model.base.BaseResponse
import com.texthip.thip.data.model.users.request.FollowRequest
import com.texthip.thip.data.model.users.request.NicknameRequest
import com.texthip.thip.data.model.users.request.ProfileUpdateRequest
import com.texthip.thip.data.model.users.request.SignupRequest
import com.texthip.thip.data.model.users.response.AliasChoiceResponse
import com.texthip.thip.data.model.users.response.FollowResponse
import com.texthip.thip.data.model.users.response.MyFollowingsResponse
import com.texthip.thip.data.model.users.response.MyPageInfoResponse
import com.texthip.thip.data.model.users.response.NicknameResponse
import com.texthip.thip.data.model.users.response.OthersFollowersResponse
import com.texthip.thip.data.model.users.response.SignupResponse
import com.texthip.thip.data.model.users.response.UserSearchResponse
import com.texthip.thip.data.model.users.response.UsersMyFollowingsRecentFeedsResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {
    @GET("users/my-followings")
    suspend fun getMyFollowings(
        @Query("size") size: Int = 10,
        @Query("cursor") cursor: String? = null
    ): BaseResponse<MyFollowingsResponse>

    @GET("users/my-followings/recent-feeds")
    suspend fun getMyFollowingsRecentFeeds(): BaseResponse<UsersMyFollowingsRecentFeedsResponse>

    @GET("users/{userId}/followers")
    suspend fun getUserFollowers(
        @Path("userId") userId: Long,
        @Query("size") size: Int = 10,
        @Query("cursor") cursor: String? = null
    ): BaseResponse<OthersFollowersResponse>

    @GET("users/my-page")
    suspend fun getMyPage(): BaseResponse<MyPageInfoResponse>

    @POST("users/nickname")
    suspend fun checkNickname(
        @Body request: NicknameRequest
    ): BaseResponse<NicknameResponse>

    @GET("users/alias")
    suspend fun getAliasChoices(): BaseResponse<AliasChoiceResponse>

    @POST("users/following/{followingUserId}")
    suspend fun toggleFollow(
        @Path("followingUserId") followingUserId: Long,
        @Body request: FollowRequest
    ): BaseResponse<FollowResponse>

    @PATCH("users")
    suspend fun updateProfile(
        @Body request: ProfileUpdateRequest
    ): BaseResponse<Unit>

    @POST("users/signup")
    suspend fun signup(
        @Header("Authorization") tempToken: String,
        @Body request: SignupRequest
    ): BaseResponse<SignupResponse>

    @GET("users")
    suspend fun searchUsers(
        @Query("isFinalized") isFinalized: Boolean,
        @Query("keyword") keyword: String,
        @Query("size") size: Int = 30
    ): BaseResponse<UserSearchResponse>

}