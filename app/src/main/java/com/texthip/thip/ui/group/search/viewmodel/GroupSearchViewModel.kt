package com.texthip.thip.ui.group.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.model.book.response.RecentSearchItem
import com.texthip.thip.data.repository.RecentSearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GroupSearchUiState(
    val recentSearches: List<RecentSearchItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class GroupSearchViewModel @Inject constructor(
    private val recentSearchRepository: RecentSearchRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GroupSearchUiState())
    val uiState: StateFlow<GroupSearchUiState> = _uiState.asStateFlow()
    
    // Map 기반 빠른 최근 검색어 관리
    private val recentSearchMap = mutableMapOf<String, RecentSearchItem>()

    init {
        loadRecentSearches()
    }
    
    private fun updateState(update: (GroupSearchUiState) -> GroupSearchUiState) {
        _uiState.update(update)
    }

    fun loadRecentSearches() {
        viewModelScope.launch {
            updateState { it.copy(isLoading = true) }
            
            recentSearchRepository.getRecentSearches("ROOM")
                .onSuccess { response ->
                    response?.let { recentSearchResponse ->
                        // Map에 최근 검색어 저장 (빠른 검색을 위해)
                        recentSearchMap.clear()
                        recentSearchResponse.recentSearchList.forEach { item ->
                            recentSearchMap[item.searchTerm] = item
                        }
                        
                        updateState {
                            it.copy(
                                recentSearches = recentSearchResponse.recentSearchList,
                                isLoading = false,
                                error = null
                            )
                        }
                    } ?: run {
                        updateState {
                            it.copy(
                                recentSearches = emptyList(),
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                }
                .onFailure { throwable ->
                    updateState {
                        it.copy(
                            recentSearches = emptyList(),
                            isLoading = false,
                            error = throwable.message ?: "최근 검색어를 불러오는 중 오류가 발생했습니다."
                        )
                    }
                }
        }
    }

    fun deleteRecentSearch(recentSearchId: Int) {
        viewModelScope.launch {
            recentSearchRepository.deleteRecentSearch(recentSearchId)
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

    fun refreshData() {
        loadRecentSearches()
    }
}