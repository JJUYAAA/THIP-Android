package com.texthip.thip.ui.myPage.groupRoom

import com.texthip.thip.R

data class BookData(
    val title: String,
    val author: String,
    val publisher: String,
    val description: String,
    val imageRes: Int = R.drawable.bookcover_sample
)
