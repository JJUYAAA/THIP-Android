package com.texthip.thip.ui.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.model.book.response.BookDetailResponse
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
        loadMoreJob?.cancel() // 신규 검색 시 이전 로드 작업 취소
        currentIsbn = isbn
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true, 
                error = null,
                recruitingRooms = emptyList(),
                nextCursor = null,
                hasMore = true,
                totalCount = 0,
                bookDetail = null
            )
            
            // 책 정보와 모집중인 방 정보 동시 로드
            loadBookDetail(isbn)
            loadRooms(isbn, null)
        }
    }

    private suspend fun loadBookDetail(isbn: String) {
        bookRepository.getBookDetail(isbn)
            .onSuccess { bookDetail ->
                _uiState.value = _uiState.value.copy(bookDetail = bookDetail)
            }
            .onFailure { }
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
                        hasMore = false, // null 응답 시 더 이상 로드할 수 없음을 명시
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
    val error: String? = null,
    val bookDetail: BookDetailResponse? = null
) {
    val canLoadMore: Boolean get() = hasMore && !isLoading && !isLoadingMore
}