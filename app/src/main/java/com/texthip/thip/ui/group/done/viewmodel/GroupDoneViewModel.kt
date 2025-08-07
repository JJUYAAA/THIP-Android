package com.texthip.thip.ui.group.done.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.repository.GroupRepository
import com.texthip.thip.ui.group.done.mock.GroupDoneUiState
import com.texthip.thip.ui.group.myroom.mock.GroupMyUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupDoneViewModel @Inject constructor(
    private val repository: GroupRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GroupDoneUiState())
    val uiState: StateFlow<GroupDoneUiState> = _uiState.asStateFlow()
    
    private var nextCursor: String? = null
    private var isLastPage = false
    private var isLoadingMore = false
    
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
        if (isLoadingMore && !reset) return
        if (isLastPage && !reset) return
        
        viewModelScope.launch {
            try {
                if (reset) {
                    updateState { it.copy(isLoading = true, expiredRooms = emptyList()) }
                    nextCursor = null
                    isLastPage = false
                } else {
                    isLoadingMore = true
                }

                repository.getMyRoomsByType(GroupMyUiState.EXPIRED, nextCursor)
                    .onSuccess { paginationResult ->
                        val currentList = if (reset) emptyList() else uiState.value.expiredRooms
                        updateState { 
                            it.copy(
                                expiredRooms = currentList + paginationResult.data,
                                error = null
                            )
                        }
                        nextCursor = paginationResult.nextCursor
                        isLastPage = paginationResult.isLast
                    }
                    .onFailure { exception ->
                        updateState { it.copy(error = exception.message) }
                    }
            } finally {
                updateState { it.copy(isLoading = false) }
                isLoadingMore = false
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