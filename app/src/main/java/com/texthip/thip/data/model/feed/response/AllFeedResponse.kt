package com.texthip.thip.data.model.feed.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class AllFeedResponse(
    @SerialName("feedList") val feedList: List<AllFeedItem>,
    @SerialName("nextCursor") val nextCursor: String?,
    @SerialName("isLast") val isLast: Boolean
)

@Serializable
data class AllFeedItem(
    @SerialName("feedId") val feedId: Int,
    @SerialName("creatorId") val creatorId: Int,
    @SerialName("creatorNickname") val creatorNickname: String,
    @SerialName("creatorProfileImageUrl") val creatorProfileImageUrl: String?,
    @SerialName("aliasName") val aliasName: String,
    @SerialName("aliasColor") val aliasColor: String,
    @SerialName("postDate") val postDate: String,
    @SerialName("isbn") val isbn: String,
    @SerialName("bookTitle") val bookTitle: String,
    @SerialName("bookAuthor") val bookAuthor: String,
    @SerialName("contentBody") val contentBody: String,
    @SerialName("contentUrls") val contentUrls: List<String>,
    @SerialName("likeCount") val likeCount: Int,
    @SerialName("commentCount") val commentCount: Int,
    @SerialName("isSaved") val isSaved: Boolean,
    @SerialName("isLiked") val isLiked: Boolean,
    @SerialName("isWriter") val isWriter: Boolean
)