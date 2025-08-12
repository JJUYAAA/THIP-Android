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
        loadInitialData()
    }
    
    private fun updateState(update: (SearchBookUiState) -> SearchBookUiState) {
        _uiState.value = update(_uiState.value)
    }

    fun updateSearchQuery(query: String) {
        updateState { it.copy(searchQuery = query) }

        searchJob?.cancel()
        if (query.isNotBlank()) {
            updateState { it.copy(searchMode = SearchMode.LiveSearch) }
            searchJob = viewModelScope.launch {
                delay(1000) // 디바운싱
                performSearch(query, isLiveSearch = true)
            }
        } else {
            clearSearchResults()
        }
    }

    fun onSearchButtonClick() {
        val query = uiState.value.searchQuery.trim()
        if (query.isNotBlank()) {
            searchJob?.cancel()
            updateState { it.copy(searchMode = SearchMode.CompleteSearch) }
            viewModelScope.launch {
                performSearch(query, isLiveSearch = false)
                loadRecentSearches()
            }
        }
    }

    fun loadMoreBooks() {
        val currentState = uiState.value
        if (currentState.canLoadMore && currentState.searchQuery.isNotBlank()) {
            loadMoreJob?.cancel()
            loadMoreJob = viewModelScope.launch {
                performLoadMore()
            }
        }
    }

    private suspend fun performSearch(query: String, isLiveSearch: Boolean) {
        try {
            updateState { 
                it.copy(
                    isSearching = true,
                    error = null,
                    searchResults = emptyList(),
                    currentPage = 1
                ) 
            }

            delay(if (isLiveSearch) 0 else 1000) // Complete search에만 딜레이

            bookRepository.searchBooks(query, 1)
                .onSuccess { response ->
                    response?.let { searchResponse ->
                        updateState {
                            it.copy(
                                searchResults = searchResponse.searchResult,
                                currentPage = searchResponse.page,
                                totalElements = searchResponse.totalElements,
                                hasMorePages = !searchResponse.last,
                                isSearching = false,
                                error = null
                            )
                        }
                    } ?: run {
                        updateState {
                            it.copy(
                                searchResults = emptyList(),
                                isSearching = false,
                                error = if (isLiveSearch) null else "검색 결과를 불러올 수 없습니다."
                            )
                        }
                    }
                }
                .onFailure { throwable ->
                    updateState {
                        it.copy(
                            searchResults = emptyList(),
                            isSearching = false,
                            error = if (isLiveSearch) null else (throwable.message ?: "검색 중 오류가 발생했습니다.")
                        )
                    }
                }
        } catch (e: Exception) {
            updateState {
                it.copy(
                    searchResults = emptyList(),
                    isSearching = false,
                    error = if (isLiveSearch) null else (e.message ?: "검색 중 오류가 발생했습니다.")
                )
            }
        }
    }

    private suspend fun performLoadMore() {
        try {
            val currentState = uiState.value
            val nextPage = currentState.currentPage + 1
            
            updateState { it.copy(isLoadingMore = true) }
            delay(1000) // 로딩 표시를 위한 딜레이

            bookRepository.searchBooks(currentState.searchQuery, nextPage)
                .onSuccess { response ->
                    response?.let { searchResponse ->
                        updateState {
                            it.copy(
                                searchResults = it.searchResults + searchResponse.searchResult,
                                currentPage = searchResponse.page,
                                totalElements = searchResponse.totalElements,
                                hasMorePages = !searchResponse.last,
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

    private fun loadInitialData() {
        loadPopularBooks()
        loadRecentSearches()
    }

    private fun loadPopularBooks() {
        viewModelScope.launch {
            try {
                bookRepository.getMostSearchedBooks()
                    .onSuccess { response ->
                        response?.let { mostSearchedBooks ->
                            updateState {
                                it.copy(popularBooks = mostSearchedBooks.bookList)
                            }
                        }
                    }
                    .onFailure {
                        // 인기 책 로딩 실패는 조용히 처리
                    }
            } catch (e: Exception) {
                // 예외 처리 - 조용히 처리
            }
        }
    }

    private fun loadRecentSearches() {
        viewModelScope.launch {
            try {
                bookRepository.getRecentSearches()
                    .onSuccess { response ->
                        response?.let { recentSearchResponse ->
                            updateState {
                                it.copy(recentSearches = recentSearchResponse.recentSearchList)
                            }
                        }
                    }
                    .onFailure {
                        // 최근 검색어 로딩 실패는 조용히 처리
                    }
            } catch (e: Exception) {
                // 예외 처리 - 조용히 처리
            }
        }
    }

    fun deleteRecentSearch(recentSearchId: Int) {
        viewModelScope.launch {
            try {
                bookRepository.deleteRecentSearch(recentSearchId)
                    .onSuccess {
                        loadRecentSearches() // 삭제 성공 시 목록 새로고침
                    }
                    .onFailure {
                        // 삭제 실패는 조용히 처리
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
            it.copy(
                searchQuery = "",
                searchMode = SearchMode.Initial,
                searchResults = emptyList(),
                currentPage = 1,
                hasMorePages = true,
                isSearching = false,
                isLoadingMore = false,
                error = null
            )
        }
    }

    fun refreshData() {
        loadInitialData()
    }

    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
        loadMoreJob?.cancel()
    }
}