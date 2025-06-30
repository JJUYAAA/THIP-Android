package com.texthip.thip.ui.myPage.mock

data class FeedItem(
    val id: Int,
    val user_profile_image: Int? = null,
    val user_name: String,
    val user_role: String,
    val book_title: String,
    val auth_name: String,
    val time_ago: Int,
    val content: String,
    val like_count: Int,
    val comment_count: Int,
    val is_liked: Boolean,
    val is_saved: Boolean,
    val imageUrl: Int? = null
)

