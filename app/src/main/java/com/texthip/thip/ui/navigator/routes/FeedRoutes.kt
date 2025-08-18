package com.texthip.thip.ui.navigator.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class FeedRoutes : Routes() {
  
    @Serializable data object MySubscription : FeedRoutes()
    
    @Serializable data class Comment(val feedId: Int) : FeedRoutes()

    @Serializable
    data class Write(
        val feedId: Int? = null,  // 수정 모드용 피드 ID
        val isbn: String? = null,
        val bookTitle: String? = null,
        val bookAuthor: String? = null,
        val bookImageUrl: String? = null,
        val recordContent: String? = null,
        // 수정 모드용 추가 필드들
        val editContentBody: String? = null,
        val editIsPublic: Boolean? = null,
        val editTagList: List<String>? = null,
        val editContentUrls: List<String>? = null
    ) : FeedRoutes()

    @Serializable data class Others(val userId: Long) : FeedRoutes()
}