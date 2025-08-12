package com.texthip.thip.data.model.book.request

import kotlinx.serialization.Serializable

@Serializable
data class BookSaveRequest(
    val type: Boolean
)