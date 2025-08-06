package com.texthip.thip.ui.group.myroom.viewmodel

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
class GroupMyViewModel @Inject constructor(
    private val repository: GroupRepository
) : ViewModel() {

    private val _myRooms = MutableStateFlow<List<MyRoomCardData>>(emptyList())
    val myRooms: StateFlow<List<MyRoomCardData>> = _myRooms.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()
    
    private val _currentRoomType = MutableStateFlow("playingAndRecruiting")
    val currentRoomType: StateFlow<String> = _currentRoomType.asStateFlow()
    
    private var nextCursor: String? = null
    private var isLastPage = false
    private var isLoadingData = false

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
                    _isLoading.value = true
                    nextCursor = null
                    isLastPage = false
                    _myRooms.value = emptyList()
                } else {
                    _isLoadingMore.value = true
                }

                repository.getMyRoomsByType(_currentRoomType.value, nextCursor)
                    .onSuccess { paginationResult ->
                        val currentList = if (reset) emptyList() else _myRooms.value
                        _myRooms.value = currentList + paginationResult.data
                        nextCursor = paginationResult.nextCursor
                        isLastPage = paginationResult.isLast
                    }
                    .onFailure {
                        // 에러 처리 (필요시 에러 상태 추가)
                    }
            } finally {
                isLoadingData = false
                _isLoading.value = false
                _isLoadingMore.value = false
            }
        }
    }
    
    fun loadMoreMyRooms() {
        loadMyRooms(reset = false)
    }
    
    fun refreshData() {
        loadMyRooms(reset = true)
    }
    
    fun changeRoomType(roomType: String) {
        if (roomType != _currentRoomType.value) {
            _currentRoomType.value = roomType
            loadMyRooms(reset = true)
        }
    }
    
    // Room type
    companion object {
        const val PLAYING_AND_RECRUITING = "playingAndRecruiting"
        const val RECRUITING = "recruiting" 
        const val PLAYING = "playing"
    }
}