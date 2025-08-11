package com.texthip.thip.ui.search.viewmodel

import com.texthip.thip.data.model.book.response.BookSearchItem
import com.texthip.thip.data.model.book.response.PopularBookItem
import com.texthip.thip.data.model.book.response.RecentSearchItem

data class SearchBookUiState(
    val searchQuery: String = "",
    val liveSearchResults: List<BookSearchItem> = emptyList(), // Live search 결과 (검색어 입력 시)
    val searchResults: List<BookSearchItem> = emptyList(), // 완료된 검색 결과 (검색 버튼 클릭 시)
    val popularBooks: List<PopularBookItem> = emptyList(), // 인기 책 목록
    val recentSearches: List<RecentSearchItem> = emptyList(), // 최근 검색어 목록
    val isLiveSearching: Boolean = false, // Live search 로딩 상태
    val isSearching: Boolean = false, // 완료된 검색 로딩 상태
    val isLoadingPopularBooks: Boolean = false, // 인기 책 로딩 상태
    val isLoadingRecentSearches: Boolean = false, // 최근 검색어 로딩 상태
    val isLoadingMore: Boolean = false,
    val isLiveLoadingMore: Boolean = false, // Live search 무한 스크롤 로딩
    val hasMorePages: Boolean = true,
    val liveHasMorePages: Boolean = true, // Live search 페이징 정보
    val isFirstPage: Boolean = true,
    val currentPage: Int = 1,
    val liveCurrentPage: Int = 1, // Live search 현재 페이지
    val totalPages: Int = 0,
    val liveTotalPages: Int = 0, // Live search 총 페이지
    val totalElements: Int = 0,
    val liveTotalElements: Int = 0, // Live search 총 요소
    val isSearchCompleted: Boolean = false, // 검색 버튼을 눌러서 완료된 검색인지
    val error: String? = null,
    val showToast: Boolean = false,
    val toastMessage: String = ""
) {
    val hasLiveResults: Boolean get() = liveSearchResults.isNotEmpty()
    val hasSearchResults: Boolean get() = searchResults.isNotEmpty() && isSearchCompleted
    val canLoadMore: Boolean get() = hasMorePages && !isSearching && !isLoadingMore && isSearchCompleted
    val canLiveLoadMore: Boolean get() = liveHasMorePages && !isLiveSearching && !isLiveLoadingMore && !isSearchCompleted
    val showEmptyState: Boolean get() = searchQuery.isNotBlank() && liveSearchResults.isEmpty() && !isLiveSearching && !isSearchCompleted
}