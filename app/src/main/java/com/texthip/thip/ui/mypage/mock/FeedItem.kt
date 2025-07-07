package com.texthip.thip.ui.mypage.mock

data class FeedItem(
    val id: Int,
    val userProfileImage: Int? = null,
    val userName: String,
    val userRole: String,
    val bookTitle: String,
    val authName: String,
    val timeAgo: String,
    val content: String,
    val likeCount: Int,
    val commentCount: Int,
    val isLiked: Boolean,
    val isSaved: Boolean,
    val imageUrl: Int? = null
)

