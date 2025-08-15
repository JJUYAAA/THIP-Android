package com.texthip.thip.data.model.feed.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeedWriteInfoResponse(
    @SerialName("categoryList")
    val categoryList: List<FeedCategory>
)

@Serializable
data class FeedCategory(
    @SerialName("category")
    val category: String,
    @SerialName("tagList")
    val tagList: List<String>
)