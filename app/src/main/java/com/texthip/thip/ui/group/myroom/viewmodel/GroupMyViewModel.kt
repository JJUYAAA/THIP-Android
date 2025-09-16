package com.texthip.thip.ui.group.myroom.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.repository.RoomsRepository
import com.texthip.thip.ui.group.myroom.mock.RoomType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupMyViewModel @Inject constructor(
    private val repository: RoomsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GroupMyUiState())
    val uiState: StateFlow<GroupMyUiState> = _uiState.asStateFlow()
    
    private var nextCursor: String? = null
    private var isLastPage = false
    private var isLoadingData = false
    
    private fun updateState(update: (GroupMyUiState) -> GroupMyUiState) {
        _uiState.value = update(_uiState.value)
    }

    init {
        loadMyRooms(reset = true)
    }

    fun loadMyRooms(reset: Boolean = false) {
        if (isLoadingData && !reset) return
        if (isLastPage && !reset) return
        
        viewModelScope.launch {
            try {
                isLoadingData = true
                
                if (reset) {
                    updateState { it.copy(isLoading = true, myRooms = emptyList(), hasMore = true) }
                    nextCursor = null
                    isLastPage = false
                } else {
                    updateState { it.copy(isLoadingMore = true) }
                }

                repository.getMyRoomsByType(uiState.value.currentRoomType.value, nextCursor)
                    .onSuccess { myRoomListResponse ->
                        myRoomListResponse?.let { response ->
                            val currentList = if (reset) emptyList() else uiState.value.myRooms
                            updateState { 
                                it.copy(
                                    myRooms = currentList + response.roomList,
                                    error = null,
                                    hasMore = !response.isLast
                                ) 
                            }
                            nextCursor = response.nextCursor
                            isLastPage = response.isLast
                        } ?: run {
                            // null 응답 시 더 이상 로드할 수 없음을 명시
                            updateState { it.copy(hasMore = false) }
                            isLastPage = true
                        }
                    }
                    .onFailure { exception ->
                        updateState { it.copy(error = exception.message) }
                    }
            } finally {
                isLoadingData = false
                updateState { it.copy(isLoading = false, isLoadingMore = false) }
            }
        }
    }
    
    fun loadMoreMyRooms() {
        loadMyRooms(reset = false)
    }
    
    fun refreshData() {
        loadMyRooms(reset = true)
    }
    
    fun changeRoomType(roomType: RoomType) {
        if (roomType != uiState.value.currentRoomType) {
            updateState { it.copy(currentRoomType = roomType) }
            loadMyRooms(reset = true)
        }
    }
}