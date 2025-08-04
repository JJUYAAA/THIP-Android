package com.texthip.thip.data.model.service

import com.texthip.thip.data.model.base.BaseResponse
import com.texthip.thip.data.model.group.response.JoinedRoomsDto
import com.texthip.thip.data.model.group.response.RoomsHomeDto
import retrofit2.http.GET
import retrofit2.http.Query

interface GroupService {

    @GET("rooms/home/joined")
    suspend fun getJoinedRooms(
        @Query("page") page: Int = 1
    ): BaseResponse<JoinedRoomsDto>

    @GET("rooms")
    suspend fun getRooms(
        @Query("category") category: String = "문학"   // 디폴트=문학
    ): BaseResponse<RoomsHomeDto>

    @GET("user/name")
    suspend fun getUserName(): BaseResponse<String>

    @GET("groups/done")
    suspend fun getDoneGroups(): BaseResponse<List<Any>> // TODO: 실제 Response 모델로 교체

    @GET("groups/my-rooms")
    suspend fun getMyRoomGroups(): BaseResponse<List<Any>> // TODO: 실제 Response 모델로 교체

    @GET("groups/search")
    suspend fun searchRooms(@Query("query") query: String): BaseResponse<List<Any>> // TODO: 실제 Response 모델로 교체

    @GET("groups/room")
    suspend fun getRoomDetail(@Query("roomId") roomId: Int): BaseResponse<Any> // TODO: 실제 Response 모델로 교체

    @GET("groups/genres")
    suspend fun getGenres(): BaseResponse<List<String>>
}