package com.texthip.thip.ui.group.myroom.mock

data class GroupCardItemRoomData(
    val id: Int,
    val title: String,
    val participants: Int,
    val maxParticipants: Int,
    val isRecruiting: Boolean,
    val endDate: Int? = null, // 남은 일 수
    val imageUrl: String? = null, // API에서 받은 이미지 URL
    val isSecret: Boolean = false
)


