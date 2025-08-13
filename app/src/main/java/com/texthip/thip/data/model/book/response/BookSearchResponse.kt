package com.texthip.thip.data.model.book.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookSearchResponse(
    @SerialName("searchResult") val searchResult: List<BookSearchItem>,
    @SerialName("page") val page: Int,
    @SerialName("size") val size: Int,
    @SerialName("totalElements") val totalElements: Int,
    @SerialName("totalPages") val totalPages: Int,
    @SerialName("last") val last: Boolean,
    @SerialName("first") val first: Boolean
)

@Serializable
data class BookSearchItem(
    @SerialName("title") val title: String,
    @SerialName("imageUrl") val imageUrl: String,
    @SerialName("authorName") val authorName: String,
    @SerialName("publisher") val publisher: String,
    @SerialName("isbn") val isbn: String
)