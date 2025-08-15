package com.texthip.thip.data.model.feed.response

import kotlinx.serialization.Serializable

@Serializable
data class FeedWriteInfoResponse(
    val categoryList: List<FeedCategory>
)

@Serializable
data class FeedCategory(
    val category: String,
    val tagList: List<String>
)