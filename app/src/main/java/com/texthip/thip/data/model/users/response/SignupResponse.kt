package com.texthip.thip.data.model.users.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignupResponse(
    @SerialName("accessToken") val accessToken: String,
    @SerialName("userId") val userId: Long
)