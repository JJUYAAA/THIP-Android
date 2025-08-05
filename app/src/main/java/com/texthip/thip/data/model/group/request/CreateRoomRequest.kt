package com.texthip.thip.data.model.group.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateRoomRequest(
    val isbn: String,
    val category: String,
    val roomName: String,
    val description: String,
    val progressStartDate: String, // yyyy.MM.dd 형식
    val progressEndDate: String,   // yyyy.MM.dd 형식
    val recruitCount: Int,         // 1~30명
    val password: String? = null,  // 비공개방일 때만 필요 (숫자 4자리)
    val isPublic: Boolean
)