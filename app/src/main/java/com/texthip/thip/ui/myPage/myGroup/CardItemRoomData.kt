package com.texthip.thip.ui.myPage.myGroup

import com.texthip.thip.R

data class CardItemRoomData(
    val title: String,
    val participants: Int,
    val maxParticipants: Int,
    val isRecruiting: Boolean,
    val endDate: Int, // 남은 일 수
    val imageRes: Int? = R.drawable.bookcover_sample,
    val genreIndex: Int // 장르 인덱스
)


