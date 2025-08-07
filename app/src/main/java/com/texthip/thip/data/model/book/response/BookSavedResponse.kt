package com.texthip.thip.data.model.book.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookSavedResponse(
    @SerialName("isbn") val isbn: String,
    @SerialName("bookTitle") val bookTitle: String,
    @SerialName("authorName") val authorName: String,
    @SerialName("publisher") val publisher: String,
    @SerialName("imageUrl") val imageUrl: String?
)

@Serializable
data class BookListResponse(
    @SerialName("bookList") val bookList: List<BookSavedResponse>
)