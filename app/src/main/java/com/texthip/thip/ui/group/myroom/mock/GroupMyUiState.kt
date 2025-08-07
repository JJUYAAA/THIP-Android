package com.texthip.thip.ui.group.myroom.mock

import com.texthip.thip.ui.group.done.mock.MyRoomCardData

data class GroupMyUiState(
    val myRooms: List<MyRoomCardData> = emptyList(),
    val currentRoomType: RoomType = RoomType.PLAYING_AND_RECRUITING,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasMore: Boolean = true,
    val error: String? = null
) {
    val hasContent: Boolean get() = myRooms.isNotEmpty()
    val canLoadMore: Boolean get() = !isLoading && !isLoadingMore && hasMore
}