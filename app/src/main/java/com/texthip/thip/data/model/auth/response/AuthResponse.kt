package com.texthip.thip.data.model.auth.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    @SerialName("token") val token: String,
    @SerialName("isNewUser") val isNewUser: Boolean
)