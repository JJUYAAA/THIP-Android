package com.texthip.thip.ui.feed.mock

import kotlinx.serialization.Serializable

@Serializable
data class FeedStateUpdateResult(
    val feedId: Long,
    val isLiked: Boolean,
    val likeCount: Int,
    val isSaved: Boolean
)
