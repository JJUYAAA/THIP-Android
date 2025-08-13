package com.texthip.thip.ui.group.note.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.model.comments.response.CommentList
import com.texthip.thip.data.repository.CommentsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CommentsUiState(
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val isLast: Boolean = false,
    val comments: List<CommentList> = emptyList()
)

sealed interface CommentsEvent {
    data object LoadMoreComments : CommentsEvent
}

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val commentsRepository: CommentsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CommentsUiState())
    val uiState = _uiState.asStateFlow()

    private var nextCursor: String? = null
    private var currentPostId: Long = -1L

    fun initialize(postId: Long) {
        if (currentPostId == postId) return
        this.currentPostId = postId
        fetchComments(isRefresh = true)
    }

    fun onEvent(event: CommentsEvent) {
        when (event) {
            is CommentsEvent.LoadMoreComments -> fetchComments(isRefresh = false)
        }
    }

    private fun fetchComments(isRefresh: Boolean) {
        val currentState = _uiState.value
        if (currentState.isLoading || currentState.isLoadingMore || (currentState.isLast && !isRefresh)) {
            return
        }

        viewModelScope.launch {
            _uiState.update {
                if (isRefresh) it.copy(isLoading = true, comments = emptyList(), isLast = false)
                else it.copy(isLoadingMore = true)
            }

            val cursorToFetch = if (isRefresh) null else nextCursor

            commentsRepository.getComments(postId = currentPostId, cursor = cursorToFetch)
                .onSuccess { response ->
                    if (response != null) {
                        nextCursor = response.nextCursor
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isLoadingMore = false,
                                // isRefresh일 경우 새 목록으로, 아닐 경우 기존 목록에 추가
                                comments = if (isRefresh) response.commentList else it.comments + response.commentList,
                                isLast = response.isLast
                            )
                        }
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(isLoading = false, isLoadingMore = false, error = throwable.message)
                    }
                }
        }
    }
}
