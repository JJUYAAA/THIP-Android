package com.texthip.thip.ui.feed.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.model.feed.response.FeedDetailResponse
import com.texthip.thip.data.repository.FeedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FeedDetailUiState(
    val isLoading: Boolean = false,
    val feedDetail: FeedDetailResponse? = null,
    val error: String? = null
)

@HiltViewModel
class FeedDetailViewModel @Inject constructor(
    private val feedRepository: FeedRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedDetailUiState())
    val uiState: StateFlow<FeedDetailUiState> = _uiState.asStateFlow()

    private fun updateState(update: (FeedDetailUiState) -> FeedDetailUiState) {
        _uiState.value = update(_uiState.value)
    }

    fun loadFeedDetail(feedId: Int) {
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

    fun clearError() {
        updateState { it.copy(error = null) }
    }
}