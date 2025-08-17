package com.texthip.thip.data.model.feed.response

import kotlinx.serialization.Serializable

@Serializable
data class FeedUsersResponse (
    val feedList: List<FeedList>,
    val nextCursor: String? = null,
    val isLast: Boolean = false,
)

@Serializable
data class FeedList(
    val feedId: Long,
    val postDate: String,
    val isbn: String,
    val bookTitle: String,
    val bookAuthor: String,
    val contentBody: String,
    val contentUrls: List<String>,
    val likeCount: Int,
    val commentCount: Int,
    val isPublic: Boolean,
    val isSaved: Boolean,
    val isLiked: Boolean,
    val isWriter: Boolean,
)