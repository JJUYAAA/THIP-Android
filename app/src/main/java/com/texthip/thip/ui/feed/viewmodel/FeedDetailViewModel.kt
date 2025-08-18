package com.texthip.thip.ui.feed.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.model.feed.response.FeedDetailResponse
import com.texthip.thip.data.repository.FeedRepository
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
    private val deleteFeedUseCase: DeleteFeedUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedDetailUiState())
    val uiState: StateFlow<FeedDetailUiState> = _uiState.asStateFlow()

    private fun updateState(update: (FeedDetailUiState) -> FeedDetailUiState) {
        _uiState.value = update(_uiState.value)
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

    fun clearError() {
        updateState { it.copy(error = null) }
    }
}