package com.texthip.thip.ui.feed.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.model.feed.response.AllFeedItem
import com.texthip.thip.data.model.feed.response.FeedMineInfoResponse
import com.texthip.thip.data.model.feed.response.MyFeedItem
import com.texthip.thip.data.model.users.response.RecentWriterList
import com.texthip.thip.data.repository.FeedRepository
import com.texthip.thip.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FeedUiState(
    val selectedTabIndex: Int = 0,
    val allFeeds: List<AllFeedItem> = emptyList(),
    val myFeeds: List<MyFeedItem> = emptyList(),
    val recentWriters: List<RecentWriterList> = emptyList(),
    val myFeedInfo: FeedMineInfoResponse? = null,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val isLastPageAllFeeds: Boolean = false,
    val isLastPageMyFeeds: Boolean = false,
    val error: String? = null
) {
    val canLoadMoreAllFeeds: Boolean get() = !isLoading && !isLoadingMore && !isRefreshing && !isLastPageAllFeeds
    val canLoadMoreMyFeeds: Boolean get() = !isLoading && !isLoadingMore && !isRefreshing && !isLastPageMyFeeds
    val currentTabFeeds: List<Any>
        get() = when (selectedTabIndex) {
            0 -> allFeeds
            1 -> myFeeds
            else -> emptyList()
        }
    val canLoadMoreCurrentTab: Boolean
        get() = when (selectedTabIndex) {
            0 -> canLoadMoreAllFeeds
            1 -> canLoadMoreMyFeeds
            else -> false
        }
}

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val feedRepository: FeedRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedUiState())
    val uiState = _uiState.asStateFlow()

    private var allFeedsNextCursor: String? = null
    private var myFeedsNextCursor: String? = null
    private var isLoadingAllFeeds = false
    private var isLoadingMyFeeds = false

    init {
        loadAllFeeds()
        fetchRecentWriters()
    }

    private fun updateState(update: (FeedUiState) -> FeedUiState) {
        _uiState.value = update(_uiState.value)
    }

    fun onTabSelected(index: Int) {
        updateState { it.copy(selectedTabIndex = index) }

        when (index) {
            0 -> {
                loadAllFeeds(isInitial = true)
            }

            1 -> {
                loadMyFeeds(isInitial = true)
                fetchMyFeedInfo()
            }
        }
    }

    private fun loadAllFeeds(isInitial: Boolean = true) {
        if (isLoadingAllFeeds && !isInitial) return
        if (_uiState.value.isLastPageAllFeeds && !isInitial) return

        viewModelScope.launch {
            try {
                isLoadingAllFeeds = true

                if (isInitial) {
                    updateState {
                        it.copy(
                            isLoading = true,
                            allFeeds = emptyList(),
                            isLastPageAllFeeds = false
                        )
                    }
                    allFeedsNextCursor = null
                } else {
                    updateState { it.copy(isLoadingMore = true) }
                }

                val cursor = if (isInitial) null else allFeedsNextCursor

                feedRepository.getAllFeeds(cursor).onSuccess { response ->
                    if (response != null) {
                        val currentList = if (isInitial) emptyList() else _uiState.value.allFeeds
                        updateState {
                            it.copy(
                                allFeeds = currentList + response.feedList,
                                error = null,
                                isLastPageAllFeeds = response.isLast
                            )
                        }
                        allFeedsNextCursor = response.nextCursor
                    } else {
                        updateState { it.copy(isLastPageAllFeeds = true) }
                    }
                }.onFailure { exception ->
                    updateState { it.copy(error = exception.message) }
                }
            } finally {
                isLoadingAllFeeds = false
                updateState { it.copy(isLoading = false, isLoadingMore = false) }
            }
        }
    }

    private fun loadMyFeeds(isInitial: Boolean = true) {
        if (isLoadingMyFeeds && !isInitial) return
        if (_uiState.value.isLastPageMyFeeds && !isInitial) return

        viewModelScope.launch {
            try {
                isLoadingMyFeeds = true

                if (isInitial) {
                    updateState {
                        it.copy(
                            isLoading = true,
                            myFeeds = emptyList(),
                            isLastPageMyFeeds = false
                        )
                    }
                    myFeedsNextCursor = null
                } else {
                    updateState { it.copy(isLoadingMore = true) }
                }

                val cursor = if (isInitial) null else myFeedsNextCursor

                feedRepository.getMyFeeds(cursor).onSuccess { response ->
                    if (response != null) {
                        val currentList = if (isInitial) emptyList() else _uiState.value.myFeeds
                        updateState {
                            it.copy(
                                myFeeds = currentList + response.feedList,
                                error = null,
                                isLastPageMyFeeds = response.isLast
                            )
                        }
                        myFeedsNextCursor = response.nextCursor
                    } else {
                        updateState { it.copy(isLastPageMyFeeds = true) }
                    }
                }.onFailure { exception ->
                    updateState { it.copy(error = exception.message) }
                }
            } finally {
                isLoadingMyFeeds = false
                updateState { it.copy(isLoading = false, isLoadingMore = false) }
            }
        }
    }

    fun refreshCurrentTab() {
        viewModelScope.launch {
            updateState { it.copy(isRefreshing = true) }

            when (_uiState.value.selectedTabIndex) {
                0 -> refreshAllFeeds()
                1 -> refreshMyFeeds()
            }
            updateState { it.copy(isRefreshing = false) }
        }
    }

    private suspend fun refreshAllFeeds() {
        allFeedsNextCursor = null

        feedRepository.getAllFeeds().onSuccess { response ->
            if (response != null) {
                allFeedsNextCursor = response.nextCursor
                updateState {
                    it.copy(
                        allFeeds = response.feedList,
                        isRefreshing = false,
                        isLastPageAllFeeds = response.isLast,
                        error = null
                    )
                }
            } else {
                updateState {
                    it.copy(
                        allFeeds = emptyList(),
                        isRefreshing = false,
                        isLastPageAllFeeds = true
                    )
                }
            }
        }.onFailure { exception ->
            updateState {
                it.copy(
                    isRefreshing = false,
                    error = exception.message
                )
            }
        }
    }

    private suspend fun refreshMyFeeds() {
        myFeedsNextCursor = null

        feedRepository.getMyFeeds().onSuccess { response ->
            if (response != null) {
                myFeedsNextCursor = response.nextCursor
                updateState {
                    it.copy(
                        myFeeds = response.feedList,
                        isRefreshing = false,
                        isLastPageMyFeeds = response.isLast,
                        error = null
                    )
                }
            } else {
                updateState {
                    it.copy(
                        myFeeds = emptyList(),
                        isRefreshing = false,
                        isLastPageMyFeeds = true
                    )
                }
            }
        }.onFailure { exception ->
            updateState {
                it.copy(
                    isRefreshing = false,
                    error = exception.message
                )
            }
        }
    }

    fun loadMoreFeeds() {
        if (!_uiState.value.canLoadMoreCurrentTab || _uiState.value.isRefreshing) return

        when (_uiState.value.selectedTabIndex) {
            0 -> loadAllFeeds(isInitial = false)
            1 -> loadMyFeeds(isInitial = false)
        }
    }

    fun refreshData() {
        fetchRecentWriters()
    }

    private fun fetchRecentWriters() {
        viewModelScope.launch {
            updateState { it.copy(isLoading = true) }
            userRepository.getMyFollowingsRecentFeeds()
                .onSuccess { data ->
                    val writers = data?.myFollowingUsers ?: emptyList()
                    updateState {
                        it.copy(
                            isLoading = false,
                            recentWriters = writers
                        )
                    }
                }
                .onFailure { exception ->
                    updateState {
                        it.copy(
                            isLoading = false,
                            error = exception.message
                        )
                    }
                }
        }
    }

    private fun fetchMyFeedInfo() {
        viewModelScope.launch {
            feedRepository.getMyFeedInfo()
                .onSuccess { data ->
                    updateState {
                        it.copy(myFeedInfo = data)
                    }
                }
                .onFailure { exception ->
                    updateState {
                        it.copy(error = exception.message)
                    }
                }
        }
    }
}