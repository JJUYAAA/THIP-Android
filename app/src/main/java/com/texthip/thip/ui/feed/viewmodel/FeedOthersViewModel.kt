package com.texthip.thip.ui.feed.viewmodel

import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.model.feed.response.FeedList
import com.texthip.thip.data.model.feed.response.FeedUsersInfoResponse
import com.texthip.thip.data.repository.FeedRepository
import com.texthip.thip.data.repository.UserRepository
import com.texthip.thip.ui.feed.usecase.ChangeFeedLikeUseCase
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
    val errorMessage: String? = null,
    val showToast: Boolean = false,
    val toastMessage: String = ""
)

@HiltViewModel
class FeedOthersViewModel @Inject constructor(
    private val feedRepository: FeedRepository,
    private val changeFeedLikeUseCase: ChangeFeedLikeUseCase,
    private val userRepository: UserRepository,
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

    fun changeFeedLike(feedId: Long) {
        viewModelScope.launch {
            val currentFeeds = _uiState.value.feeds
            val feedToUpdate = currentFeeds.find { it.feedId == feedId } ?: return@launch

            //ui 먼저 변경 ( 낙관적 업데이트 )
            val newFeeds = currentFeeds.map {
                if (it.feedId == feedId) {
                    it.copy(
                        isLiked = !it.isLiked,
                        likeCount = if (it.isLiked) it.likeCount - 1 else it.likeCount + 1
                    )
                } else {
                    it
                }
            }
            _uiState.update { it.copy(feeds = newFeeds) }

            //api 호출
            val newLikeStatus = !feedToUpdate.isLiked
            changeFeedLikeUseCase(feedId, newLikeStatus)
                .onFailure {
                    _uiState.update { it.copy(feeds = currentFeeds) }
                }
        }
    }
    fun toggleFollow(followedMessage: String, unfollowedMessage: String) {
        val currentUserInfo = _uiState.value.userInfo ?: return
        val currentIsFollowing = currentUserInfo.isFollowing

        //UI 즉시 변경 (낙관적 업데이트)
        val optimisticUserInfo = currentUserInfo.copy(isFollowing = !currentIsFollowing)
        _uiState.update {
            it.copy(
                userInfo = optimisticUserInfo,
                showToast = true,
                toastMessage = if (!currentIsFollowing) followedMessage else unfollowedMessage
            )
        }

        //API 요청
        viewModelScope.launch {
            userRepository.toggleFollow(followingUserId = userId, isFollowing = !currentIsFollowing)
                .onFailure {
                    _uiState.update { it.copy(userInfo = currentUserInfo) }
                }
        }
    }

    fun hideToast() {
        _uiState.update { it.copy(showToast = false) }
    }
}