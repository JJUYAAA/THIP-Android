package com.texthip.thip.data.service

import com.texthip.thip.data.model.base.BaseResponse
import com.texthip.thip.data.model.group.request.RoomJoinRequest
import com.texthip.thip.data.model.group.response.RoomJoinResponse
import com.texthip.thip.data.model.users.MyFollowingsResponse
import com.texthip.thip.data.model.users.MyPageInfoResponse
import com.texthip.thip.data.model.users.NicknameRequest
import com.texthip.thip.data.model.users.NicknameResponse
import com.texthip.thip.data.model.users.OthersFollowersResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {
    @GET("users/my-followings")
    suspend fun getMyFollowings(
        @Query("size") size: Int = 10,
        @Query("cursor") cursor: String? = null
    ): BaseResponse<MyFollowingsResponse>

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
}