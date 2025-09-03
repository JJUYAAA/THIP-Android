package com.texthip.thip.data.model.feed.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateFeedRequest(
    @SerialName("isbn")
    val isbn: String,
    @SerialName("contentBody")
    val contentBody: String,
    @SerialName("isPublic")
    val isPublic: Boolean,
    @SerialName("tagList")
    val tagList: List<String> = emptyList(),
    @SerialName("imageUrls")
    val imageUrls: List<String> = emptyList()
)