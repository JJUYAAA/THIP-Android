package com.texthip.thip.data.model.book.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookSaveRequest(
    @SerialName("type") val type: Boolean = false
)