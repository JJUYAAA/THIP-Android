package com.texthip.thip.ui.group.myroom.mock

import com.texthip.thip.ui.group.done.mock.MyRoomCardData

data class GroupMyUiState(
    val myRooms: List<MyRoomCardData> = emptyList(),
    val currentRoomType: String = PLAYING_AND_RECRUITING,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null
) {
    val hasContent: Boolean get() = myRooms.isNotEmpty()
    val canLoadMore: Boolean get() = !isLoading && !isLoadingMore
    
    companion object {
        const val PLAYING_AND_RECRUITING = "playingAndRecruiting"
        const val RECRUITING = "recruiting" 
        const val PLAYING = "playing"
        const val EXPIRED = "expired"
        
        // Room actions
        const val ACTION_JOIN = "join"
        const val ACTION_CANCEL = "cancel"
    }
}