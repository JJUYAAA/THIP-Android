package com.texthip.thip.ui.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchBookViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchBookUiState())
    val uiState: StateFlow<SearchBookUiState> = _uiState.asStateFlow()
    
    private var searchJob: Job? = null
    private var loadMoreJob: Job? = null

    init {
        loadPopularBooks()
        loadRecentSearches()
    }
    
    private fun updateState(update: (SearchBookUiState) -> SearchBookUiState) {
        _uiState.value = update(_uiState.value)
    }

    fun updateSearchQuery(query: String) {
        updateState { it.copy(searchQuery = query, isSearchCompleted = false) }
        
        // Live search with debounce
        searchJob?.cancel()
        if (query.isNotBlank()) {
            searchJob = viewModelScope.launch {
                delay(300) // debounce
                performLiveSearch(query)
            }
        } else {
            clearSearchResults()
        }
    }

    fun onSearchButtonClick() {
        val query = uiState.value.searchQuery.trim()
        if (query.isNotBlank()) {
            performCompleteSearch(query)
            // 검색 완료 후 최신 검색어 목록 불러오기 (새로운 검색어가 추가되었을 수 있음)
            loadRecentSearches()
        }
    }

    fun loadMoreBooks() {
        val currentState = uiState.value
        if (currentState.canLoadMore && currentState.searchQuery.isNotBlank()) {
            loadMoreJob?.cancel()
            loadMoreJob = viewModelScope.launch {
                performLoadMore(currentState.searchQuery)
            }
        }
    }

    fun loadMoreLiveSearchResults() {
        val currentState = uiState.value
        if (currentState.canLiveLoadMore && currentState.searchQuery.isNotBlank()) {
            loadMoreJob?.cancel()
            loadMoreJob = viewModelScope.launch {
                performLiveSearchLoadMore(currentState.searchQuery)
            }
        }
    }

    private fun performLiveSearch(query: String) {
        viewModelScope.launch {
            try {
                updateState { 
                    it.copy(
                        isLiveSearching = true, 
                        error = null,
                        liveSearchResults = emptyList(), // 기존 Live search 결과 클리어
                        liveCurrentPage = 1
                    ) 
                }

                bookRepository.searchBooks(query, 1)
                    .onSuccess { searchResponse ->
                        searchResponse?.let { response ->
                            updateState {
                                it.copy(
                                    liveSearchResults = response.searchResult, // Live search는 전체 결과 표시 (무한스크롤 포함)
                                    liveCurrentPage = response.page,
                                    liveTotalPages = response.totalPages,
                                    liveTotalElements = response.totalElements,
                                    liveHasMorePages = !response.last,
                                    isLiveSearching = false,
                                    error = null
                                )
                            }
                        } ?: run {
                            updateState {
                                it.copy(
                                    liveSearchResults = emptyList(),
                                    isLiveSearching = false,
                                    error = null
                                )
                            }
                        }
                    }
                    .onFailure {
                        updateState {
                            it.copy(
                                liveSearchResults = emptyList(),
                                isLiveSearching = false,
                                error = null // Live search 에러는 조용히 처리
                            )
                        }
                    }
            } catch (e: Exception) {
                updateState {
                    it.copy(
                        liveSearchResults = emptyList(),
                        isLiveSearching = false,
                        error = null
                    )
                }
            }
        }
    }

    private fun performLiveSearchLoadMore(query: String) {
        viewModelScope.launch {
            try {
                val currentState = uiState.value
                val nextPage = currentState.liveCurrentPage + 1
                
                updateState { it.copy(isLiveLoadingMore = true) }

                bookRepository.searchBooks(query, nextPage)
                    .onSuccess { searchResponse ->
                        searchResponse?.let { response ->
                            updateState {
                                it.copy(
                                    liveSearchResults = it.liveSearchResults + response.searchResult,
                                    liveCurrentPage = response.page,
                                    liveTotalPages = response.totalPages,
                                    liveTotalElements = response.totalElements,
                                    liveHasMorePages = !response.last,
                                    isLiveLoadingMore = false,
                                    error = null
                                )
                            }
                        } ?: run {
                            updateState {
                                it.copy(
                                    isLiveLoadingMore = false,
                                    error = null
                                )
                            }
                        }
                    }
                    .onFailure {
                        updateState {
                            it.copy(
                                isLiveLoadingMore = false,
                                error = null // Live search 에러는 조용히 처리
                            )
                        }
                    }
            } catch (e: Exception) {
                updateState {
                    it.copy(
                        isLiveLoadingMore = false,
                        error = null
                    )
                }
            }
        }
    }

    private fun performCompleteSearch(query: String) {
        viewModelScope.launch {
            try {
                updateState { 
                    it.copy(
                        isSearching = true, 
                        isSearchCompleted = true,
                        error = null,
                        searchResults = emptyList(), // 기존 검색 결과 클리어
                        currentPage = 1
                    ) 
                }

                bookRepository.searchBooks(query, 1)
                    .onSuccess { searchResponse ->
                        searchResponse?.let { response ->
                            updateState {
                                it.copy(
                                    searchResults = response.searchResult,
                                    currentPage = response.page,
                                    totalPages = response.totalPages,
                                    totalElements = response.totalElements,
                                    hasMorePages = !response.last,
                                    isFirstPage = response.first,
                                    isSearching = false,
                                    error = null
                                )
                            }
                        } ?: run {
                            updateState {
                                it.copy(
                                    searchResults = emptyList(),
                                    isSearching = false,
                                    error = "검색 결과를 불러올 수 없습니다."
                                )
                            }
                        }
                    }
                    .onFailure { throwable ->
                        updateState {
                            it.copy(
                                searchResults = emptyList(),
                                isSearching = false,
                                error = throwable.message ?: "검색 중 오류가 발생했습니다."
                            )
                        }
                    }
            } catch (e: Exception) {
                updateState {
                    it.copy(
                        searchResults = emptyList(),
                        isSearching = false,
                        error = e.message ?: "검색 중 오류가 발생했습니다."
                    )
                }
            }
        }
    }

    private fun performLoadMore(query: String) {
        viewModelScope.launch {
            try {
                val currentState = uiState.value
                val nextPage = currentState.currentPage + 1
                
                updateState { it.copy(isLoadingMore = true) }

                bookRepository.searchBooks(query, nextPage)
                    .onSuccess { searchResponse ->
                        searchResponse?.let { response ->
                            updateState {
                                it.copy(
                                    searchResults = it.searchResults + response.searchResult,
                                    currentPage = response.page,
                                    totalPages = response.totalPages,
                                    totalElements = response.totalElements,
                                    hasMorePages = !response.last,
                                    isLoadingMore = false,
                                    error = null
                                )
                            }
                        } ?: run {
                            updateState {
                                it.copy(
                                    isLoadingMore = false,
                                    error = "추가 결과를 불러올 수 없습니다."
                                )
                            }
                        }
                    }
                    .onFailure { throwable ->
                        updateState {
                            it.copy(
                                isLoadingMore = false,
                                error = throwable.message ?: "추가 결과를 불러오는 중 오류가 발생했습니다."
                            )
                        }
                    }
            } catch (e: Exception) {
                updateState {
                    it.copy(
                        isLoadingMore = false,
                        error = e.message ?: "추가 결과를 불러오는 중 오류가 발생했습니다."
                    )
                }
            }
        }
    }


    private fun loadPopularBooks() {
        viewModelScope.launch {
            try {
                updateState { it.copy(isLoadingPopularBooks = true) }

                bookRepository.getMostSearchedBooks()
                    .onSuccess { response ->
                        response?.let { mostSearchedBooks ->
                            updateState {
                                it.copy(
                                    popularBooks = mostSearchedBooks.bookList,
                                    isLoadingPopularBooks = false,
                                    error = null
                                )
                            }
                        } ?: run {
                            updateState {
                                it.copy(
                                    popularBooks = emptyList(),
                                    isLoadingPopularBooks = false,
                                    error = null
                                )
                            }
                        }
                    }
                    .onFailure {
                        updateState {
                            it.copy(
                                popularBooks = emptyList(),
                                isLoadingPopularBooks = false,
                                error = null // 인기 책 로딩 실패는 조용히 처리
                            )
                        }
                    }
            } catch (e: Exception) {
                updateState {
                    it.copy(
                        popularBooks = emptyList(),
                        isLoadingPopularBooks = false,
                        error = null
                    )
                }
            }
        }
    }

    private fun loadRecentSearches() {
        viewModelScope.launch {
            try {
                updateState { it.copy(isLoadingRecentSearches = true) }

                bookRepository.getRecentSearches()
                    .onSuccess { response ->
                        response?.let { recentSearchResponse ->
                            updateState {
                                it.copy(
                                    recentSearches = recentSearchResponse.recentSearchList,
                                    isLoadingRecentSearches = false,
                                    error = null
                                )
                            }
                        } ?: run {
                            updateState {
                                it.copy(
                                    recentSearches = emptyList(),
                                    isLoadingRecentSearches = false,
                                    error = null
                                )
                            }
                        }
                    }
                    .onFailure { throwable ->
                        updateState {
                            it.copy(
                                recentSearches = emptyList(),
                                isLoadingRecentSearches = false,
                                error = null // 최근 검색어 로딩 실패는 조용히 처리
                            )
                        }
                    }
            } catch (e: Exception) {
                updateState {
                    it.copy(
                        recentSearches = emptyList(),
                        isLoadingRecentSearches = false,
                        error = null
                    )
                }
            }
        }
    }

    fun deleteRecentSearch(recentSearchId: Int) {
        viewModelScope.launch {
            try {
                bookRepository.deleteRecentSearch(recentSearchId)
                    .onSuccess {
                        // 삭제 성공 시 목록 새로고침
                        loadRecentSearches()
                    }
                    .onFailure {
                        // 삭제 실패는 조용히 처리하거나 Toast로 알림 표시 가능
                    }
            } catch (e: Exception) {
                // 예외 처리
            }
        }
    }

    private fun clearSearchResults() {
        searchJob?.cancel()
        loadMoreJob?.cancel()
        updateState {
            SearchBookUiState(
                searchQuery = it.searchQuery,
                popularBooks = it.popularBooks, // 인기 책은 유지
                recentSearches = it.recentSearches // 최근 검색어도 유지
            )
        }
        // 검색어가 삭제되어 초기화면으로 돌아갈 때 최신 검색기록 불러오기
        loadRecentSearches()
    }

    fun showToastMessage(message: String) {
        updateState {
            it.copy(
                toastMessage = message,
                showToast = true
            )
        }
    }

    fun hideToast() {
        updateState { it.copy(showToast = false) }
    }

    fun clearError() {
        updateState { it.copy(error = null) }
    }

    fun refreshData() {
        loadPopularBooks()
        loadRecentSearches()
    }

    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
        loadMoreJob?.cancel()
    }
}