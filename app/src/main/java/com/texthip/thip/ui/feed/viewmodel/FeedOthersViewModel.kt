package com.texthip.thip.ui.feed.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.model.feed.response.FeedUsersInfoResponse
import com.texthip.thip.data.repository.FeedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FeedOthersUiState(
    val isLoading: Boolean = true,
    val userInfo: FeedUsersInfoResponse? = null,
    // TODO: 유저 피드 목록 불러오기
    // val feeds: List<FeedItem> = emptyList(),
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
        fetchUserInfo()
    }

    private fun fetchUserInfo() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            feedRepository.getFeedUsersInfo(userId)
                .onSuccess { data ->
                    _uiState.update {
                        it.copy(isLoading = false, userInfo = data)
                    }
                }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = exception.message)
                    }
                }
        }
    }
}