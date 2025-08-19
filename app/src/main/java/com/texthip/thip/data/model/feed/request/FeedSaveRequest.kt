package com.texthip.thip.data.model.feed.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeedSaveRequest(
    @SerialName("type") val type: Boolean
)
