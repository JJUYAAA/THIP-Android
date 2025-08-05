package com.texthip.thip.ui.group.makeroom.mock

import com.texthip.thip.R

data class BookData(
    val title: String,
    val imageUrl: String? = null, // 이미지 URL
    val author: String? = null
)

val dummySavedBooks = listOf(
    BookData("토마토 컬러면", null, "최정화"),
    BookData("사슴", null, "최정화"),
    BookData("토마토 컬러면", null, "최정화"),
    BookData("사슴", null, "최정화"),
    BookData("토마토 컬러면", null, "최정화"),
    BookData("사슴", null, "최정화"),
    BookData("토마토 컬러면", null, "최정화"),
    BookData("사슴", null, "최정화")
)
val dummyGroupBooks = listOf(
    BookData("명작 읽기방", null),
    BookData("또 다른 방", null),
    BookData("명작 읽기방", null),
    BookData("또 다른 방", null),
    BookData("명작 읽기방", null),
    BookData("또 다른 방", null),
    BookData("명작 읽기방", null),
    BookData("또 다른 방", null)
)