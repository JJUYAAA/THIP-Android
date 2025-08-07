package com.texthip.thip.ui.group.viewmodel

import com.texthip.thip.ui.group.myroom.mock.GroupCardData
import com.texthip.thip.ui.group.myroom.mock.GroupRoomSectionData

data class GroupUiState(
    val myGroups: List<GroupCardData> = emptyList(),
    val hasMoreMyGroups: Boolean = true,
    val isRefreshing: Boolean = false,
    val isLoadingMoreMyGroups: Boolean = false,
    val roomSections: List<GroupRoomSectionData> = emptyList(),
    val roomSectionsError: String? = null,
    val userName: String = "",
    val selectedGenreIndex: Int = 0,
    val showToast: Boolean = false,
    val toastMessage: String = ""
) {
    val hasContent: Boolean get() = myGroups.isNotEmpty() || roomSections.isNotEmpty()
    val canLoadMore: Boolean get() = hasMoreMyGroups && !isRefreshing && !isLoadingMoreMyGroups
}