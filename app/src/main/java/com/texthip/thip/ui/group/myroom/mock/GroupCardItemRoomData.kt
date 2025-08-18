package com.texthip.thip.ui.group.myroom.mock

data class GroupCardItemRoomData(
    val id: Int,
    val title: String,
    val participants: Int,
    val maxParticipants: Int,
    val isRecruiting: Boolean,
    val endDate: String? = null, // 마감 시간 텍스트 (예: "8시간 뒤")
    val imageUrl: String? = null, // API에서 받은 이미지 URL
    val isSecret: Boolean = false
)