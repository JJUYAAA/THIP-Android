package com.texthip.thip.ui.myPage.groupPage

import com.texthip.thip.R

data class MyGroupCardData(
    val title: String,
    val members: Int,
    val imageRes: Int = R.drawable.bookcover_sample,
    val progress: Int, // 진행률 (0~100)
    val nickname: String
)