package com.texthip.thip.data.model.book.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookUserSaveResponse(
    val bookList: List<BookUserSaveList>,
    @SerialName("nextCursor") val nextCursor: String? = null,
    @SerialName("isLast") val isLast: Boolean = false
)

@Serializable
data class BookUserSaveList(
    val bookId: Int,
    val bookTitle: String,
    val authorName: String,
    val publisher: String,
    val bookImageUrl: String? = null,
    val isbn: String = "",
    val isSaved: Boolean = true
)