package com.texthip.thip.ui.group.done.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.model.repository.GroupRepository
import com.texthip.thip.ui.group.done.mock.MyRoomCardData
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

    private val _expiredRooms = MutableStateFlow<List<MyRoomCardData>>(emptyList())
    val expiredRooms: StateFlow<List<MyRoomCardData>> = _expiredRooms.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()
    
    private var nextCursor: String? = null
    private var isLastPage = false
    private var isLoadingMore = false

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
                    _userName.value = name
                }
        }
    }

    fun loadExpiredRooms(reset: Boolean = false) {
        if (isLoadingMore && !reset) return
        if (isLastPage && !reset) return
        
        viewModelScope.launch {
            try {
                if (reset) {
                    _isLoading.value = true
                    nextCursor = null
                    isLastPage = false
                    _expiredRooms.value = emptyList()
                } else {
                    isLoadingMore = true
                }

                repository.getMyRoomsByType("expired", nextCursor)
                    .onSuccess { paginationResult ->
                        val currentList = if (reset) emptyList() else _expiredRooms.value
                        _expiredRooms.value = currentList + paginationResult.data
                        nextCursor = paginationResult.nextCursor
                        isLastPage = paginationResult.isLast
                    }
                    .onFailure {
                    }
            } finally {
                _isLoading.value = false
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