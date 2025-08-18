package com.texthip.thip.data.model.feed.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateFeedRequest(
    @SerialName("contentBody") val contentBody: String? = null,
    @SerialName("isPublic") val isPublic: Boolean? = null,
    @SerialName("tagList") val tagList: List<String>? = null,
    @SerialName("remainImageUrls") val remainImageUrls: List<String>? = null
)