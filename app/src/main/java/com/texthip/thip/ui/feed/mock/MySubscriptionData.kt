package com.texthip.thip.ui.feed.mock

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter

data class MySubscriptionData(
    val profileImageUrl: String? = null,
    val nickname: String,
    val role: String,
    val roleColor: Color,
    val subscriberCount: Int,
    var isSubscribed: Boolean = true
)
