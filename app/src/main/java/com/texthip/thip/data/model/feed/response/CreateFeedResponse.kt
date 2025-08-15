package com.texthip.thip.data.model.feed.response

import kotlinx.serialization.Serializable

@Serializable
data class CreateFeedResponse(
    val feedId: Int
)