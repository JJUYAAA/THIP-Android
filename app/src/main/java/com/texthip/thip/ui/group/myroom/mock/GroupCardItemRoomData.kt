package com.texthip.thip.ui.group.myroom.mock

import com.texthip.thip.R

data class GroupCardItemRoomData(
    val id: Int,
    val title: String,
    val participants: Int,
    val maxParticipants: Int,
    val isRecruiting: Boolean,
    val endDate: Int? = null, // 남은 일 수
    val imageRes: Int? = R.drawable.bookcover_sample, // 스켈레톤 이미지 (fallback)
    val imageUrl: String? = null, // API에서 받은 이미지 URL
    val genreIndex: Int, // 장르 인덱스
    val isSecret: Boolean = false
)


