package com.texthip.thip.data.service

import com.texthip.thip.data.model.base.BaseResponse
import com.texthip.thip.data.model.book.response.BookListResponse
import com.texthip.thip.data.model.group.request.CreateRoomRequest
import com.texthip.thip.data.model.group.request.RoomJoinRequest
import com.texthip.thip.data.model.group.response.CreateRoomResponse
import com.texthip.thip.data.model.group.response.JoinedRoomListResponse
import com.texthip.thip.data.model.group.response.MyRoomListResponse
import com.texthip.thip.data.model.group.response.RoomJoinResponse
import com.texthip.thip.data.model.group.response.RoomRecruitingResponse
import com.texthip.thip.data.model.group.response.RoomMainList
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GroupService {

    /** 참여 중인 모임방 목록 조회 */
    @GET("rooms/home/joined")
    suspend fun getJoinedRooms(
        @Query("page") page: Int = 1
    ): BaseResponse<JoinedRoomListResponse>

    /** 카테고리별 모임방 목록 조회 (마감임박/인기) */
    @GET("rooms")
    suspend fun getRooms(
        @Query("category") category: String = "문학"
    ): BaseResponse<RoomMainList>

    /** 내가 만든/참여한 모임방 목록 조회 */
    @GET("rooms/my")
    suspend fun getMyRooms(
        @Query("type") type: String? = null,
        @Query("cursor") cursor: String? = null
    ): BaseResponse<MyRoomListResponse>

    /** 모집중인 모임방 상세 정보 조회 */
    @GET("rooms/{roomId}/recruiting")
    suspend fun getRoomRecruiting(@Path("roomId") roomId: Int): BaseResponse<RoomRecruitingResponse>

    /** 새 모임방 생성 */
    @POST("rooms")
    suspend fun createRoom(
        @Body request: CreateRoomRequest
    ): BaseResponse<CreateRoomResponse>

    /** 모임방 참여/취소 */
    @POST("rooms/{roomId}/join")
    suspend fun joinOrCancelRoom(
        @Path("roomId") roomId: Int,
        @Body request: RoomJoinRequest
    ): BaseResponse<RoomJoinResponse>

}