package com.texthip.thip.data.service

import com.texthip.thip.data.model.base.BaseResponse
import com.texthip.thip.data.model.rooms.response.RoomsUsersResponse
import com.texthip.thip.data.model.users.MyFollowingsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {
    @GET("users/my-followings")
    suspend fun getMyFollowings(
        @Query("size") size: Int = 10,
        @Query("cursor") cursor: String? = null
    ): BaseResponse<MyFollowingsResponse>

}