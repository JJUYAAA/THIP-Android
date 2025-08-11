package com.texthip.thip.ui.group.myroom.mock

data class GroupRoomData(
    val id: Int,
    val title: String,
    val isSecret: Boolean,
    val description: String,
    val startDate: String,
    val endDate: String,
    val members: Int,
    val maxMembers: Int,
    val daysLeft: Int,
    val genre: String,
    val bookData: GroupBookData,
    val recommendations: List<GroupCardItemRoomData>,
    val buttonType: GroupBottomButtonType? = null, // API에서 결정된 버튼 타입
    val roomImageUrl: String? = null, // 방 대표 이미지 URL
    val bookImageUrl: String? = null  // 책 이미지 URL
)
