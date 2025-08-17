package com.texthip.thip.ui.navigator.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class FeedRoutes : Routes() {
  
    @Serializable data object MySubscription : FeedRoutes()

    @Serializable data object Write : FeedRoutes()
    
    @Serializable data class Comment(val feedId: Int) : FeedRoutes()

    @Serializable
    data class Write(
        val isbn: String? = null,
        val bookTitle: String? = null,
        val bookAuthor: String? = null,
        val bookImageUrl: String? = null,
        val recordContent: String? = null
    ) : FeedRoutes()

    @Serializable data class Others(val userId: Long) : FeedRoutes()
}