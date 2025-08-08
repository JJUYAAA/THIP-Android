package com.texthip.thip.ui.group.note.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.model.rooms.request.RoomsPostsRequestParams
import com.texthip.thip.data.model.rooms.response.PostList
import com.texthip.thip.data.repository.RoomsRepository
import com.texthip.thip.utils.type.SortType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GroupNoteUiState(
    // 데이터 로딩 상태
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val isLastPage: Boolean = false,

    // 화면 데이터
    val posts: List<PostList> = emptyList(),

    // 필터 및 탭 상태
    val selectedTabIndex: Int = 0,
    val selectedSort: SortType = SortType.LATEST,
    val pageStart: String = "",
    val pageEnd: String = "",
    val isOverview: Boolean = false,
    val isPageFilter: Boolean = false,
    val totalEnabled: Boolean = false
)

sealed interface GroupNoteEvent {
    data class OnTabSelected(val index: Int) : GroupNoteEvent
    data class OnSortSelected(val sortType: SortType) : GroupNoteEvent
    data class OnPageStartChanged(val page: String) : GroupNoteEvent
    data class OnPageEndChanged(val page: String) : GroupNoteEvent
    data class OnOverviewToggled(val isSelected: Boolean) : GroupNoteEvent
    data object ApplyPageFilter : GroupNoteEvent
    data object LoadMorePosts : GroupNoteEvent
}


@HiltViewModel
class GroupNoteViewModel @Inject constructor(
    private val roomsRepository: RoomsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GroupNoteUiState())
    val uiState = _uiState.asStateFlow()

    private var nextCursor: String? = null
    private var roomId: Int = -1

    fun initialize(roomId: Int) {
        this.roomId = roomId
        loadPosts(isRefresh = true)
    }

    fun onEvent(event: GroupNoteEvent) {
        when (event) {
            is GroupNoteEvent.OnTabSelected -> {
                _uiState.update { it.copy(selectedTabIndex = event.index) }
                loadPosts(isRefresh = true)
            }
            is GroupNoteEvent.OnSortSelected -> {
                _uiState.update { it.copy(selectedSort = event.sortType) }
                loadPosts(isRefresh = true)
            }
            is GroupNoteEvent.OnPageStartChanged -> _uiState.update { it.copy(pageStart = event.page) }
            is GroupNoteEvent.OnPageEndChanged -> _uiState.update { it.copy(pageEnd = event.page) }
            is GroupNoteEvent.OnOverviewToggled -> _uiState.update { it.copy(isOverview = event.isSelected) }
            GroupNoteEvent.ApplyPageFilter -> loadPosts(isRefresh = true)
            GroupNoteEvent.LoadMorePosts -> loadPosts(isRefresh = false)
        }
    }

    private fun loadPosts(isRefresh: Boolean = false) {
        val currentState = _uiState.value
        if (currentState.isLoading || currentState.isLoadingMore || (currentState.isLastPage && !isRefresh)) return

        viewModelScope.launch {
            _uiState.update {
                if (isRefresh) it.copy(isLoading = true, posts = emptyList(), error = null, isLastPage = false)
                else it.copy(isLoadingMore = true, error = null)
            }

            val cursor = if (isRefresh) null else nextCursor
            val type = if (currentState.selectedTabIndex == 0) "group" else "mine"

            val params = if (type == "mine") {
                // "mine" 탭일 경우 필수 파라미터만 채워 넣음
                RoomsPostsRequestParams(type = type, cursor = cursor)
            } else {
                // "group" 탭일 경우 모든 필터 파라미터 포함
                RoomsPostsRequestParams(
                    type = type,
                    sort = currentState.selectedSort.apiKey,
                    pageStart = currentState.pageStart.toIntOrNull(),
                    pageEnd = currentState.pageEnd.toIntOrNull(),
                    isOverview = currentState.isOverview,
                    isPageFilter = currentState.isPageFilter,
                    cursor = cursor
                )
            }

            roomsRepository.getRoomsPosts(
                roomId = roomId,
                type = params.type,
                sort = params.sort,
                pageStart = params.pageStart,
                pageEnd = params.pageEnd,
                isOverview = params.isOverview,
                isPageFilter = params.isPageFilter,
                cursor = params.cursor
            ).onSuccess { response ->
                if (response != null) {
                    nextCursor = response.nextCursor
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isLoadingMore = false,
                            posts = if (isRefresh) response.postList else it.posts + response.postList,
                            isLastPage = response.isLast
                        )
                    }
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(isLoading = false, isLoadingMore = false, error = throwable.message)
                }
            }
        }
    }
}
