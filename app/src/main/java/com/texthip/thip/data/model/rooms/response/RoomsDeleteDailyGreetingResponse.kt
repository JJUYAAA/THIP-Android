package com.texthip.thip.data.model.rooms.response

import kotlinx.serialization.Serializable

@Serializable
data class RoomsDeleteDailyGreetingResponse (
    val roomId: Int,
)