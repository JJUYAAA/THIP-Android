package com.texthip.thip.ui.navigator.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class FeedRoutes : Routes() {

    @Serializable data object MySubscription : FeedRoutes()
    @Serializable data object Write : FeedRoutes()
}