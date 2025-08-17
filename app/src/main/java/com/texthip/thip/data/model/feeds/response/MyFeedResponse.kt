package com.texthip.thip.data.model.feeds.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class MyFeedResponse(
    @SerialName("feedList") val feedList: List<MyFeedItem>,
    @SerialName("nextCursor") val nextCursor: String?,
    @SerialName("isLast") val isLast: Boolean
)

@Serializable
data class MyFeedItem(
    @SerialName("feedId") val feedId: Int,
    @SerialName("postDate") val postDate: String,
    @SerialName("isbn") val isbn: String,
    @SerialName("bookTitle") val bookTitle: String,
    @SerialName("bookAuthor") val bookAuthor: String,
    @SerialName("contentBody") val contentBody: String,
    @SerialName("contentUrls") val contentUrls: List<String>,
    @SerialName("likeCount") val likeCount: Int,
    @SerialName("commentCount") val commentCount: Int,
    @SerialName("isPublic") val isPublic: Boolean,
    @SerialName("isWriter") val isWriter: Boolean
)