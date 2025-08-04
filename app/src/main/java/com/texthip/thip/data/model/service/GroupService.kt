package com.texthip.thip.data.model.service

import com.texthip.thip.data.model.base.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GroupService {
    
    // TODO: 실제 API 엔드포인트로 교체 필요
    @GET("groups/my")
    suspend fun getMyGroups(): BaseResponse<List<Any>> // TODO: 실제 Response 모델로 교체
    
    @GET("groups/rooms")
    suspend fun getRoomSections(): BaseResponse<List<Any>> // TODO: 실제 Response 모델로 교체
    
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