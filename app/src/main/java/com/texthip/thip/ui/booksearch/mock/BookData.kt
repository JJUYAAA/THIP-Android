package com.texthip.thip.ui.booksearch.mock

import com.texthip.thip.R

data class BookData(
    val title: String,
    val author: String = "",
    val publisher: String = "",
    val imageRes: Int = R.drawable.bookcover_sample
)
