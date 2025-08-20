package com.texthip.thip.ui.feed.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.model.feed.response.FeedDetailResponse
import com.texthip.thip.data.repository.FeedRepository
import com.texthip.thip.ui.feed.usecase.ChangeFeedLikeUseCase
import com.texthip.thip.ui.feed.usecase.ChangeFeedSaveUseCase
import com.texthip.thip.ui.feed.usecase.DeleteFeedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FeedDetailUiState(
    val isLoading: Boolean = false,
    val feedDetail: FeedDetailResponse? = null,
    val error: String? = null,
    val isDeleting: Boolean = false,
    val deleteSuccess: Boolean = false
)

@HiltViewModel
class FeedDetailViewModel @Inject constructor(
    private val feedRepository: FeedRepository,
    private val deleteFeedUseCase: DeleteFeedUseCase,
    private val changeFeedLikeUseCase: ChangeFeedLikeUseCase,
    private val changeFeedSaveUseCase: ChangeFeedSaveUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedDetailUiState())
    val uiState: StateFlow<FeedDetailUiState> = _uiState.asStateFlow()

    init {
        observeFeedUpdates()
    }

    private fun updateState(update: (FeedDetailUiState) -> FeedDetailUiState) {
        _uiState.value = update(_uiState.value)
    }
    private fun observeFeedUpdates() {
        viewModelScope.launch {
            feedRepository.feedStateUpdateResult.collect { update ->
                val currentFeed = _uiState.value.feedDetail
                if (currentFeed != null && currentFeed.feedId.toLong() == update.feedId) {
                    updateState {
                        it.copy(
                            feedDetail = currentFeed.copy(
                                isLiked = update.isLiked,
                                likeCount = update.likeCount,
                                isSaved = update.isSaved
                            )
                        )
                    }
                }
            }
        }
    }

    fun loadFeedDetail(feedId: Long) {
        viewModelScope.launch {
            updateState { it.copy(isLoading = true, error = null) }

            feedRepository.getFeedDetail(feedId)
                .onSuccess { response ->
                    updateState {
                        it.copy(
                            isLoading = false,
                            feedDetail = response,
                            error = null
                        )
                    }
                }
                .onFailure { exception ->
                    updateState {
                        it.copy(
                            isLoading = false,
                            feedDetail = null,
                            error = exception.message ?: "알 수 없는 오류가 발생했습니다."
                        )
                    }
                }
        }
    }
    fun deleteFeed(feedId: Long) {
        viewModelScope.launch {
            updateState { it.copy(isDeleting = true, error = null) }

            deleteFeedUseCase(feedId) // UseCase 호출
                .onSuccess {
                    updateState { it.copy(isDeleting = false, deleteSuccess = true) }
                }
                .onFailure { exception ->
                    updateState {
                        it.copy(
                            isDeleting = false,
                            error = exception.message ?: "피드 삭제 중 오류가 발생했습니다."
                        )
                    }
                }
        }
    }
    fun changeFeedLike() {
        viewModelScope.launch {
            val originalFeed = _uiState.value.feedDetail ?: return@launch

            val updatedFeed = originalFeed.copy(
                isLiked = !originalFeed.isLiked,
                likeCount = if (originalFeed.isLiked) originalFeed.likeCount - 1 else originalFeed.likeCount + 1
            )
            updateState { it.copy(feedDetail = updatedFeed) }

            val newLikeStatus = !originalFeed.isLiked
            changeFeedLikeUseCase(
                feedId = originalFeed.feedId.toLong(),
                newLikeStatus = newLikeStatus,
                currentLikeCount = originalFeed.likeCount,
                currentIsSaved = originalFeed.isSaved
            ).onFailure {
                updateState { it.copy(feedDetail = originalFeed) }
            }
        }
    }

    fun changeFeedSave() {
        viewModelScope.launch {
            val originalFeed = _uiState.value.feedDetail ?: return@launch

            val updatedFeed = originalFeed.copy(
                isSaved = !originalFeed.isSaved
            )
            updateState { it.copy(feedDetail = updatedFeed) }

            val newSaveStatus = !originalFeed.isSaved
            changeFeedSaveUseCase(
                feedId = originalFeed.feedId.toLong(),
                newSaveStatus = newSaveStatus,
                currentIsLiked = originalFeed.isLiked,
                currentLikeCount = originalFeed.likeCount
            ).onFailure {
                updateState { it.copy(feedDetail = originalFeed) }
            }
        }
    }

    fun clearError() {
        updateState { it.copy(error = null) }
    }
}