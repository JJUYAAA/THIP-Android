package com.texthip.thip.data.model.feed.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateFeedRequest(
    val isbn: String,
    val contentBody: String,
    val isPublic: Boolean,
    val tagList: List<String> = emptyList()
)