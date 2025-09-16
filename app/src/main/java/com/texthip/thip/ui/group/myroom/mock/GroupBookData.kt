package com.texthip.thip.ui.group.myroom.mock

import com.texthip.thip.R

data class GroupBookData(
    val title: String,
    val author: String,
    val publisher: String,
    val description: String,
    val imageUrl: String? = null // API에서 받은 이미지 URL
)
