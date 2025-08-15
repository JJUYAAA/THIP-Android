package com.texthip.thip.data.model.feed.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateFeedResponse(
    @SerialName("feedId")
    val feedId: Int
)