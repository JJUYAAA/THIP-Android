package com.texthip.thip.ui.group.viewmodel

import androidx.lifecycle.ViewModel
import com.texthip.thip.R
import com.texthip.thip.ui.group.myroom.mock.GroupCardData
import com.texthip.thip.ui.group.myroom.mock.GroupCardItemRoomData
import com.texthip.thip.ui.group.myroom.mock.GroupRoomSectionData
import com.texthip.thip.ui.group.myroom.mock.GroupRoomData
import com.texthip.thip.ui.group.myroom.mock.GroupBookData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GroupViewModel : ViewModel() {

    private val _myGroups = MutableStateFlow<List<GroupCardData>>(emptyList())
    val myGroups: StateFlow<List<GroupCardData>> = _myGroups

    private val _roomSections = MutableStateFlow<List<GroupRoomSectionData>>(emptyList())
    val roomSections: StateFlow<List<GroupRoomSectionData>> = _roomSections

    private val _userName = MutableStateFlow("규빈")  // 임시 유저 이름
    val userName: StateFlow<String> = _userName

    private val _doneGroups = MutableStateFlow<List<GroupCardItemRoomData>>(emptyList())
    val doneGroups: StateFlow<List<GroupCardItemRoomData>> = _doneGroups

    private val _myRoomGroups = MutableStateFlow<List<GroupCardItemRoomData>>(emptyList())
    val myRoomGroups: StateFlow<List<GroupCardItemRoomData>> = _myRoomGroups

    private val _searchGroups = MutableStateFlow<List<GroupCardItemRoomData>>(emptyList())
    val searchGroups: StateFlow<List<GroupCardItemRoomData>> = _searchGroups

    private val _genres = listOf("문학", "과학·IT", "사회과학", "인문학", "예술")
    val genres: List<String> get() = _genres

    // 모든 모임방 데이터 (ID로 조회하기 위한 맵)
    private val _allRoomDetails = mutableMapOf<Int, GroupRoomData>()
    
    // 초기 데이터 세팅 (실제에선 repository/remote에서 받아옴)
    init {
        _myGroups.value = listOf(
            GroupCardData(23, "호르몬 체인지 완독하는 방", 22, R.drawable.bookcover_sample, 40, "uibowl1"),
            GroupCardData(24, "명작 읽기방", 10, R.drawable.bookcover_sample, 70, "joyce"),
            GroupCardData(25, "또 다른 방", 13, R.drawable.bookcover_sample, 10, "other")
        )

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

        _roomSections.value = listOf(
            GroupRoomSectionData(
                title = "마감 임박한 독서 모임방",
                rooms = deadlineRooms,
                genres = _genres
            ),
            GroupRoomSectionData(
                title = "인기 있는 독서 모임방",
                rooms = popularRooms,
                genres = _genres
            ),
            GroupRoomSectionData(
                title = "인플루언서·작가 독서 모임방",
                rooms = influencerRooms,
                genres = _genres
            )
        )

        // 완료된 모임방 데이터
        _doneGroups.value = listOf(
            GroupCardItemRoomData(18, "완료된 독서 모임방 1", 15, 20, false, null, R.drawable.bookcover_sample, 0),
            GroupCardItemRoomData(19, "완료된 독서 모임방 2", 25, 30, false, null, R.drawable.bookcover_sample, 1),
            GroupCardItemRoomData(20, "완료된 독서 모임방 3", 12, 15, false, null, R.drawable.bookcover_sample, 2),
            GroupCardItemRoomData(21, "호르몬 체인지 완독한 방", 22, 22, false, null, R.drawable.bookcover_sample, 0),
            GroupCardItemRoomData(22, "명작 읽기방 완료", 10, 10, false, null, R.drawable.bookcover_sample, 0)
        )

        // 내 모임방 데이터
        _myRoomGroups.value = listOf(
            GroupCardItemRoomData(23, "호르몬 체인지 완독하는 방", 22, 30, true, 5, R.drawable.bookcover_sample, 0),
            GroupCardItemRoomData(24, "명작 읽기방", 10, 20, true, 3, R.drawable.bookcover_sample, 0),
            GroupCardItemRoomData(25, "또 다른 방", 13, 25, false, 10, R.drawable.bookcover_sample, 1),
            GroupCardItemRoomData(26, "내가 참여한 과학책방", 18, 25, true, 7, R.drawable.bookcover_sample, 1),
            GroupCardItemRoomData(27, "인문학 토론방", 12, 20, true, 2, R.drawable.bookcover_sample, 3)
        )

        // 검색용 모임방 데이터 (모든 모임방 합쳐서)
        _searchGroups.value = deadlineRooms + popularRooms + influencerRooms
        
        // 상세 모임방 데이터 초기화 (모든 모임방 데이터 포함)
        val allRooms = deadlineRooms + popularRooms + influencerRooms + _doneGroups.value + _myRoomGroups.value
        initializeRoomDetails(allRooms)
    }

    fun onMyGroupCardClick(
        data: GroupCardData,
        onNavigateToRoom: (Int) -> Unit
    ) {
        // 내 모임방은 진행중인 방으로 이동
        onNavigateToRoom(data.id)
    }

    fun onRoomCardClick(
        data: GroupCardItemRoomData, 
        onNavigateToRecruit: (Int) -> Unit,
        onNavigateToRoom: (Int) -> Unit
    ) {
        // 방 카드 클릭 (상세 진입)
        if (data.isRecruiting) {
            onNavigateToRecruit(data.id)
        } else {
            onNavigateToRoom(data.id)
        }
    }

    // 상세 모임방 데이터 초기화 (임시 예시)
    private fun initializeRoomDetails(rooms: List<GroupCardItemRoomData>) {
        rooms.forEach { room ->
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
                genre = _genres[room.genreIndex],
                bookData = bookData,
                recommendations = getRecommendations(room.id)
            )
            
            _allRoomDetails[room.id] = roomDetail
        }
    }
    
    // 추천 모임방 가져오기 (간단한 더미 로직)
    private fun getRecommendations(roomId: Int): List<GroupCardItemRoomData> {
        return _searchGroups.value.filter { it.id != roomId }.take(3)
    }
    
    // ID로 모임방 상세 정보 조회
    fun getRoomDetail(roomId: Int): GroupRoomData? {
        return _allRoomDetails[roomId]
    }

}