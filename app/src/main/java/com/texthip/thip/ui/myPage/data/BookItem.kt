package com.texthip.thip.ui.myPage.data

data class BookItem(
    val id: Int,
    val title: String,
    val author: String,
    val publisher: String,
    val imageUrl: String? = null,
    val isSaved: Boolean
)
