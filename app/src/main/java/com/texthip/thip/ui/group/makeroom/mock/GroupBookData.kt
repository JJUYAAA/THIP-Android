package com.texthip.thip.ui.group.makeroom.mock

import com.texthip.thip.R

data class BookData(
    val title: String,
    val imageRes: Int, // drawable 리소스 or 이미지 URL
    val author: String? = null
)

val dummySavedBooks = listOf(
    BookData("토마토 컬러면", R.drawable.bookcover_sample),
    BookData("사슴",  R.drawable.bookcover_sample),
    BookData("토마토 컬러면", R.drawable.bookcover_sample),
    BookData("사슴",  R.drawable.bookcover_sample),
    BookData("토마토 컬러면", R.drawable.bookcover_sample),
    BookData("사슴",  R.drawable.bookcover_sample),
    BookData("토마토 컬러면", R.drawable.bookcover_sample),
    BookData("사슴",  R.drawable.bookcover_sample)
)
val dummyGroupBooks = listOf(
    BookData("명작 읽기방",  R.drawable.bookcover_sample),
    BookData("또 다른 방",  R.drawable.bookcover_sample),
    BookData("명작 읽기방",  R.drawable.bookcover_sample),
    BookData("또 다른 방",  R.drawable.bookcover_sample),
    BookData("명작 읽기방",  R.drawable.bookcover_sample),
    BookData("또 다른 방",  R.drawable.bookcover_sample),
    BookData("명작 읽기방",  R.drawable.bookcover_sample),
    BookData("또 다른 방",  R.drawable.bookcover_sample)
)