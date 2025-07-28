package com.texthip.thip.ui.group.data.repository

import com.texthip.thip.ui.group.myroom.mock.GroupCardData
import com.texthip.thip.ui.group.myroom.mock.GroupCardItemRoomData
import com.texthip.thip.ui.group.myroom.mock.GroupRoomData
import com.texthip.thip.ui.group.myroom.mock.GroupRoomSectionData


// 그룹 데이터를 제공하는 Repository 인터페이스
interface GroupRepository {
    // 사용저의 이름
    suspend fun getUserName(): Result<String>
    
    // 내 모임방 카드 목록
    suspend fun getMyGroups(): Result<List<GroupCardData>>
    
    // 모임방 섹션 데이터 (마감 임박, 인기, 인플루언서 등)
    suspend fun getRoomSections(): Result<List<GroupRoomSectionData>>
    
    // 완료된 모임방 목록
    suspend fun getDoneGroups(): Result<List<GroupCardItemRoomData>>
    
    // 내 참여 모임방 목록
    suspend fun getMyRoomGroups(): Result<List<GroupCardItemRoomData>>
    
    // 검색용 모임방 목록
    suspend fun getSearchGroups(): Result<List<GroupCardItemRoomData>>
    
    // 특정 모임방 상세 정보
    suspend fun getRoomDetail(roomId: Int): Result<GroupRoomData>
    
    // 모임방 검색
    suspend fun searchRooms(query: String): Result<List<GroupCardItemRoomData>>

    suspend fun getGenres(): Result<List<String>>
}