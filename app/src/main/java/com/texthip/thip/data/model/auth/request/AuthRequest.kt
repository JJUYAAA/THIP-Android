package com.texthip.thip.data.model.auth.request
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    @SerialName("oauth2Id") val oauth2Id: String
)