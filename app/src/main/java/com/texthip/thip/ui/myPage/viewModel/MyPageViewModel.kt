package com.texthip.thip.ui.myPage.viewModel

import androidx.lifecycle.ViewModel
import com.texthip.thip.ui.myPage.groupPage.GroupCardData
import com.texthip.thip.ui.myPage.myGroup.GroupCardItemRoomData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.texthip.thip.R

class MyPageViewModel : ViewModel() {

    private val _myGroups = MutableStateFlow<List<GroupCardData>>(emptyList())
    val myGroups: StateFlow<List<GroupCardData>> = _myGroups

    private val _deadlineRooms = MutableStateFlow<List<GroupCardItemRoomData>>(emptyList())
    val deadlineRooms: StateFlow<List<GroupCardItemRoomData>> = _deadlineRooms

    private val _genres = listOf("문학", "과학·IT", "사회과학", "인문학", "예술")
    val genres: List<String> get() = _genres

    private val _selectedGenreIndex = MutableStateFlow(0)
    val selectedGenreIndex: StateFlow<Int> = _selectedGenreIndex

    // 초기 데이터 세팅 (실제에선 repository/remote에서 받아옴)
    init {
        _myGroups.value = listOf(
            GroupCardData("호르몬 체인지 완독하는 방", 22, R.drawable.bookcover_sample, 40, "uibowl1"),
            GroupCardData("호르몬 체인지 완독하는 방", 22, R.drawable.bookcover_sample, 40, "uibowl1"),
            GroupCardData("호르몬 체인지 완독하는 방", 22, R.drawable.bookcover_sample, 40, "uibowl1")
        )
        _deadlineRooms.value = listOf(
            GroupCardItemRoomData("시집만 읽는 사람들 3월", 22, 30, true, 3, R.drawable.bookcover_sample, 0), // 문학
            GroupCardItemRoomData("일본 소설 좋아하는 사람들", 22, 30, true, 3, R.drawable.bookcover_sample, 0), // 문학
            GroupCardItemRoomData("명작 같이 읽기방", 22, 30, true, 3, R.drawable.bookcover_sample, 0), // 문학

            GroupCardItemRoomData("물리책 읽는 방", 13, 20, true, 1, R.drawable.bookcover_sample, 1), // 과학·IT
            GroupCardItemRoomData("코딩 과학 동아리", 12, 15, true, 5, R.drawable.bookcover_sample, 1), // 과학·IT

            GroupCardItemRoomData("사회과학 인문 탐구", 8, 12, true, 4, R.drawable.bookcover_sample, 2), // 사회과학
        )

    }

    // 선택 장르 변경
    fun selectGenre(index: Int) {
        _selectedGenreIndex.value = index
        // 필요하면 장르에 따라 _deadlineRooms 필터링도 여기서 처리
    }

    fun onMyGroupHeaderClick() {
        // 내 모임방 리스트로 이동 (Nav 이벤트 트리거 등)
    }

    fun onMyGroupCardClick(data: GroupCardData) {
        // 내 모임방 카드 클릭 (상세 진입)
    }

    fun onRoomCardClick(data: GroupCardItemRoomData) {
        // 마감임박 카드 클릭 (상세 진입)
    }

    fun onFabClick() {
        // FAB 클릭(모임방 생성 등)
    }
}
