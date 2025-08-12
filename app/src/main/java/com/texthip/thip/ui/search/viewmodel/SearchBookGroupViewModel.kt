package com.texthip.thip.ui.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.model.book.response.RecruitingRoomItem
import com.texthip.thip.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchBookGroupViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchBookGroupUiState())
    val uiState: StateFlow<SearchBookGroupUiState> = _uiState.asStateFlow()
    
    private var loadMoreJob: Job? = null
    private var currentIsbn: String = ""

    fun loadRecruitingRooms(isbn: String) {
        currentIsbn = isbn
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true, 
                error = null,
                recruitingRooms = emptyList(),
                nextCursor = null,
                hasMore = true,
                totalCount = 0
            )
            
            loadRooms(isbn, null)
        }
    }

    fun loadMoreRooms() {
        val currentState = _uiState.value
        if (currentState.hasMore && !currentState.isLoading && !currentState.isLoadingMore && currentIsbn.isNotBlank()) {
            loadMoreJob?.cancel()
            loadMoreJob = viewModelScope.launch {
                _uiState.value = _uiState.value.copy(isLoadingMore = true)
                loadRooms(currentIsbn, currentState.nextCursor)
            }
        }
    }

    private suspend fun loadRooms(isbn: String, cursor: String?) {
        bookRepository.getRecruitingRooms(isbn, cursor)
            .onSuccess { response ->
                response?.let { recruitingRoomsResponse ->
                    val currentRooms = if (cursor == null) emptyList() else _uiState.value.recruitingRooms
                    _uiState.value = _uiState.value.copy(
                        recruitingRooms = currentRooms + recruitingRoomsResponse.recruitingRoomList,
                        totalCount = recruitingRoomsResponse.totalRoomCount,
                        nextCursor = recruitingRoomsResponse.nextCursor,
                        hasMore = !recruitingRoomsResponse.isLast,
                        isLoading = false,
                        isLoadingMore = false,
                        error = null
                    )
                } ?: run {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoadingMore = false,
                        error = if (cursor == null) "모집중인 방 정보를 찾을 수 없습니다." else null
                    )
                }
            }
            .onFailure { exception ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isLoadingMore = false,
                    error = exception.message ?: "모집중인 방을 불러오는데 실패했습니다."
                )
            }
    }

    override fun onCleared() {
        super.onCleared()
        loadMoreJob?.cancel()
    }
}

data class SearchBookGroupUiState(
    val recruitingRooms: List<RecruitingRoomItem> = emptyList(),
    val totalCount: Int = 0,
    val nextCursor: String? = null,
    val hasMore: Boolean = true,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null
) {
    val canLoadMore: Boolean get() = hasMore && !isLoading && !isLoadingMore
}