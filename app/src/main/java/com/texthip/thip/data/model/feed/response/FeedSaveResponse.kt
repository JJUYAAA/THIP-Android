package com.texthip.thip.data.model.feed.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeedSaveResponse(
    @SerialName("feedId") val feedId: Long,
    @SerialName("isSaved") val isSaved: Boolean
)
