package com.texthip.thip.ui.feed.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.model.users.response.FollowingList
import com.texthip.thip.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MySubscriptionUiState(
    val isLoading: Boolean = false,
    val followings: List<FollowingList> = emptyList(),
    val totalCount: Int = 0,
    val isLastPage: Boolean = false,
    val errorMessage: String? = null,
    val showToast: Boolean = false,
    val toastMessage: String = ""
)

@HiltViewModel
class MySubscriptionViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MySubscriptionUiState())
    val uiState = _uiState.asStateFlow()

    private var nextCursor: String? = null

    init {
        fetchMyFollowings(isInitial = true)
    }

    fun fetchMyFollowings(isInitial: Boolean = false) {
        if (_uiState.value.isLoading || (!isInitial && _uiState.value.isLastPage)) {
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val cursorToFetch = if (isInitial) null else nextCursor

            val result = userRepository.getMyFollowings(cursor = cursorToFetch)

            result.onSuccess { data ->
                data?.let {
                    val newFollowings = it.followings
                    nextCursor = it.nextCursor

                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            followings = if (isInitial) newFollowings else currentState.followings + newFollowings,
                            totalCount = it.totalFollowingCount,
                            isLastPage = it.isLast
                        )
                    }
                } ?: _uiState.update {
                    it.copy(isLoading = false, errorMessage = "응답 데이터가 비어있습니다.")
                }
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = exception.message)
                }
            }
        }
    }


    fun toggleFollow(userId: Long, followedMessage: String, unfollowedMessage: String) {
        val currentState = _uiState.value
        val userToUpdate = currentState.followings.find { it.userId == userId } ?: return
        val currentIsFollowing = userToUpdate.isFollowing
        //낙관적 업데이트 -> ui 먼저 변경
        val newOptimisticList = currentState.followings.map { user ->
            if (user.userId == userId) {
                user.copy(isFollowing = !currentIsFollowing)
            } else {
                user
            }
        }

        _uiState.update {
            it.copy(
                followings = newOptimisticList,
                showToast = true,
                toastMessage = if (!currentIsFollowing) followedMessage else unfollowedMessage
            )
        }
        viewModelScope.launch {
            val requestType = !currentIsFollowing

            userRepository.toggleFollow(followingUserId = userId, isFollowing = requestType)
                .onSuccess { response ->
                    val serverState = response?.isFollowing ?: requestType
                    _uiState.update { state ->
                        state.copy(followings = state.followings.map { user ->
                            if (user.userId == userId) {
                                user.copy(isFollowing = serverState)
                            } else {
                                user
                            }
                        })
                    }
                }
                .onFailure {
                    _uiState.update { state ->
                        state.copy(followings = state.followings.map { user ->
                            if (user.userId == userId) {
                                user.copy(isFollowing = currentIsFollowing) // 원래 상태로 복원
                            } else {
                                user
                            }
                        })
                    }
                }
        }
    }
    fun hideToast() {
        _uiState.update { it.copy(showToast = false) }
    }
}