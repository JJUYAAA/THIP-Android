package com.texthip.thip.ui.myPage.data

data class FeedItem(
    val id: Int,
    val username: String,
    val user_role: String,
    val book_name: String,
    val auth_name: String,
    val timedate: String,
    val content: String,
    val like_count: Int,
    val comment_count: Int,
    val is_like: Boolean,
    val is_saved: Boolean,
    val imageUrl: String? = null
)

