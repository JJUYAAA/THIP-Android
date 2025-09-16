package com.texthip.thip.data.model.users.request

import kotlinx.serialization.Serializable

@Serializable
data class SignupRequest(
    val nickname: String,
    val aliasName: String
)