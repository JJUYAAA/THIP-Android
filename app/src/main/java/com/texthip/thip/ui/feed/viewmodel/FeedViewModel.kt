package com.texthip.thip.ui.feed.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.model.users.response.RecentWriterList
import com.texthip.thip.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FeedUiState(
    val isLoading: Boolean = true,
    val recentWriters: List<RecentWriterList> = emptyList(),
    val errorMessage: String? = null
    //TODO 추후 피드 목록 등 다른 상태들 추가될 예정
)

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchRecentWriters()
    }

    fun refreshData() {
        fetchRecentWriters()
    }

    private fun fetchRecentWriters() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            userRepository.getMyFollowingsRecentFeeds()
                .onSuccess { data ->
                    val writers = data?.recentWriters ?: emptyList()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            recentWriters = writers
                        )
                    }
                }
                .onFailure { exception ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = exception.message) }
                }
        }
    }
}