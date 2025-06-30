package com.texthip.thip.ui.myPage.data

import androidx.compose.ui.graphics.painter.Painter

data class FeedItem(
    val id: Int,
    val user_name: String,
    val user_role: String,
    val book_title: String,
    val auth_name: String,
    val time_ago: Int,
    val content: String,
    val like_count: Int,
    val comment_count: Int,
    val is_like: Boolean,
    val is_saved: Boolean,
    val imageUrl: Painter? = null
)

