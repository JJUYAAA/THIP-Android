package com.texthip.thip.ui.search.mock

import com.texthip.thip.R

data class DetailBookData(
    val title: String,
    val author: String,
    val publisher: String,
    val description: String,
    val coverImageRes: Int? = R.drawable.bookcover_sample,
    val participantsCount: Int = 0,
    val recruitingRoomCount: Int = 0
)
