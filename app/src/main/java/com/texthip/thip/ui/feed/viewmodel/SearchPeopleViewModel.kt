package com.texthip.thip.ui.feed.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.repository.RecentSearchRepository
import com.texthip.thip.data.repository.UserRepository
import com.texthip.thip.ui.feed.mock.MySubscriptionData
import com.texthip.thip.ui.feed.mock.toMySubscriptionData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.onSuccess

data class RecentSearchUiItem(
    val id: Long,
    val term: String
)
data class SearchPeopleUiState(
    val searchText: String = "",
    val isSearched: Boolean = false, // 최종 검색 완료 여부
    val searchResults: List<MySubscriptionData> = emptyList(),
    val recentSearches: List<RecentSearchUiItem> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class SearchPeopleViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val recentSearchRepository: RecentSearchRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchPeopleUiState())
    val uiState = _uiState.asStateFlow()

    private var searchJob: Job? = null

    //최근 검색어
    fun fetchRecentSearches() {
        viewModelScope.launch {
            recentSearchRepository.getRecentSearches("USER")
                .onSuccess { response ->
                    val searchItems = response?.recentSearchList?.map {
                        RecentSearchUiItem(id = it.recentSearchId.toLong(), term = it.searchTerm)
                    } ?: emptyList()
                    _uiState.update { it.copy(recentSearches = searchItems) }
                }
                .onFailure { exception ->
                    _uiState.update { it.copy(errorMessage = exception.message) }
                }
        }
    }
    //최근 검색어 삭제
    fun removeRecentSearch(searchId: Long) {
        viewModelScope.launch {
            // UI에서 먼저 즉시 삭제
            val currentSearches = _uiState.value.recentSearches
            val updatedSearches = currentSearches.filterNot { it.id == searchId }
            _uiState.update { it.copy(recentSearches = updatedSearches) }

            // 서버에 삭제 요청
            recentSearchRepository.deleteRecentSearch(searchId.toInt())
                .onFailure {
                    // 실패 시 UI를 원래대로 복구
                    _uiState.update { it.copy(recentSearches = currentSearches) }
                }
        }
    }

    // 사용자가 텍스트를 입력할 때 호출
    fun onSearchTextChanged(text: String) {
        _uiState.update { it.copy(searchText = text, isSearched = false) }

        // 이전 검색 요청이 있다면 취소
        searchJob?.cancel()

        if (text.isNotBlank()) {
            searchJob = viewModelScope.launch {
                delay(500L)
                searchUsers(keyword = text, isFinalized = false)
            }
        } else {
            // 입력창이 비워지면 검색 결과도 비움
            _uiState.update { it.copy(searchResults = emptyList()) }
        }
    }

    // 키보드의 '검색' 버튼이나 아이콘을 눌렀을 때 호출
    fun onFinalSearch(query: String) {
        searchJob?.cancel()
        _uiState.update { it.copy(isSearched = true) }

        if (query.isNotBlank()) {
            addRecentSearch(query)
            searchUsers(keyword = query, isFinalized = true)
        } else {
            _uiState.update { it.copy(searchResults = emptyList()) }
        }
    }

    // 실제 API를 호출하는 private 함수
    private fun searchUsers(keyword: String, isFinalized: Boolean) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            userRepository.searchUsers(keyword, isFinalized)
                .onSuccess { response ->
                    val userList =
                        response?.userList?.map { it.toMySubscriptionData() } ?: emptyList()
                    _uiState.update { it.copy(isLoading = false, searchResults = userList) }
                }
                .onFailure { exception ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = exception.message) }
                }
        }
    }

    // 최근 검색어 관련 로직
    fun addRecentSearch(keyword: String) {
        _uiState.update { currentState ->
            val newItem = RecentSearchUiItem(id = -1L, term = keyword)
            val updatedSearches = (listOf(newItem) + currentState.recentSearches)
                .distinctBy { it.term }
                .take(5)
            currentState.copy(recentSearches = updatedSearches)
        }
    }
}
