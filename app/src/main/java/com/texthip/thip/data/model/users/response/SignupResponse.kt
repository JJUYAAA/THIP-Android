package com.texthip.thip.data.model.users.response

import kotlinx.serialization.Serializable

@Serializable
data class SignupResponse(
    val accessToken: String,
    val refreshToken: String
)