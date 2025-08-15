package com.texthip.thip.ui.group.room.viewmodel

import com.texthip.thip.ui.group.myroom.mock.GroupBottomButtonType
import com.texthip.thip.data.model.rooms.response.RoomRecruitingResponse

data class GroupRoomRecruitUiState(
    val roomDetail: RoomRecruitingResponse? = null,
    val isLoading: Boolean = false,
    val currentButtonType: GroupBottomButtonType? = null,
    val showToast: Boolean = false,
    val toastMessage: String = "",
    val showDialog: Boolean = false,
    val dialogTitle: String = "",
    val dialogDescription: String = "",
    val shouldNavigateToGroupScreen: Boolean = false,
    val shouldNavigateToRoomPlayingScreen: Boolean = false,
    val roomId: Int? = null
) {
    val hasRoomDetail: Boolean get() = roomDetail != null
}