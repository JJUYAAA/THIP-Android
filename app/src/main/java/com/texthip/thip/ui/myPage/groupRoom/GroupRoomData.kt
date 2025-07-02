package com.texthip.thip.ui.myPage.groupRoom

import com.texthip.thip.ui.myPage.myGroup.GroupCardItemRoomData

data class GroupRoomData(
    val title: String,
    val isSecret: Boolean,
    val description: String,
    val period: String,
    val members: Int,
    val maxMembers: Int,
    val daysLeft: Int,
    val genre: String,
    val bookData: GroupBookData,
    val recommendations: List<GroupCardItemRoomData>
)
