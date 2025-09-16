package com.texthip.thip.data.model.rooms.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoomRecruitingResponse(
    @SerialName("isHost") val isHost: Boolean,
    @SerialName("isJoining") val isJoining: Boolean,
    @SerialName("roomId") val roomId: Int,
    @SerialName("roomName") val roomName: String,
    @SerialName("roomImageUrl") val roomImageUrl: String?,
    @SerialName("isPublic") val isPublic: Boolean,
    @SerialName("progressStartDate") val progressStartDate: String,
    @SerialName("progressEndDate") val progressEndDate: String,
    @SerialName("recruitEndDate") val recruitEndDate: String,
    @SerialName("category") val category: String,
    @SerialName("categoryColor") val categoryColor: String,
    @SerialName("roomDescription") val roomDescription: String,
    @SerialName("memberCount") val memberCount: Int,
    @SerialName("recruitCount") val recruitCount: Int,
    @SerialName("isbn") val isbn: String,
    @SerialName("bookImageUrl") val bookImageUrl: String,
    @SerialName("bookTitle") val bookTitle: String,
    @SerialName("authorName") val authorName: String,
    @SerialName("bookDescription") val bookDescription: String,
    @SerialName("publisher") val publisher: String,
    @SerialName("recommendRooms") val recommendRooms: List<RecommendRoomResponse>
)

@Serializable
data class RecommendRoomResponse(
    @SerialName("roomId") val roomId: Int,
    @SerialName("bookImageUrl") val bookImageUrl: String?,
    @SerialName("roomName") val roomName: String,
    @SerialName("memberCount") val memberCount: Int,
    @SerialName("recruitCount") val recruitCount: Int,
    @SerialName("recruitEndDate") val recruitEndDate: String
)