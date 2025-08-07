package com.texthip.thip.ui.group.room.mock

import com.texthip.thip.ui.group.myroom.mock.GroupBottomButtonType
import com.texthip.thip.ui.group.myroom.mock.GroupRoomData

data class GroupRoomRecruitUiState(
    val roomDetail: GroupRoomData? = null,
    val isLoading: Boolean = false,
    val currentButtonType: GroupBottomButtonType? = null,
    val showToast: Boolean = false,
    val toastMessage: String = "",
    val showDialog: Boolean = false,
    val dialogTitle: String = "",
    val dialogDescription: String = "",
    val shouldNavigateToGroupScreen: Boolean = false
) {
    val hasRoomDetail: Boolean get() = roomDetail != null
}