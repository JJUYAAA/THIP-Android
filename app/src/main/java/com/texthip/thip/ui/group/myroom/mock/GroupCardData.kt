package com.texthip.thip.ui.group.myroom.mock

import com.texthip.thip.R

data class GroupCardData(
    val id: Int,
    val title: String,
    val members: Int,
    val imageUrl: String?,       // 추가
    val progress: Int,           // 0~100
    val nickname: String
)
