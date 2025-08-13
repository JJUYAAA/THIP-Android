package com.texthip.thip.data.model.book.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MostSearchedBooksResponse(
    @SerialName("bookList") val bookList: List<PopularBookItem> = emptyList()
)

@Serializable
data class PopularBookItem(
    @SerialName("rank") val rank: Int = 0,
    @SerialName("title") val title: String = "",
    @SerialName("imageUrl") val imageUrl: String? = null,
    @SerialName("isbn") val isbn: String = ""
)