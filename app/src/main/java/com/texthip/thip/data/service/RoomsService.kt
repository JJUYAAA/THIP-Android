package com.texthip.thip.data.service

import com.texthip.thip.data.model.base.BaseResponse
import com.texthip.thip.data.model.rooms.request.RoomsRecordRequest
import com.texthip.thip.data.model.rooms.request.RoomsCreateVoteRequest
import com.texthip.thip.data.model.rooms.response.RoomsBookPageResponse
import com.texthip.thip.data.model.rooms.response.RoomsPlayingResponse
import com.texthip.thip.data.model.rooms.response.RoomsPostsResponse
import com.texthip.thip.data.model.rooms.response.RoomsRecordResponse
import com.texthip.thip.data.model.rooms.response.RoomsUsersResponse
import com.texthip.thip.data.model.rooms.response.RoomsCreateVoteResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RoomsService {
    @GET("rooms/{roomId}/playing")
    suspend fun getRoomsPlaying(
        @Path("roomId") roomId: Int
    ): BaseResponse<RoomsPlayingResponse>

    @GET("rooms/{roomId}/users")
    suspend fun getRoomsUsers(
        @Path("roomId") roomId: Int
    ): BaseResponse<RoomsUsersResponse>

    @GET("rooms/{roomId}/posts")
    suspend fun getRoomsPosts(
        @Path("roomId") roomId: Int,
        @Query("type") type: String = "group",
        @Query("sort") sort: String? = "latest",
        @Query("pageStart") pageStart: Int? = null,
        @Query("pageEnd") pageEnd: Int? = null,
        @Query("isOverview") isOverview: Boolean? = false,
        @Query("isPageFilter") isPageFilter: Boolean? = false,
        @Query("cursor") cursor: String? = null,
    ): BaseResponse<RoomsPostsResponse>

    @POST("rooms/{roomId}/record")
    suspend fun postRoomsRecord(
        @Path("roomId") roomId: Int,
        @Body request: RoomsRecordRequest
    ): BaseResponse<RoomsRecordResponse>

    @POST("rooms/{roomId}/vote")
    suspend fun postRoomsCreateVote(
        @Path("roomId") roomId: Int,
        @Body request: RoomsCreateVoteRequest
    ): BaseResponse<RoomsCreateVoteResponse>

    @GET("rooms/{roomId}/book-page")
    suspend fun getRoomsBookPage(
        @Path("roomId") roomId: Int,
    ): BaseResponse<RoomsBookPageResponse>
}