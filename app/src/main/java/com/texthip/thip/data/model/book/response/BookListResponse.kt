package com.texthip.thip.data.model.book.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class BookListResponse(
    @SerialName("bookList") val bookList: List<BookSavedResponse> = emptyList(),
    @SerialName("nextCursor") val nextCursor: String? = null,
    @SerialName("isLast") val isLast: Boolean = false
)

@Serializable
data class BookSavedResponse(
    @SerialName("bookId") val bookId: Int = 0,
    @SerialName("bookTitle") val bookTitle: String = "",
    @SerialName("authorName") val authorName: String = "",
    @SerialName("publisher") val publisher: String = "",
    @SerialName("bookImageUrl") val bookImageUrl: String? = null,
    @SerialName("isbn") val isbn: String = "",
)