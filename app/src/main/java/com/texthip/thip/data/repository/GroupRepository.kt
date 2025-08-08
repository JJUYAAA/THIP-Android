package com.texthip.thip.data.repository

import android.content.Context
import com.texthip.thip.R
import com.texthip.thip.data.manager.GenreManager
import com.texthip.thip.data.manager.UserDataManager
import com.texthip.thip.data.mapper.GroupDataMapper
import com.texthip.thip.data.model.base.handleBaseResponse
import com.texthip.thip.data.model.group.request.CreateRoomRequest
import com.texthip.thip.data.model.group.request.RoomJoinRequest
import com.texthip.thip.data.model.group.response.PaginationResult
import com.texthip.thip.data.service.GroupService
import com.texthip.thip.ui.group.done.mock.MyRoomsPaginationResult
import com.texthip.thip.ui.group.myroom.mock.GroupRoomSectionData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupRepository @Inject constructor(
    private val groupService: GroupService,
    private val groupDataMapper: GroupDataMapper,
    private val genreManager: GenreManager,
    private val userDataManager: UserDataManager,
    @param:ApplicationContext private val context: Context
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
    suspend fun getMyJoinedRooms(page: Int) = runCatching {
        groupService.getJoinedRooms(page)
            .handleBaseResponse()
            .getOrThrow()
            ?.let { joinedRoomsDto ->
                userDataManager.cacheUserName(joinedRoomsDto.nickname)
                
                val groups = joinedRoomsDto.roomList.map { dto ->
                    groupDataMapper.toGroupCardData(dto, joinedRoomsDto.nickname)
                }

                PaginationResult(
                    data = groups,
                    hasMore = !joinedRoomsDto.last,
                    currentPage = joinedRoomsDto.page,
                    nickname = joinedRoomsDto.nickname
                )
            } ?: PaginationResult(
                data = emptyList(),
                hasMore = false,
                currentPage = page,
                nickname = ""
            )
    }

    /** 카테고리별 모임방 섹션 조회 (마감임박/인기) */
    suspend fun getRoomSections(category: String = "") = runCatching {
        val finalCategory = category.ifEmpty { genreManager.getDefaultGenre() }
        val apiCategory = genreManager.mapGenreToApiCategory(finalCategory)
        
        groupService.getRooms(apiCategory)
            .handleBaseResponse()
            .getOrThrow()
            ?.let { roomsData ->
                listOf(
                    GroupRoomSectionData(
                        title = context.getString(R.string.room_section_deadline),
                        rooms = roomsData.deadlineRoomList.map { dto -> 
                            val daysLeft = groupDataMapper.extractDaysFromDeadline(dto.deadlineDate)
                            groupDataMapper.toGroupCardItemRoomData(dto, daysLeft)
                        },
                        genres = genreManager.getGenres()
                    ),
                    GroupRoomSectionData(
                        title = context.getString(R.string.room_section_popular), 
                        rooms = roomsData.popularRoomList.map { dto ->
                            val daysLeft = groupDataMapper.extractDaysFromDeadline(dto.deadlineDate)
                            groupDataMapper.toGroupCardItemRoomData(dto, daysLeft)
                        },
                        genres = genreManager.getGenres()
                    )
                )
            } ?: emptyList()
    }

    /** 타입별 내 모임방 목록 조회 */
    suspend fun getMyRoomsByType(type: String?, cursor: String? = null) = runCatching {
        groupService.getMyRooms(type, cursor)
            .handleBaseResponse()
            .getOrThrow()
?.let { data ->
                val myRoomCards = data.roomList.map { room ->
                    groupDataMapper.toMyRoomCardData(room)
                }

                MyRoomsPaginationResult(
                    data = myRoomCards,
                    nextCursor = data.nextCursor,
                    isLast = data.isLast
                )
            } ?: MyRoomsPaginationResult(
                data = emptyList(),
                nextCursor = null,
                isLast = true
            )
    }
    
    /** 모집중인 모임방 상세 정보 조회 */
    suspend fun getRoomRecruiting(roomId: Int) = runCatching {
        groupService.getRoomRecruiting(roomId)
            .handleBaseResponse()
            .getOrThrow()!!
            .let { data ->
                groupDataMapper.toGroupRoomData(data)
            }
    }

    /** 새 모임방 생성 */
    suspend fun createRoom(request: CreateRoomRequest) = runCatching {
        groupService.createRoom(request)
            .handleBaseResponse()
            .getOrThrow()!!
            .roomId
    }

    /** 모임방 참여 또는 취소 */
    suspend fun joinOrCancelRoom(roomId: Int, type: String) = runCatching {
        val request = RoomJoinRequest(type = type)
        groupService.joinOrCancelRoom(roomId, request)
            .handleBaseResponse()
            .getOrThrow()!!
            .type
    }
}