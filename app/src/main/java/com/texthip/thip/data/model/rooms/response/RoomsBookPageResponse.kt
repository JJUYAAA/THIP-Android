package com.texthip.thip.data.model.rooms.response

import kotlinx.serialization.Serializable

@Serializable
data class RoomsBookPageResponse(
    val totalBookPage: Int,
    val recentBookPage: Int,
    val isOverviewPossible: Boolean,
    val roomId: Int,
)
