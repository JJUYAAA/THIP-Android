package com.texthip.thip.data.model.book.response

import kotlinx.serialization.Serializable

@Serializable
data class MostSearchedBooksResponse(
    val bookList: List<PopularBookItem>
)

@Serializable
data class PopularBookItem(
    val rank: Int,
    val title: String,
    val imageUrl: String,
    val isbn: String
)