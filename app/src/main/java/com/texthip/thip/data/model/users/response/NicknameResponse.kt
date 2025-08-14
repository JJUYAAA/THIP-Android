package com.texthip.thip.data.model.users.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NicknameResponse(
    @SerialName("isVerified") val isVerified: Boolean
)
