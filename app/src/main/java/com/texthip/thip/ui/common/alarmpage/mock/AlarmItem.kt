package com.texthip.thip.ui.common.alarmpage.mock

data class AlarmItem(
    val id: Int,
    val badgeText: String,
    val title: String,
    val message: String,
    val timeAgo: String,
    var isRead: Boolean
)