package com.texthip.thip.ui.group.done.mock

data class GroupDoneUiState(
    val expiredRooms: List<MyRoomCardData> = emptyList(),
    val isLoading: Boolean = false,
    val userName: String = "",
    val error: String? = null
) {
    val hasContent: Boolean get() = expiredRooms.isNotEmpty()
}