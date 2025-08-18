package com.texthip.thip.data.model.rooms.response

import kotlinx.serialization.Serializable

@Serializable
data class RoomsCreateDailyGreetingResponse(
    val attendanceCheckId: Long,
    val roomId: Long,
    val isFirstWrite: Boolean,
)
