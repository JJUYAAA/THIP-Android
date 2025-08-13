package com.texthip.thip.ui.search.viewmodel

import com.texthip.thip.data.model.book.response.BookSearchItem
import com.texthip.thip.data.model.book.response.PopularBookItem
import com.texthip.thip.data.model.book.response.RecentSearchItem

data class SearchBookUiState(
    val searchQuery: String = "",
    
    // 상태 관리 단순화 - boolean 필드 사용
    val isInitial: Boolean = true,
    val isLiveSearching: Boolean = false,
    val isCompleteSearching: Boolean = false,
    
    // 통합된 검색 결과 (Live/Complete 구분 없이)
    val searchResults: List<BookSearchItem> = emptyList(),
    val popularBooks: List<PopularBookItem> = emptyList(),
    val recentSearches: List<RecentSearchItem> = emptyList(),
    
    // 로딩 상태
    val isSearching: Boolean = false,
    val isLoadingMore: Boolean = false,
    
    // 페이징 정보
    val currentPage: Int = 1,
    val totalElements: Int = 0,
    val hasMorePages: Boolean = true,
    
    // 에러/토스트
    val error: String? = null,
    val showToast: Boolean = false,
    val toastMessage: String = ""
) {
    val hasResults: Boolean get() = searchResults.isNotEmpty()
    val canLoadMore: Boolean get() = hasMorePages && !isSearching && !isLoadingMore
    val showEmptyState: Boolean get() = searchQuery.isNotBlank() && searchResults.isEmpty() && !isSearching
    val showInitialScreen: Boolean get() = isInitial && searchQuery.isBlank()
    val isAnySearching: Boolean get() = isLiveSearching || isCompleteSearching
}