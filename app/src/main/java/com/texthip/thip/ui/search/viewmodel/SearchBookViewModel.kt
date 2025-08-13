package com.texthip.thip.ui.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.model.book.response.RecentSearchItem
import com.texthip.thip.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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
    
    // Map 기반 빠른 최근 검색어 관리
    private val recentSearchMap = mutableMapOf<String, RecentSearchItem>()

    init {
        loadInitialData()
    }
    
    private fun updateState(update: (SearchBookUiState) -> SearchBookUiState) {
        _uiState.update(update)
    }

    fun updateSearchQuery(query: String) {
        updateState { it.copy(searchQuery = query) }
        searchJob?.cancel()
        loadMoreJob?.cancel()

        if (query.isNotBlank()) {
            updateState { 
                it.copy(
                    isInitial = false,
                    isLiveSearching = true,
                    isCompleteSearching = false
                ) 
            }
            searchJob = viewModelScope.launch {
                delay(1000) // Live search에 딜레이 추가
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
            loadMoreJob?.cancel()

            updateState { 
                it.copy(
                    isInitial = false,
                    isLiveSearching = false,
                    isCompleteSearching = true
                ) 
            }
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
        updateState { 
            it.copy(
                isSearching = true,
                error = null,
                searchResults = emptyList(),
                currentPage = 1
            ) 
        }

        bookRepository.searchBooks(query, 1, isFinalized = !isLiveSearch)
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
                            isLiveSearching = false,
                            isCompleteSearching = false,
                            hasMorePages = false,
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
                        isLiveSearching = false,
                        isCompleteSearching = false,
                        error = if (isLiveSearch) null else (throwable.message ?: "검색 중 오류가 발생했습니다.")
                    )
                }
            }
    }

    private suspend fun performLoadMore() {
        val currentState = uiState.value
        val nextPage = currentState.currentPage + 1
        
        updateState { it.copy(isLoadingMore = true) }

        bookRepository.searchBooks(currentState.searchQuery, nextPage, isFinalized = true)
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
                            hasMorePages = false, // null 응답 시 더 이상 페이지가 없음을 명시
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
    }

    private fun loadInitialData() {
        loadPopularBooks()
        loadRecentSearches()
    }

    private fun loadPopularBooks() {
        viewModelScope.launch {
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
        }
    }

    private fun loadRecentSearches() {
        viewModelScope.launch {
            bookRepository.getRecentSearches()
                .onSuccess { response ->
                    response?.let { recentSearchResponse ->
                        // Map에 최근 검색어 저장 (빠른 검색을 위해)
                        recentSearchMap.clear()
                        recentSearchResponse.recentSearchList.forEach { item ->
                            recentSearchMap[item.searchTerm] = item
                        }
                        
                        updateState {
                            it.copy(recentSearches = recentSearchResponse.recentSearchList)
                        }
                    }
                }
                .onFailure {
                    // 최근 검색어 로딩 실패는 조용히 처리
                }
        }
    }

    fun deleteRecentSearch(recentSearchId: Int) {
        viewModelScope.launch {
            bookRepository.deleteRecentSearch(recentSearchId)
                .onSuccess {
                    loadRecentSearches() // 삭제 성공 시 목록 새로고침
                }
                .onFailure {
                    // 삭제 실패는 조용히 처리
                }
        }
    }
    
    /** 키워드로 빠른 최근 검색어 삭제 (Map 기반) */
    fun deleteRecentSearchByKeyword(keyword: String) {
        recentSearchMap[keyword]?.let { recentSearchItem ->
            deleteRecentSearch(recentSearchItem.recentSearchId)
        }
    }

    private fun clearSearchResults() {
        searchJob?.cancel()
        loadMoreJob?.cancel()
        updateState {
            it.copy(
                searchQuery = "",
                isInitial = true,
                isLiveSearching = false,
                isCompleteSearching = false,
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