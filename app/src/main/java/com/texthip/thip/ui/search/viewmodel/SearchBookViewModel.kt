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


    private fun clearSearchResults() {
        searchJob?.cancel()
        loadMoreJob?.cancel()
        updateState {
            SearchBookUiState(searchQuery = it.searchQuery)
        }
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

    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
        loadMoreJob?.cancel()
    }
}