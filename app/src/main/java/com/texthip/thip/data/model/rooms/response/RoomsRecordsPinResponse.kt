package com.texthip.thip.data.model.rooms.response

import kotlinx.serialization.Serializable

@Serializable
data class RoomsRecordsPinResponse(
    val bookTitle: String,
    val authorName: String,
    val bookImageUrl: String,
    val isbn: String,
)