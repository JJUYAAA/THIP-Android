package com.texthip.thip.ui.group.done.viewmodel

import com.texthip.thip.data.model.rooms.response.MyRoomResponse

data class GroupDoneUiState(
    val expiredRooms: List<MyRoomResponse> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasMore: Boolean = true,
    val userName: String = "",
    val error: String? = null
) {
    val hasContent: Boolean get() = expiredRooms.isNotEmpty()
    val canLoadMore: Boolean get() = !isLoading && !isLoadingMore && hasMore
}