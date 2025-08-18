package com.texthip.thip.ui.group.done.viewmodel

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
class GroupDoneViewModel @Inject constructor(
    private val repository: RoomsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GroupDoneUiState())
    val uiState: StateFlow<GroupDoneUiState> = _uiState.asStateFlow()
    
    private var nextCursor: String? = null
    private var isLastPage = false
    private var isLoadingMore = false
    private var isInitialLoading = false
    
    private fun updateState(update: (GroupDoneUiState) -> GroupDoneUiState) {
        _uiState.value = update(_uiState.value)
    }

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        loadUserName()
        loadExpiredRooms(reset = true)
    }
    
    private fun loadUserName() {
        viewModelScope.launch {
            repository.getUserName()
                .onSuccess { name ->
                    updateState { it.copy(userName = name) }
                }
        }
    }

    fun loadExpiredRooms(reset: Boolean = false) {
        // 중복 호출 방지
        if (reset) {
            if (isInitialLoading) return
            isInitialLoading = true
        } else {
            if (isLoadingMore || isLastPage) return
            isLoadingMore = true
        }
        
        viewModelScope.launch {
            try {
                if (reset) {
                    updateState { it.copy(isLoading = true, expiredRooms = emptyList(), hasMore = true) }
                    nextCursor = null
                    isLastPage = false
                }

                repository.getMyRoomsByType(RoomType.EXPIRED.value, nextCursor)
                    .onSuccess { myRoomListResponse ->
                        myRoomListResponse?.let { response ->
                            val currentList = if (reset) emptyList() else uiState.value.expiredRooms
                            updateState { 
                                it.copy(
                                    expiredRooms = currentList + response.roomList,
                                    error = null,
                                    isLoadingMore = false,
                                    hasMore = !response.isLast
                                )
                            }
                            nextCursor = response.nextCursor
                            isLastPage = response.isLast
                        } ?: run {
                            // null 응답 시 더 이상 로드할 수 없음을 명시
                            updateState { it.copy(hasMore = false, isLoadingMore = false) }
                            isLastPage = true
                        }
                    }
                    .onFailure { exception ->
                        updateState { it.copy(error = exception.message) }
                    }
            } finally {
                if (reset) {
                    updateState { it.copy(isLoading = false) }
                    isInitialLoading = false
                } else {
                    updateState { it.copy(isLoadingMore = false) }
                    isLoadingMore = false
                }
            }
        }
    }
    
    fun loadMoreExpiredRooms() {
        loadExpiredRooms(reset = false)
    }
    
    fun refreshData() {
        loadExpiredRooms(reset = true)
    }
}