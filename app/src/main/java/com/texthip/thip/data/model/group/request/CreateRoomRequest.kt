package com.texthip.thip.data.model.group.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateRoomRequest(
    val isbn: String,
    val category: String,
    val roomName: String,
    val description: String,
    val progressStartDate: String,
    val progressEndDate: String,
    val recruitCount: Int,
    val password: String? = null,
    val isPublic: Boolean
)