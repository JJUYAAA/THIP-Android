package com.texthip.thip.data.model.book.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookSaveResponse(
    @SerialName("isbn") val isbn: String = "",
    @SerialName("isSaved") val isSaved: Boolean = false
)