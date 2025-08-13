package com.texthip.thip.data.model.rooms.request

import kotlinx.serialization.Serializable

@Serializable
data class RoomsRecordRequest(
    val page: Int,
    val isOverview: Boolean = false,
    val content: String,
)
