package com.texthip.thip.ui.group.viewmodel

import com.texthip.thip.data.model.group.response.JoinedRoomResponse
import com.texthip.thip.data.model.group.response.RoomMainList

data class GroupUiState(
    val myJoinedRooms: List<JoinedRoomResponse> = emptyList(),
    val hasMoreMyGroups: Boolean = true,
    val isRefreshing: Boolean = false,
    val isLoadingMoreMyGroups: Boolean = false,
    val roomMainList: RoomMainList? = null,
    val roomSectionsError: String? = null,
    val userName: String = "",
    val selectedGenreIndex: Int = 0,
    val showToast: Boolean = false,
    val toastMessage: String = ""
) {
    val hasContent: Boolean get() = myJoinedRooms.isNotEmpty() || (roomMainList != null)
    val canLoadMore: Boolean get() = hasMoreMyGroups && !isRefreshing && !isLoadingMoreMyGroups
}