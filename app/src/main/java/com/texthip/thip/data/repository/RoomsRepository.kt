package com.texthip.thip.data.repository

import android.R.attr.type
import com.texthip.thip.data.manager.GenreManager
import com.texthip.thip.data.manager.UserDataManager
import com.texthip.thip.data.manager.Genre
import com.texthip.thip.data.model.base.handleBaseResponse
import com.texthip.thip.data.model.rooms.request.CreateRoomRequest
import com.texthip.thip.data.model.rooms.request.RoomJoinRequest
import com.texthip.thip.data.model.rooms.request.RoomSecreteRoomRequest
import com.texthip.thip.data.model.rooms.request.RoomsCreateVoteRequest
import com.texthip.thip.data.model.rooms.request.RoomsPostsLikesRequest
import com.texthip.thip.data.model.rooms.request.RoomsRecordRequest
import com.texthip.thip.data.model.rooms.request.RoomsVoteRequest
import com.texthip.thip.data.model.rooms.request.VoteItem
import com.texthip.thip.data.model.rooms.response.JoinedRoomListResponse
import com.texthip.thip.data.model.rooms.response.MyRoomListResponse
import com.texthip.thip.data.model.rooms.response.RoomMainList
import com.texthip.thip.data.model.rooms.response.RoomRecruitingResponse
import com.texthip.thip.data.model.rooms.response.RoomSecreteRoomResponse
import com.texthip.thip.data.model.rooms.response.RoomCloseResponse
import com.texthip.thip.data.service.RoomsService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomsRepository @Inject constructor(
    private val roomsService: RoomsService,
    private val genreManager: GenreManager,
    private val userDataManager: UserDataManager
) {
    
    /** 장르 목록 조회 */
    fun getGenres(): Result<List<Genre>> {
        return Result.success(genreManager.getGenres())
    }
    
    /** 사용자 이름 조회(캐싱 데이터 사용)*/
    fun getUserName(): Result<String> {
        return Result.success(userDataManager.getUserName())
    }
    
    /** 내가 참여 중인 모임방 목록 조회 */
    suspend fun getMyJoinedRooms(page: Int): Result<JoinedRoomListResponse?> = runCatching {
        val response = roomsService.getJoinedRooms(page)
            .handleBaseResponse()
            .getOrThrow()
        
        response?.let { joinedRoomsDto ->
            userDataManager.cacheUserName(joinedRoomsDto.nickname)
        }
        response
    }

    /** 카테고리별 모임방 섹션 조회 (마감임박/인기) */
    suspend fun getRoomSections(genre: Genre? = null): Result<RoomMainList?> = runCatching {
        val selectedGenre = genre ?: genreManager.getDefaultGenre()
        val apiCategory = genreManager.mapGenreToApiCategory(selectedGenre)
        
        roomsService.getRooms(apiCategory)
            .handleBaseResponse()
            .getOrThrow()
    }

    /** 타입별 내 모임방 목록 조회 */
    suspend fun getMyRoomsByType(type: String?, cursor: String? = null): Result<MyRoomListResponse?> = runCatching {
        roomsService.getMyRooms(type, cursor)
            .handleBaseResponse()
            .getOrThrow()
    }
    
    /** 모집중인 모임방 상세 정보 조회 */
    suspend fun getRoomRecruiting(roomId: Int): Result<RoomRecruitingResponse> = runCatching {
        roomsService.getRoomRecruiting(roomId)
            .handleBaseResponse()
            .getOrThrow()
            ?: throw NoSuchElementException("모집중인 모임방 정보를 찾을 수 없습니다.")
    }

    /** 새 모임방 생성 */
    suspend fun createRoom(request: CreateRoomRequest): Result<Int> = runCatching {
        val response = roomsService.createRoom(request)
            .handleBaseResponse()
            .getOrThrow()
            ?: throw NoSuchElementException("모임방 생성 응답을 받을 수 없습니다.")
        response.roomId
    }

    /** 모임방 참여 또는 취소 */
    suspend fun joinOrCancelRoom(roomId: Int, type: String): Result<String> = runCatching {
        val request = RoomJoinRequest(type = type)
        val response = roomsService.joinOrCancelRoom(roomId, request)
            .handleBaseResponse()
            .getOrThrow()
            ?: throw NoSuchElementException("모임방 참여/취소 응답을 받을 수 없습니다.")
        response.type
    }

    /** 비밀번호 입력 */
    suspend fun postParticipateSecreteRoom(roomId: Int, password: String): Result<RoomSecreteRoomResponse> = runCatching {
        val request = RoomSecreteRoomRequest(password = password)
        val response = roomsService.postParticipateSecreteRoom(roomId, request)
            .handleBaseResponse()
            .getOrThrow()
            ?: throw NoSuchElementException("비밀번호 입력 응답을 받을 수 없습니다.")

        response
    }

    /** 모집 마감 */
    suspend fun closeRoom(roomId: Int): Result<RoomCloseResponse> = runCatching {
        val response = roomsService.closeRoom(roomId)
            .handleBaseResponse()
            .getOrThrow()
            ?: throw NoSuchElementException("모집 마감 응답을 받을 수 없습니다.")
        response
    }



    /** 기록장 API들 */
    suspend fun getRoomsPlaying(
        roomId: Int
    ) = runCatching {
        roomsService.getRoomsPlaying(
            roomId = roomId
        ).handleBaseResponse().getOrThrow()
    }

    suspend fun getRoomsUsers(
        roomId: Int
    ) = runCatching {
        roomsService.getRoomsUsers(
            roomId = roomId
        ).handleBaseResponse().getOrThrow()
    }

    suspend fun getRoomsPosts(
        roomId: Int,
        type: String = "group",
        sort: String? = "latest",
        pageStart: Int? = null,
        pageEnd: Int? = null,
        isOverview: Boolean? = false,
        isPageFilter: Boolean? = false,
        cursor: String? = null,
    ) = runCatching {
        roomsService.getRoomsPosts(
            roomId = roomId,
            type = type,
            sort = sort,
            pageStart = pageStart,
            pageEnd = pageEnd,
            isOverview = isOverview,
            isPageFilter = isPageFilter,
            cursor = cursor
        ).handleBaseResponse().getOrThrow()
    }

    suspend fun postRoomsRecord(
        roomId: Int,
        content: String,
        isOverview: Boolean = false,
        page: Int = 0
    ) = runCatching {
        roomsService.postRoomsRecord(
            roomId = roomId,
            request = RoomsRecordRequest(
                page = page,
                isOverview = isOverview,
                content = content
            )
        ).handleBaseResponse().getOrThrow()
    }

    suspend fun postRoomsCreateVote(
        roomId: Int,
        page: Int,
        isOverview: Boolean,
        content: String,
        voteItemList: List<VoteItem>
    ) = runCatching {
        roomsService.postRoomsCreateVote(
            roomId = roomId,
            request = RoomsCreateVoteRequest(
                page = page,
                isOverview = isOverview,
                content = content,
                voteItemList = voteItemList
            )
        ).handleBaseResponse().getOrThrow()
    }

    suspend fun getRoomsBookPage(
        roomId: Int,
    ) = runCatching {
        roomsService.getRoomsBookPage(
            roomId = roomId
        ).handleBaseResponse().getOrThrow()
    }

    suspend fun postRoomsVote(
        roomId: Int,
        voteId: Int,
        voteItemId: Int,
        type: Boolean
    ) = runCatching {
        roomsService.postRoomsVote(
            roomId = roomId,
            voteId = voteId,
            request = RoomsVoteRequest(
                voteItemId = voteItemId,
                type = type
            )
        ).handleBaseResponse().getOrThrow()
    }

    suspend fun deleteRoomsRecord(
        roomId: Int,
        recordId: Int
    ) = runCatching {
        roomsService.deleteRoomsRecord(
            roomId = roomId,
            recordId = recordId
        ).handleBaseResponse().getOrThrow()
    }

    suspend fun postRoomsPostsLikes(
        postId: Int,
        type: Boolean,
        roomPostType: String
    ) = runCatching {
        roomsService.postRoomsPostsLikes(
            postId = postId,
            request = RoomsPostsLikesRequest(
                type = type,
                roomPostType = roomPostType
            )
        ).handleBaseResponse().getOrThrow()
    }
}