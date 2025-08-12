package com.texthip.thip.ui.group.myroom.mock

import com.texthip.thip.R

data class GroupCardData(
    val id: Int,
    val title: String,
    val members: Int,
    val imageUrl: String?,       // API에서 받은 이미지 URL
    val progress: Int,           // 0~100
    val nickname: String
)
