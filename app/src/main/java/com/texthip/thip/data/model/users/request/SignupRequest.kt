package com.texthip.thip.data.model.users.request

import kotlinx.serialization.Serializable

@Serializable
data class SignupRequest(
    val nickName: String,
    val aliasName: String
)