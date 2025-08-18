package com.texthip.thip.data.model.feed.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeedDetailResponse(
    @SerialName("feedId") val feedId: Int,
    @SerialName("creatorId") val creatorId: Int,
    @SerialName("creatorNickname") val creatorNickname: String,
    @SerialName("creatorProfileImageUrl") val creatorProfileImageUrl: String?,
    @SerialName("aliasName") val aliasName: String,
    @SerialName("aliasColor") val aliasColor: String,
    @SerialName("postDate") val postDate: String,
    @SerialName("bookTitle") val bookTitle: String,
    @SerialName("isbn") val isbn: String,
    @SerialName("bookAuthor") val bookAuthor: String,
    @SerialName("bookImageUrl") val bookImageUrl: String? = null,  // 책 이미지 URL 추가
    @SerialName("contentBody") val contentBody: String,
    @SerialName("contentUrls") val contentUrls: List<String>,
    @SerialName("likeCount") val likeCount: Int,
    @SerialName("commentCount") val commentCount: Int,
    @SerialName("isSaved") val isSaved: Boolean,
    @SerialName("isLiked") val isLiked: Boolean,
    @SerialName("isWriter") val isWriter: Boolean,
    @SerialName("isPublic") val isPublic: Boolean? = null,  // 공개/비공개 설정 추가
    @SerialName("tagList") val tagList: List<String>
)