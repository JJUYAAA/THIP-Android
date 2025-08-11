package com.texthip.thip.data.model.group.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateRoomRequest(
    @SerialName("isbn") val isbn: String,
    @SerialName("category") val category: String,
    @SerialName("roomName") val roomName: String,
    @SerialName("description") val description: String,
    @SerialName("progressStartDate") val progressStartDate: String,
    @SerialName("progressEndDate") val progressEndDate: String,
    @SerialName("recruitCount") val recruitCount: Int,
    @SerialName("password") val password: String? = null,
    @SerialName("isPublic") val isPublic: Boolean
)