package com.texthip.thip.data.model.rooms.response

import kotlinx.serialization.Serializable

@Serializable
data class RoomsRecordResponse(
    val recordId: Int,
    val roomId: Int,
)
