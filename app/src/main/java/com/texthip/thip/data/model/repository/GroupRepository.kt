package com.texthip.thip.data.model.repository

import android.content.Context
import com.texthip.thip.R
import com.texthip.thip.data.model.base.handleBaseResponse
import com.texthip.thip.data.model.group.response.PaginationResult
import com.texthip.thip.data.model.group.response.RoomListDto
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
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupRepository @Inject constructor(
    private val groupService: GroupService,
    @ApplicationContext private val context: Context
) {
    private val genres = listOf(
        context.getString(R.string.literature),
        context.getString(R.string.science_it),
        context.getString(R.string.social_science),
        context.getString(R.string.humanities),
        context.getString(R.string.art)
    )
    private val roomDetailsCache = mutableMapOf<Int, GroupRoomData>()
    private var cachedUserName: String? = null
    
    // UI 장르명 → API 카테고리명 매핑
    private fun mapGenreToApiCategory(genre: String): String {
        return when (genre) {
            "과학·IT" -> "과학/IT"
            else -> genre
        }
    }
    
    fun getUserName(): Result<String> {
        return try {
            val name = cachedUserName ?: "사용자" // 캐시된 이름이 없으면 기본값
            Result.success(name)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getMyJoinedRooms(page: Int): Result<PaginationResult<GroupCardData>> {
        return try {
            groupService.getJoinedRooms(page)
                .handleBaseResponse()
                .mapCatching { data ->
                    data?.let { joinedRoomsDto ->
                        // API 응답에서 받은 닉네임을 캐시에 저장
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
                                rooms = roomsData.deadline.map { dto -> 
                                    convertToGroupCardItemRoomData(dto, extractDaysFromDeadline(dto.deadlineDate))
                                },
                                genres = genres
                            ),
                            GroupRoomSectionData(
                                title = context.getString(R.string.room_section_popular), 
                                rooms = roomsData.popularity.map { dto ->
                                    convertToGroupCardItemRoomData(dto, extractDaysFromDeadline(dto.deadlineDate))
                                },
                                genres = genres
                            ),
                            GroupRoomSectionData(
                                title = context.getString(R.string.room_section_influencer),
                                rooms = roomsData.influencer.map { dto ->
                                    convertToGroupCardItemRoomData(dto, extractDaysFromDeadline(dto.deadlineDate))
                                },
                                genres = genres
                            )
                        )
                        
                        // 상세 데이터 캐시에 저장
                        val allRooms = sections.flatMap { it.rooms }
                        allRooms.forEach { room ->
                            initializeRoomDetail(room)
                        }
                        
                        sections
                    }.orEmpty()
                }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // 완료된 모임방 API 연동
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
                                bookTitle = room.bookTitle,
                                memberCount = room.memberCount,
                                endDate = room.endDate
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
    
    private fun convertToGroupCardItemRoomData(dto: RoomListDto, daysLeft: Int): GroupCardItemRoomData {
        return GroupCardItemRoomData(
            id = dto.roomId,
            title = dto.bookTitle,
            participants = dto.memberCount,
            maxParticipants = dto.memberCount + 10, // API에서 maxParticipants를 제공하지 않으므로 임시로 +10
            isRecruiting = true,
            endDate = daysLeft,
            imageRes = null,
            genreIndex = 0,
            isSecret = false
        )
    }
    
    private fun extractDaysFromDeadline(deadlineDate: String): Int {
        return when {
            deadlineDate.contains("일 뒤") -> {
                deadlineDate.replace("일 뒤", "").trim().toIntOrNull() ?: 0
            }
            else -> 0 // 파싱할 수 없는 경우 0 반환
        }
    }
    
    suspend fun getMyRoomGroups(): Result<List<GroupCardItemRoomData>> {
        return try {
            val myRoomGroups = listOf(
                GroupCardItemRoomData(23, "호르몬 체인지 완독하는 방", 22, 30, true, 5, R.drawable.bookcover_sample, 0),
                GroupCardItemRoomData(24, "명작 읽기방", 10, 20, true, 3, R.drawable.bookcover_sample, 0),
                GroupCardItemRoomData(25, "또 다른 방", 13, 25, false, 10, R.drawable.bookcover_sample, 1),
                GroupCardItemRoomData(26, "내가 참여한 과학책방", 18, 25, true, 7, R.drawable.bookcover_sample, 1),
                GroupCardItemRoomData(27, "인문학 토론방", 12, 20, true, 2, R.drawable.bookcover_sample, 3)
            )

            // 상세 데이터 캐시에 저장
            myRoomGroups.forEach { room ->
                initializeRoomDetail(room)
            }
            
            Result.success(myRoomGroups)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getSearchGroups(): Result<List<GroupCardItemRoomData>> {
        return try {
            // 기존에 로드된 섹션 데이터들을 합쳐서 반환
            val sectionsResult = getRoomSections()
            if (sectionsResult.isSuccess) {
                val allRooms = sectionsResult.getOrThrow().flatMap { it.rooms }
                Result.success(allRooms)
            } else {
                Result.failure(sectionsResult.exceptionOrNull() ?: Exception("Failed to load search groups"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getRoomDetail(roomId: Int): Result<GroupRoomData> {
        return try {
            delay(150)
            val roomDetail = roomDetailsCache[roomId]
            if (roomDetail != null) {
                Result.success(roomDetail)
            } else {
                Result.failure(Exception("Room not found: $roomId"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // 모집중인 모임방 상세 정보 API 연동
    suspend fun getRoomRecruiting(roomId: Int): Result<GroupRoomData> {
        return try {
            groupService.getRoomRecruiting(roomId)
                .handleBaseResponse()
                .mapCatching { recruitingDto ->
                    recruitingDto?.let { data ->
                        // 책 정보 변환
                        val bookData = GroupBookData(
                            title = data.bookTitle,
                            author = data.authorName,
                            publisher = "출판사 정보 없음", // API에서 제공하지 않음
                            description = data.bookDescription
                        )
                        
                        // 추천 모임방 변환
                        val recommendations = data.recommendRooms.map { recommendDto ->
                            GroupCardItemRoomData(
                                id = recommendDto.hashCode(), // 임시 ID (실제로는 roomId가 필요)
                                title = recommendDto.roomName,
                                participants = recommendDto.memberCount,
                                maxParticipants = recommendDto.recruitCount,
                                isRecruiting = true,
                                endDate = extractDaysFromDeadline(recommendDto.recruitEndDate),
                                imageRes = null, // 이미지는 URL로 처리
                                genreIndex = 0, // 기본값
                                isSecret = true // 기본값
                            )
                        }
                        
                        // GroupRoomData로 변환
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
    
    // 버튼 타입 결정 로직
    private fun determineButtonType(isHost: Boolean, isJoining: Boolean): GroupBottomButtonType {
        return when {
            isHost -> GroupBottomButtonType.CLOSE // 호스트는 모집 마감 가능
            isJoining -> GroupBottomButtonType.CANCEL // 참여 중이면 취소 가능
            else -> GroupBottomButtonType.JOIN // 참여하지 않았으면 참여 가능
        }
    }
    
    suspend fun searchRooms(query: String): Result<List<GroupCardItemRoomData>> {
        return try {
            val searchResult = getSearchGroups()
            if (searchResult.isSuccess) {
                val filteredRooms = searchResult.getOrThrow().filter { room ->
                    room.title.contains(query, ignoreCase = true)
                }
                Result.success(filteredRooms)
            } else {
                searchResult
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun initializeRoomDetail(room: GroupCardItemRoomData) {
        val bookData = GroupBookData(
            title = "심장보다 단단한 토마토 한 알",
            author = "고선지",
            publisher = "푸른출판사",
            description = "${room.title}에서 읽는 책입니다. 감동적인 이야기로 가득한 작품입니다.",
            imageRes = room.imageRes ?: R.drawable.bookcover_sample
        )
        
        val recommendations = getRecommendations(room.id)
        
        val roomDetail = GroupRoomData(
            id = room.id,
            title = room.title,
            isSecret = room.isSecret,
            description = "${room.title} 모임입니다. 함께 책을 읽고 토론해요.",
            startDate = "2025.01.12",
            endDate = "2025.02.12",
            members = room.participants,
            maxMembers = room.maxParticipants,
            daysLeft = room.endDate ?: 0,
            genre = genres[room.genreIndex],
            bookData = bookData,
            recommendations = recommendations
        )
        
        roomDetailsCache[room.id] = roomDetail
        
        // 추천 모임방들의 상세 정보도 캐시에 추가
        recommendations.forEach { recommendedRoom ->
            if (!roomDetailsCache.containsKey(recommendedRoom.id)) {
                initializeRecommendedRoomDetail(recommendedRoom)
            }
        }
    }

    // 추천 모임방 예시 by gpt
    private fun initializeRecommendedRoomDetail(room: GroupCardItemRoomData) {
        val bookTitles = listOf(
            "데미안", "1984", "노인과 바다", "위대한 개츠비", "햄릿",
            "코스모스", "이기적 유전자", "블랙홀과 시간여행", "총균쇠",
            "국부론", "자본론", "사피엔스", "총균쇠", "정의란 무엇인가",
            "예술의 역사", "음악의 역사", "미학 오디세이"
        )
        
        val authors = listOf(
            "헤르만 헤세", "조지 오웰", "어니스트 헤밍웨이", "스콧 피츠제럴드",
            "칼 세이건", "리처드 도킨스", "킵 손", "재레드 다이아몬드",
            "아담 스미스", "칼 마르크스", "유발 하라리", "마이클 샌델"
        )
        
        val publishers = listOf("푸른출판사", "문학동네", "민음사", "창비", "열린책들", "김영사")
        
        val bookData = GroupBookData(
            title = bookTitles.random(),
            author = authors.random(),
            publisher = publishers.random(),
            description = "${room.title}에서 읽는 흥미로운 책입니다. 함께 읽으며 깊이 있는 토론을 나눠보세요.",
            imageRes = room.imageRes ?: R.drawable.bookcover_sample
        )
        
        val roomDetail = GroupRoomData(
            id = room.id,
            title = room.title,
            isSecret = room.isSecret,
            description = "${room.title} 모임입니다. 다양한 관점으로 책을 읽고 의견을 나눠보세요.",
            startDate = "2025.01.15",
            endDate = "2025.02.15",
            members = room.participants,
            maxMembers = room.maxParticipants,
            daysLeft = room.endDate ?: 0,
            genre = genres.getOrElse(room.genreIndex) { genres[0] },
            bookData = bookData,
            recommendations = getRecommendations(room.id) // 추천 모임방에도 추천 제공
        )
        
        roomDetailsCache[room.id] = roomDetail
    }

    private fun getRecommendations(roomId: Int): List<GroupCardItemRoomData> {
        // 추천 모임방 더미데이터 풀
        val recommendationPool = listOf(
            // 문학 관련 추천
            GroupCardItemRoomData(1001, "한국 근현대 소설 읽기", 18, 25, true, 3, R.drawable.bookcover_sample, 0),
            GroupCardItemRoomData(1002, "일본 문학 애호가들", 22, 30, true, 1, R.drawable.bookcover_sample, 0),
            GroupCardItemRoomData(1003, "시 읽기 모임", 16, 25, true, 2, R.drawable.bookcover_sample, 0),
            GroupCardItemRoomData(1004, "해외문학 번역서 읽기", 15, 22, true, 3, R.drawable.bookcover_sample, 0, true),
            GroupCardItemRoomData(1005, "고전 문학 탐구", 20, 25, true, 5, R.drawable.bookcover_sample, 0),
            
            // 과학·IT 관련 추천  
            GroupCardItemRoomData(1006, "SF 소설 탐험대", 12, 20, true, 7, R.drawable.bookcover_sample, 1),
            GroupCardItemRoomData(1007, "과학도서 함께 읽기", 7, 15, true, 9, R.drawable.bookcover_sample, 1),
            GroupCardItemRoomData(1008, "컴퓨터 과학 스터디", 14, 18, true, 4, R.drawable.bookcover_sample, 1),
            GroupCardItemRoomData(1009, "물리학 입문서 모임", 10, 16, true, 6, R.drawable.bookcover_sample, 1),
            
            // 사회과학 관련 추천
            GroupCardItemRoomData(1010, "경제경영서 스터디", 9, 12, true, 6, R.drawable.bookcover_sample, 2),
            GroupCardItemRoomData(1011, "사회학 도서 토론", 13, 18, true, 4, R.drawable.bookcover_sample, 2),
            GroupCardItemRoomData(1012, "정치학 입문 모임", 11, 15, true, 8, R.drawable.bookcover_sample, 2),
            
            // 인문학 관련 추천
            GroupCardItemRoomData(1013, "철학 에세이 읽기 모임", 8, 15, true, 5, R.drawable.bookcover_sample, 3),
            GroupCardItemRoomData(1014, "인문학 고전 읽기", 20, 25, true, 5, R.drawable.bookcover_sample, 3, true),
            GroupCardItemRoomData(1015, "심리학 도서 스터디", 10, 16, true, 7, R.drawable.bookcover_sample, 3),
            GroupCardItemRoomData(1016, "역사서 탐구 모임", 11, 16, true, 8, R.drawable.bookcover_sample, 3),
            
            // 예술 관련 추천
            GroupCardItemRoomData(1017, "미술사 도서 읽기", 14, 20, true, 3, R.drawable.bookcover_sample, 4),
            GroupCardItemRoomData(1018, "음악 관련 서적 모임", 12, 18, true, 5, R.drawable.bookcover_sample, 4),
            
            // 기타 장르
            GroupCardItemRoomData(1019, "로맨스 소설 감상회", 14, 20, true, 4, R.drawable.bookcover_sample, 0),
            GroupCardItemRoomData(1020, "미스터리 소설 동호회", 15, 18, true, 2, R.drawable.bookcover_sample, 0, true),
            GroupCardItemRoomData(1021, "자기계발서 함께 읽기", 25, 30, true, 3, R.drawable.bookcover_sample, 2, true),
            GroupCardItemRoomData(1022, "판타지 소설 동호회", 24, 30, true, 1, R.drawable.bookcover_sample, 0),
            GroupCardItemRoomData(1023, "여행 에세이 모임", 13, 18, true, 4, R.drawable.bookcover_sample, 3),
            GroupCardItemRoomData(1024, "추리소설 마니아", 19, 24, true, 6, R.drawable.bookcover_sample, 0)
        )
        
        // 현재 방과 관련 없는 추천을 제공하기 위해 현재 roomId와 다른 것들만 필터링
        val filteredRecommendations = recommendationPool.filter { it.id != roomId }
        
        // 랜덤하게 3-5개의 추천 반환
        return filteredRecommendations.shuffled().take(5)
    }
}