package com.texthip.thip.ui.myPage.groupPage

import com.texthip.thip.ui.myPage.myGroup.GroupCardItemRoomData

data class GroupRoomSectionData(
    val title: String,
    val rooms: List<GroupCardItemRoomData>,
    val genres: List<String>
)
