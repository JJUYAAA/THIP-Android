package com.texthip.thip.data.model.book.response

import kotlinx.serialization.Serializable

@Serializable
data class BookSaveResponse(
    val isbn: String,
    val isSaved: Boolean
)