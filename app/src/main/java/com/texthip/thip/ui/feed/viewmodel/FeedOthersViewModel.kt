package com.texthip.thip.ui.feed.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.model.feed.response.FeedList
import com.texthip.thip.data.model.feed.response.FeedUsersInfoResponse
import com.texthip.thip.data.repository.FeedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FeedOthersUiState(
    val isLoading: Boolean = true,
    val userInfo: FeedUsersInfoResponse? = null,
    val feeds: List<FeedList> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class FeedOthersViewModel @Inject constructor(
    private val feedRepository: FeedRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val userId: Long = requireNotNull(savedStateHandle["userId"])

    private val _uiState = MutableStateFlow(FeedOthersUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val userInfoDeferred = async { feedRepository.getFeedUsersInfo(userId) }
            val feedsDeferred = async { feedRepository.getFeedUsers(userId) }

            val userInfoResult = userInfoDeferred.await()
            val feedsResult = feedsDeferred.await()

            val fetchedFeeds = feedsResult.getOrNull()?.feedList ?: emptyList()

            // ✅ 로그를 추가하여 유저 정보와 피드 개수를 확인
            Log.d("FeedOthersViewModel", "User Info Result: ${userInfoResult.getOrNull()}")
            Log.d("FeedOthersViewModel", "Fetched Feeds Count: ${fetchedFeeds.size}")

            _uiState.update {
                it.copy(
                    isLoading = false,
                    userInfo = userInfoResult.getOrNull(),
                    feeds = fetchedFeeds,
                    errorMessage = userInfoResult.exceptionOrNull()?.message
                        ?: feedsResult.exceptionOrNull()?.message
                )
            }
        }
    }
}