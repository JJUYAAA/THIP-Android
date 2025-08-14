package com.texthip.thip.data.model.users

import kotlinx.serialization.Serializable

@Serializable
data class NicknameRequest(
    val nickname: String
)
