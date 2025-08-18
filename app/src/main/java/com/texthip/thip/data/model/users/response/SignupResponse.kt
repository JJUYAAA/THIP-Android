package com.texthip.thip.data.model.users.response

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class SignupResponse(
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("userId") val userId: Long
)