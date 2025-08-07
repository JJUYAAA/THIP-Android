package com.texthip.thip.data.model.repository

import android.content.Context
import com.texthip.thip.R
import com.texthip.thip.data.model.base.handleBaseResponse
import com.texthip.thip.data.model.group.request.CreateRoomRequest
import com.texthip.thip.data.model.group.request.RoomJoinRequest
import com.texthip.thip.data.model.group.response.PaginationResult
import com.texthip.thip.data.model.group.response.RoomMainResponse
import com.texthip.thip.data.model.service.GroupService
import com.texthip.thip.ui.group.done.mock.MyRoomCardData
import com.texthip.thip.ui.group.done.mock.MyRoomsPaginationResult
import com.texthip.thip.ui.group.myroom.mock.GroupBookData
import com.texthip.thip.ui.group.myroom.mock.GroupBottomButtonType
import com.texthip.thip.ui.group.myroom.mock.GroupCardData
import com.texthip.thip.ui.group.myroom.mock.GroupCardItemRoomData
import com.texthip.thip.ui.group.myroom.mock.GroupRoomData
import com.texthip.thip.ui.group.myroom.mock.GroupRoomSectionData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupRepository @Inject constructor(
    private val groupService: GroupService,
    @param:ApplicationContext private val context: Context
) {
    private val genres = listOf(
        context.getString(R.string.literature),
        context.getString(R.string.science_it),
        context.getString(R.string.social_science),
        context.getString(R.string.humanities),
        context.getString(R.string.art)
    )
    
    fun getGenres(): Result<List<String>> {
        return Result.success(genres)
    }
    private var cachedUserName: String? = null
    
    private fun mapGenreToApiCategory(genre: String): String {
        return when (genre) {
            "과학·IT" -> "과학/IT"
            else -> genre
        }
    }
    
    fun getUserName(): Result<String> {
        val name = cachedUserName ?: "사용자"
        return Result.success(name)
    }
    suspend fun getMyJoinedRooms(page: Int): Result<PaginationResult<GroupCardData>> {
        return try {
            groupService.getJoinedRooms(page)
                .handleBaseResponse()
                .mapCatching { data ->
                    data?.let { joinedRoomsDto ->
                        cachedUserName = joinedRoomsDto.nickname
                        
                        val groups = joinedRoomsDto.roomList.map { dto ->
                            GroupCardData(
                                id = dto.roomId,
                                title = dto.bookTitle,
                                members = dto.memberCount,
                                imageUrl = dto.bookImageUrl,
                                progress = dto.userPercentage,
                                nickname = joinedRoomsDto.nickname
                            )
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
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRoomSections(category: String = ""): Result<List<GroupRoomSectionData>> {
        return try {
            val finalCategory = category.ifEmpty { context.getString(R.string.literature) }
            val apiCategory = mapGenreToApiCategory(finalCategory)
            groupService.getRooms(apiCategory)
                .handleBaseResponse()
                .mapCatching { data ->
                    data?.let { roomsData ->
                        val sections = listOf(
                            GroupRoomSectionData(
                                title = context.getString(R.string.room_section_deadline),
                                rooms = roomsData.deadlineRoomList.map { dto -> 
                                    convertToGroupCardItemRoomData(dto, extractDaysFromDeadline(dto.deadlineDate))
                                },
                                genres = genres
                            ),
                            GroupRoomSectionData(
                                title = context.getString(R.string.room_section_popular), 
                                rooms = roomsData.popularRoomList.map { dto ->
                                    convertToGroupCardItemRoomData(dto, extractDaysFromDeadline(dto.deadlineDate))
                                },
                                genres = genres
                            )
                        )
                        sections
                    }.orEmpty()
                }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMyRoomsByType(type: String?, cursor: String? = null): Result<MyRoomsPaginationResult> {
        return try {
            groupService.getMyRooms(type, cursor)
                .handleBaseResponse()
                .mapCatching { myRoomsDto ->
                    myRoomsDto?.let { data ->
                        val myRoomCards = data.roomList.map { room ->
                            MyRoomCardData(
                                roomId = room.roomId,
                                bookImageUrl = room.bookImageUrl,
                                roomName = room.roomName,
                                recruitCount = room.recruitCount,
                                memberCount = room.memberCount,
                                endDate = room.endDate,
                                type = room.type
                            )
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
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun convertToGroupCardItemRoomData(dto: RoomMainResponse, daysLeft: Int): GroupCardItemRoomData {
        return GroupCardItemRoomData(
            id = dto.roomId,
            title = dto.roomName,
            participants = dto.memberCount,
            maxParticipants = dto.recruitCount,
            isRecruiting = true,
            endDate = daysLeft,
            imageUrl = dto.bookImageUrl,
            genreIndex = 0,
            isSecret = false
        )
    }
    
    private fun extractDaysFromDeadline(deadlineDate: String): Int {
        return when {
            deadlineDate.contains("일 뒤") -> {
                deadlineDate.replace("일 뒤", "").trim().toIntOrNull() ?: 0
            }
            else -> 0
        }
    }
    
    // TODO: 실제 검색 API 엔드포인트로 대체 필요
    suspend fun getSearchGroups(): Result<List<GroupCardItemRoomData>> {
        return Result.success(emptyList())
    }
    
    suspend fun getRoomRecruiting(roomId: Int): Result<GroupRoomData> {
        return try {
            groupService.getRoomRecruiting(roomId)
                .handleBaseResponse()
                .mapCatching { recruitingDto ->
                    recruitingDto?.let { data ->
                        val bookData = GroupBookData(
                            title = data.bookTitle,
                            author = data.authorName,
                            publisher = data.publisher,
                            description = data.bookDescription,
                            imageUrl = data.bookImageUrl
                        )
                        
                        val recommendations = data.recommendRooms.map { recommendDto ->
                            GroupCardItemRoomData(
                                id = recommendDto.roomId,
                                title = recommendDto.roomName,
                                participants = recommendDto.memberCount,
                                maxParticipants = recommendDto.recruitCount,
                                isRecruiting = true,
                                endDate = extractDaysFromDeadline(recommendDto.recruitEndDate),
                                imageUrl = recommendDto.roomImageUrl,
                                genreIndex = 0,
                            )
                        }
                        
                        GroupRoomData(
                            id = data.roomId,
                            title = data.roomName,
                            isSecret = !data.isPublic,
                            description = data.roomDescription,
                            startDate = data.progressStartDate,
                            endDate = data.progressEndDate,
                            members = data.memberCount,
                            maxMembers = data.recruitCount,
                            daysLeft = extractDaysFromDeadline(data.recruitEndDate),
                            genre = data.category,
                            bookData = bookData,
                            recommendations = recommendations,
                            buttonType = determineButtonType(data.isHost, data.isJoining),
                            roomImageUrl = data.roomImageUrl,
                            bookImageUrl = data.bookImageUrl
                        )
                    } ?: throw Exception("No recruiting data found for room $roomId")
                }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun determineButtonType(isHost: Boolean, isJoining: Boolean): GroupBottomButtonType {
        return when {
            isHost -> GroupBottomButtonType.CLOSE
            isJoining -> GroupBottomButtonType.CANCEL
            else -> GroupBottomButtonType.JOIN
        }
    }

    suspend fun createRoom(request: CreateRoomRequest): Result<Int> {
        return try {
            groupService.createRoom(request)
                .handleBaseResponse()
                .mapCatching { createRoomResponse ->
                    createRoomResponse?.roomId ?: throw Exception("Failed to create room: roomId is null")
                }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun joinOrCancelRoom(roomId: Int, type: String): Result<String> {
        return try {
            val request = RoomJoinRequest(type = type)
            groupService.joinOrCancelRoom(roomId, request)
                .handleBaseResponse()
                .mapCatching { response ->
                    response?.type ?: throw Exception("Failed to join/cancel room: no response")
                }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}