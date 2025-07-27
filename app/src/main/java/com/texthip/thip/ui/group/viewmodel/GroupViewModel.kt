package com.texthip.thip.ui.group.viewmodel

import androidx.lifecycle.ViewModel
import com.texthip.thip.R
import com.texthip.thip.ui.group.myroom.mock.GroupCardData
import com.texthip.thip.ui.group.myroom.mock.GroupCardItemRoomData
import com.texthip.thip.ui.group.myroom.mock.GroupRoomSectionData
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

    // 초기 데이터 세팅 (실제에선 repository/remote에서 받아옴)
    init {
        _myGroups.value = listOf(
            GroupCardData("호르몬 체인지 완독하는 방", 22, R.drawable.bookcover_sample, 40, "uibowl1"),
            GroupCardData("명작 읽기방", 10, R.drawable.bookcover_sample, 70, "joyce"),
            GroupCardData("또 다른 방", 13, R.drawable.bookcover_sample, 10, "other")
        )

        // 마감 임박한 독서 모임방
        val deadlineRooms = listOf(
            GroupCardItemRoomData("시집만 읽는 사람들 3월", 22, 30, true, 3, R.drawable.bookcover_sample, 0),
            GroupCardItemRoomData("일본 소설 좋아하는 사람들", 15, 20, true, 2, R.drawable.bookcover_sample, 0),
            GroupCardItemRoomData("명작 같이 읽기방", 22, 30, true, 3, R.drawable.bookcover_sample, 0),
            GroupCardItemRoomData("물리책 읽는 방", 13, 20, true, 1, R.drawable.bookcover_sample, 1),
            GroupCardItemRoomData("코딩 과학 동아리", 12, 15, true, 5, R.drawable.bookcover_sample, 1),
            GroupCardItemRoomData("사회과학 인문 탐구", 8, 12, true, 4, R.drawable.bookcover_sample, 2)
        )

        // 인기 있는 독서 모임방
        val popularRooms = listOf(
            GroupCardItemRoomData("베스트셀러 토론방", 28, 30, true, 7, R.drawable.bookcover_sample, 0),
            GroupCardItemRoomData("인기 소설 완독방", 25, 25, false, 5, R.drawable.bookcover_sample, 0),
            GroupCardItemRoomData("트렌드 과학서 읽기", 20, 25, true, 10, R.drawable.bookcover_sample, 1),
            GroupCardItemRoomData("화제의 경영서", 18, 20, true, 8, R.drawable.bookcover_sample, 2),
            GroupCardItemRoomData("인기 철학서 모임", 15, 18, true, 12, R.drawable.bookcover_sample, 3),
            GroupCardItemRoomData("예술서 베스트", 12, 15, true, 6, R.drawable.bookcover_sample, 4)
        )

        // 인플루언서, 작가 독서 모임방
        val influencerRooms = listOf(
            GroupCardItemRoomData("작가와 함께하는 독서방", 30, 30, false, 14, R.drawable.bookcover_sample, 0),
            GroupCardItemRoomData("유명 북튜버와 읽기", 18, 20, true, 8, R.drawable.bookcover_sample, 2),
            GroupCardItemRoomData("작가 초청 인문학방", 15, 20, true, 12, R.drawable.bookcover_sample, 3),
            GroupCardItemRoomData("인플루언서 과학책", 22, 25, true, 9, R.drawable.bookcover_sample, 1),
            GroupCardItemRoomData("유명작가 예술론", 16, 18, true, 11, R.drawable.bookcover_sample, 4)
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
            GroupCardItemRoomData("완료된 독서 모임방 1", 15, 20, false, null, R.drawable.bookcover_sample, 0),
            GroupCardItemRoomData("완료된 독서 모임방 2", 25, 30, false, null, R.drawable.bookcover_sample, 1),
            GroupCardItemRoomData("완료된 독서 모임방 3", 12, 15, false, null, R.drawable.bookcover_sample, 2),
            GroupCardItemRoomData("호르몬 체인지 완독한 방", 22, 22, false, null, R.drawable.bookcover_sample, 0),
            GroupCardItemRoomData("명작 읽기방 완료", 10, 10, false, null, R.drawable.bookcover_sample, 0)
        )

        // 내 모임방 데이터
        _myRoomGroups.value = listOf(
            GroupCardItemRoomData("호르몬 체인지 완독하는 방", 22, 30, true, 5, R.drawable.bookcover_sample, 0),
            GroupCardItemRoomData("명작 읽기방", 10, 20, true, 3, R.drawable.bookcover_sample, 0),
            GroupCardItemRoomData("또 다른 방", 13, 25, false, 10, R.drawable.bookcover_sample, 1),
            GroupCardItemRoomData("내가 참여한 과학책방", 18, 25, true, 7, R.drawable.bookcover_sample, 1),
            GroupCardItemRoomData("인문학 토론방", 12, 20, true, 2, R.drawable.bookcover_sample, 3)
        )

        // 검색용 모임방 데이터 (모든 모임방 합쳐서)
        _searchGroups.value = deadlineRooms + popularRooms + influencerRooms
    }

    fun onMyGroupCardClick(data: GroupCardData) {
        // 내 모임방 카드 클릭 (상세 진입)
    }

    fun onRoomCardClick(data: GroupCardItemRoomData) {
        // 방 카드 클릭 (상세 진입)
    }

}