package com.texthip.thip.ui.group.myroom.mock

import com.texthip.thip.R

data class GroupBookData(
    val title: String,
    val author: String,
    val publisher: String,
    val description: String,
    val imageRes: Int = R.drawable.bookcover_sample
)
