package com.texthip.thip.data.model.book.response

import kotlinx.serialization.Serializable

@Serializable
data class BookDetailResponse(
    val title: String,
    val imageUrl: String,
    val authorName: String,
    val publisher: String,
    val isbn: String,
    val description: String,
    val recruitingRoomCount: Int,
    val readCount: Int,
    val isSaved: Boolean
)