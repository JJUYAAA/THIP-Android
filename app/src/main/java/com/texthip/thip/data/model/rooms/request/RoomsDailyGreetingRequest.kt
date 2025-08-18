package com.texthip.thip.data.model.rooms.request

import kotlinx.serialization.Serializable

@Serializable
data class RoomsDailyGreetingRequest(
    val content: String,
)
