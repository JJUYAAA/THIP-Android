package com.texthip.thip.ui.feed.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.model.users.response.FollowerList
import com.texthip.thip.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OthersSubscriptionUiState(
    val isLoading: Boolean = false,
    val followers: List<FollowerList> = emptyList(),
    val totalCount: Int = 0,
    val isLastPage: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class OthersSubscriptionViewModel @Inject constructor(
    private val userRepository: UserRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val userId: Long = requireNotNull(savedStateHandle["userId"])
    private val _uiState = MutableStateFlow(OthersSubscriptionUiState())
    val uiState = _uiState.asStateFlow()

    private var nextCursor: String? = null

    init {
        fetchOthersFollowers(isInitial = true)
    }

    fun fetchOthersFollowers(isInitial: Boolean = false) {
        if (_uiState.value.isLoading || (!isInitial && _uiState.value.isLastPage)) {
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val cursorToFetch = if (isInitial) null else nextCursor

            val result = userRepository.getOthersFollowers(userId = userId, cursor = cursorToFetch)

            result.onSuccess { data ->
                data?.let {
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            followers = if (isInitial) it.followers else currentState.followers + currentState.followers,
                            totalCount = it.totalFollowerCount,
                            isLastPage = it.isLast
                        )
                    }
                    nextCursor = it.nextCursor
                }
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = exception.message)
                }
            }
        }
    }
}