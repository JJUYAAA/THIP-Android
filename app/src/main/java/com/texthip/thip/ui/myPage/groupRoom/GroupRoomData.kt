package com.texthip.thip.ui.myPage.groupRoom

import com.texthip.thip.ui.myPage.myGroup.CardItemRoomData

data class GroupRoomData(
    val title: String,
    val isSecret: Boolean,
    val description: String,
    val period: String,
    val members: Int,
    val maxMembers: Int,
    val daysLeft: Int,
    val genre: String,
    val bookData: BookData,
    val recommendations: List<CardItemRoomData>
)

enum class BottomButtonType { JOIN, CANCEL, CLOSE }
