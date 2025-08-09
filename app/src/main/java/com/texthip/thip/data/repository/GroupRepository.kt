package com.texthip.thip.data.repository

import com.texthip.thip.data.manager.GenreManager
import com.texthip.thip.data.manager.UserDataManager
import com.texthip.thip.data.model.base.handleBaseResponse
import com.texthip.thip.data.model.group.request.CreateRoomRequest
import com.texthip.thip.data.model.group.request.RoomJoinRequest
import com.texthip.thip.data.model.group.response.JoinedRoomListResponse
import com.texthip.thip.data.model.group.response.MyRoomListResponse
import com.texthip.thip.data.model.group.response.RoomMainList
import com.texthip.thip.data.model.group.response.RoomRecruitingResponse
import com.texthip.thip.data.service.GroupService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupRepository @Inject constructor(
    private val groupService: GroupService,
    private val genreManager: GenreManager,
    private val userDataManager: UserDataManager
) {
    
    /** 장르 목록 조회 */
    fun getGenres(): Result<List<String>> {
        return Result.success(genreManager.getGenres())
    }
    
    /** 사용자 이름 조회(캐싱 데이터 사용)*/
    fun getUserName(): Result<String> {
        return Result.success(userDataManager.getUserName())
    }
    
    /** 내가 참여 중인 모임방 목록 조회 */
    suspend fun getMyJoinedRooms(page: Int): Result<JoinedRoomListResponse?> = runCatching {
        val response = groupService.getJoinedRooms(page)
            .handleBaseResponse()
            .getOrThrow()
        
        response?.let { joinedRoomsDto ->
            userDataManager.cacheUserName(joinedRoomsDto.nickname)
        }
        
        response
    }

    /** 카테고리별 모임방 섹션 조회 (마감임박/인기) */
    suspend fun getRoomSections(category: String = ""): Result<RoomMainList?> = runCatching {
        val finalCategory = category.ifEmpty { genreManager.getDefaultGenre() }
        val apiCategory = genreManager.mapGenreToApiCategory(finalCategory)
        
        groupService.getRooms(apiCategory)
            .handleBaseResponse()
            .getOrThrow()
    }

    /** 타입별 내 모임방 목록 조회 */
    suspend fun getMyRoomsByType(type: String?, cursor: String? = null): Result<MyRoomListResponse?> = runCatching {
        groupService.getMyRooms(type, cursor)
            .handleBaseResponse()
            .getOrThrow()
    }
    
    /** 모집중인 모임방 상세 정보 조회 */
    suspend fun getRoomRecruiting(roomId: Int): Result<RoomRecruitingResponse> = runCatching {
        groupService.getRoomRecruiting(roomId)
            .handleBaseResponse()
            .getOrThrow()!!
    }

    /** 새 모임방 생성 */
    suspend fun createRoom(request: CreateRoomRequest): Result<Int> = runCatching {
        groupService.createRoom(request)
            .handleBaseResponse()
            .getOrThrow()!!
            .roomId
    }

    /** 모임방 참여 또는 취소 */
    suspend fun joinOrCancelRoom(roomId: Int, type: String): Result<String> = runCatching {
        val request = RoomJoinRequest(type = type)
        groupService.joinOrCancelRoom(roomId, request)
            .handleBaseResponse()
            .getOrThrow()!!
            .type
    }
}