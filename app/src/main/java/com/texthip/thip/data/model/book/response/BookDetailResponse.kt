package com.texthip.thip.data.model.book.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookDetailResponse(
    @SerialName("title") val title: String = "",
    @SerialName("imageUrl") val imageUrl: String? = null,
    @SerialName("authorName") val authorName: String = "",
    @SerialName("publisher") val publisher: String = "",
    @SerialName("isbn") val isbn: String = "",
    @SerialName("description") val description: String = "",
    @SerialName("recruitingRoomCount") val recruitingRoomCount: Int = 0,
    @SerialName("readCount") val readCount: Int = 0,
    @SerialName("isSaved") val isSaved: Boolean = false
)