package com.texthip.thip.data.model.rooms.response

import kotlinx.serialization.Serializable

@Serializable
data class RoomsDailyGreetingResponse(
    val todayCommentList: List<TodayCommentList>,
    val nextCursor: String?,
    val isLast: Boolean
)

@Serializable
data class TodayCommentList(
    val attendanceCheckId: Int,
    val creatorId: Int,
    val creatorNickname: String,
    val creatorProfileImageUrl: String,
    val todayComment: String,
    val postDate: String,
    val date: String,
    val isWriter: Boolean,
)