package com.texthip.thip.ui.group.data.repository

import com.texthip.thip.R
import com.texthip.thip.ui.group.myroom.mock.GroupBookData
import com.texthip.thip.ui.group.myroom.mock.GroupCardData
import com.texthip.thip.ui.group.myroom.mock.GroupCardItemRoomData
import com.texthip.thip.ui.group.myroom.mock.GroupRoomData
import com.texthip.thip.ui.group.myroom.mock.GroupRoomSectionData
import kotlinx.coroutines.delay

// GroupRepository의 구현
// 실제로는 서버의 API와 통신할 거라서 다 삭제하고 함수 구조만 유지한 채 수정하면 될 듯 합니다.

class GroupRepositoryImpl : GroupRepository {
    
    private val genres = listOf("문학", "과학·IT", "사회과학", "인문학", "예술")
    private val roomDetailsCache = mutableMapOf<Int, GroupRoomData>()
    
    override suspend fun getUserName(): Result<String> {
        return try {
            Result.success("규빈")    // 임시 이름
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getMyGroups(): Result<List<GroupCardData>> {
        return try {
            delay(200)
            val myGroups = listOf(
                GroupCardData(23, "호르몬 체인지 완독하는 방", 22, R.drawable.bookcover_sample, 40, "uibowl1"),
                GroupCardData(24, "명작 읽기방", 10, R.drawable.bookcover_sample, 70, "joyce"),
                GroupCardData(25, "또 다른 방", 13, R.drawable.bookcover_sample, 10, "other")
            )
            Result.success(myGroups)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getRoomSections(): Result<List<GroupRoomSectionData>> {
        return try {

            // 마감 임박한 독서 모임방
            val deadlineRooms = listOf(
                GroupCardItemRoomData(1, "시집만 읽는 사람들 3월", 22, 30, true, 3, R.drawable.bookcover_sample, 0),
                GroupCardItemRoomData(2, "일본 소설 좋아하는 사람들", 15, 20, true, 2, R.drawable.bookcover_sample, 0),
                GroupCardItemRoomData(3, "명작 같이 읽기방", 22, 30, true, 3, R.drawable.bookcover_sample, 0),
                GroupCardItemRoomData(4, "물리책 읽는 방", 13, 20, true, 1, R.drawable.bookcover_sample, 1),
                GroupCardItemRoomData(5, "코딩 과학 동아리", 12, 15, true, 5, R.drawable.bookcover_sample, 1),
                GroupCardItemRoomData(6, "사회과학 인문 탐구", 8, 12, true, 4, R.drawable.bookcover_sample, 2)
            )
            
            // 인기 있는 독서 모임방
            val popularRooms = listOf(
                GroupCardItemRoomData(7, "베스트셀러 토론방", 28, 30, true, 7, R.drawable.bookcover_sample, 0),
                GroupCardItemRoomData(8, "인기 소설 완독방", 25, 25, false, 5, R.drawable.bookcover_sample, 0),
                GroupCardItemRoomData(9, "트렌드 과학서 읽기", 20, 25, true, 10, R.drawable.bookcover_sample, 1),
                GroupCardItemRoomData(10, "화제의 경영서", 18, 20, true, 8, R.drawable.bookcover_sample, 2),
                GroupCardItemRoomData(11, "인기 철학서 모임", 15, 18, true, 12, R.drawable.bookcover_sample, 3),
                GroupCardItemRoomData(12, "예술서 베스트", 12, 15, true, 6, R.drawable.bookcover_sample, 4)
            )
            
            // 인플루언서, 작가 독서 모임방
            val influencerRooms = listOf(
                GroupCardItemRoomData(13, "작가와 함께하는 독서방", 30, 30, false, 14, R.drawable.bookcover_sample, 0),
                GroupCardItemRoomData(14, "유명 북튜버와 읽기", 18, 20, true, 8, R.drawable.bookcover_sample, 2),
                GroupCardItemRoomData(15, "작가 초청 인문학방", 15, 20, true, 12, R.drawable.bookcover_sample, 3),
                GroupCardItemRoomData(16, "인플루언서 과학책", 22, 25, true, 9, R.drawable.bookcover_sample, 1),
                GroupCardItemRoomData(17, "유명작가 예술론", 16, 18, true, 11, R.drawable.bookcover_sample, 4)
            )
            
            val sections = listOf(
                GroupRoomSectionData(
                    title = "마감 임박한 독서 모임방",
                    rooms = deadlineRooms,
                    genres = genres
                ),
                GroupRoomSectionData(
                    title = "인기 있는 독서 모임방",
                    rooms = popularRooms,
                    genres = genres
                ),
                GroupRoomSectionData(
                    title = "인플루언서·작가 독서 모임방",
                    rooms = influencerRooms,
                    genres = genres
                )
            )

            // 상세 데이터 캐시에 저장
            (deadlineRooms + popularRooms + influencerRooms).forEach { room ->
                initializeRoomDetail(room)
            }
            
            Result.success(sections)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getDoneGroups(): Result<List<GroupCardItemRoomData>> {
        return try {
            val doneGroups = listOf(
                GroupCardItemRoomData(18, "완료된 독서 모임방 1", 15, 20, false, null, R.drawable.bookcover_sample, 0),
                GroupCardItemRoomData(19, "완료된 독서 모임방 2", 25, 30, false, null, R.drawable.bookcover_sample, 1),
                GroupCardItemRoomData(20, "완료된 독서 모임방 3", 12, 15, false, null, R.drawable.bookcover_sample, 2),
                GroupCardItemRoomData(21, "호르몬 체인지 완독한 방", 22, 22, false, null, R.drawable.bookcover_sample, 0),
                GroupCardItemRoomData(22, "명작 읽기방 완료", 10, 10, false, null, R.drawable.bookcover_sample, 0)
            )

            // 상세 데이터 캐시에 저장
            doneGroups.forEach { room ->
                initializeRoomDetail(room)
            }
            
            Result.success(doneGroups)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getMyRoomGroups(): Result<List<GroupCardItemRoomData>> {
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
    
    override suspend fun getSearchGroups(): Result<List<GroupCardItemRoomData>> {
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
    
    override suspend fun getRoomDetail(roomId: Int): Result<GroupRoomData> {
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
    
    override suspend fun searchRooms(query: String): Result<List<GroupCardItemRoomData>> {
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
    
    override suspend fun getGenres(): Result<List<String>> {
        return try {
            delay(50)
            Result.success(genres)
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
            recommendations = getRecommendations(room.id)
        )
        
        roomDetailsCache[room.id] = roomDetail
    }

    private fun getRecommendations(roomId: Int): List<GroupCardItemRoomData> {
        return emptyList()
    }
}