package com.texthip.thip.data.model.users.request

import kotlinx.serialization.Serializable

@Serializable
data class NicknameRequest(
    val nickname: String
)