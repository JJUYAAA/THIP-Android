package com.texthip.thip.data.model.users.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MyPageInfoResponse(
    @SerialName("profileImageUrl") val profileImageUrl: String?,
    @SerialName("nickname") val nickname: String,
    @SerialName("aliasName") val aliasName: String,
    @SerialName("aliasColor") val aliasColor: String
)