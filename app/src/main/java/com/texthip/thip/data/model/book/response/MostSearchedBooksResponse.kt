package com.texthip.thip.data.model.book.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MostSearchedBooksResponse(
    @SerialName("bookList") val bookList: List<PopularBookItem>
)

@Serializable
data class PopularBookItem(
    @SerialName("rank") val rank: Int,
    @SerialName("title") val title: String,
    @SerialName("imageUrl") val imageUrl: String,
    @SerialName("isbn") val isbn: String
)