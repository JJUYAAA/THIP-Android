package com.texthip.thip.data.model.book.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookSearchResponse(
    @SerialName("searchResult") val searchResult: List<BookSearchItem> = emptyList(),
    @SerialName("page") val page: Int = 0,
    @SerialName("size") val size: Int = 0,
    @SerialName("totalElements") val totalElements: Int = 0,
    @SerialName("totalPages") val totalPages: Int = 0,
    @SerialName("last") val last: Boolean = true,
    @SerialName("first") val first: Boolean = true
)

@Serializable
data class BookSearchItem(
    @SerialName("title") val title: String = "",
    @SerialName("imageUrl") val imageUrl: String? = null,
    @SerialName("authorName") val authorName: String = "",
    @SerialName("publisher") val publisher: String = "",
    @SerialName("isbn") val isbn: String = ""
)