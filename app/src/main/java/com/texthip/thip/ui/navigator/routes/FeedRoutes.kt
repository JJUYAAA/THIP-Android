package com.texthip.thip.ui.navigator.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class FeedRoutes : Routes() {
    // 향후 추가될 Feed 관련 화면들
    // @Serializable data object SubscriptionList : FeedRoutes
    // @Serializable data object Detail : FeedRoutes
}