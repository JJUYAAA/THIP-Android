package com.texthip.thip.ui.group.note.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.model.comments.response.CommentList
import com.texthip.thip.data.model.comments.response.ReplyList
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
    data class LikeComment(val commentId: Int) : CommentsEvent // 댓글 좋아요 이벤트
    data class LikeReply(val replyId: Int) : CommentsEvent // 답글 좋아요 이벤트
    data class CreateComment(val content: String, val parentId: Int?) : CommentsEvent
    data class DeleteComment(val commentId: Int) : CommentsEvent
}

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val commentsRepository: CommentsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CommentsUiState())
    val uiState = _uiState.asStateFlow()

    private var nextCursor: String? = null
    private var currentPostId: Long = -1L
    private var currentPostType: String = "RECORD"

    fun initialize(postId: Long, postType: String) {
        if (currentPostId == postId) return
        this.currentPostId = postId
        this.currentPostType = postType
        fetchComments(isRefresh = true)
    }

    fun onEvent(event: CommentsEvent) {
        when (event) {
            is CommentsEvent.LoadMoreComments -> fetchComments(isRefresh = false)
            is CommentsEvent.LikeComment -> toggleCommentLike(event.commentId)
            is CommentsEvent.LikeReply -> toggleReplyLike(event.replyId)
            is CommentsEvent.CreateComment -> createComment(
                content = event.content,
                parentId = event.parentId
            )

            is CommentsEvent.DeleteComment -> deleteComment(event.commentId)
        }
    }

    private fun deleteComment(commentId: Int) {
        val originalComments = _uiState.value.comments

        val newComments = originalComments.map { comment ->
            if (comment.commentId == commentId) {
                comment.copy(isDeleted = true)
            } else {
                comment.copy(
                    replyList = comment.replyList.map { reply ->
                        if (reply.commentId == commentId) {
                            reply
                        } else {
                            reply
                        }
                    }.filterNot { reply ->
                        reply.commentId == commentId
                    }
                )
            }
        }
        _uiState.update { it.copy(comments = newComments) }

        viewModelScope.launch {
            commentsRepository.deleteComment(commentId.toLong())
                .onSuccess { response ->
                    // 성공 시 별도 처리 필요 없음
                }
                .onFailure {
                    _uiState.update { it.copy(comments = originalComments, error = "삭제 실패") }
                }
        }
    }

    private fun createComment(content: String, parentId: Int?) {
        if (content.isBlank()) return

        viewModelScope.launch {
            val isReply = parentId != null

            commentsRepository.createComment(
                postId = currentPostId,
                content = content,
                isReplyRequest = isReply,
                parentId = parentId,
                postType = currentPostType
            ).onSuccess {
                fetchComments(isRefresh = true)
            }.onFailure { throwable ->
                _uiState.update { it.copy(error = "댓글 작성 실패: ${throwable.message}") }
            }
        }
    }

    private fun toggleCommentLike(commentId: Int) {
        // 클릭한 댓글 찾기
        val comments = _uiState.value.comments
        val commentIndex = comments.indexOfFirst { it.commentId == commentId }
        if (commentIndex == -1) return

        val comment = comments[commentIndex]
        val currentIsLiked = comment.isLike
        val newLikeCount = if (currentIsLiked) comment.likeCount - 1 else comment.likeCount + 1

        // 즉시 UI 업데이트
        val updatedComment = comment.copy(isLike = !currentIsLiked, likeCount = newLikeCount)
        val newComments = comments.toMutableList().apply { set(commentIndex, updatedComment) }
        _uiState.update { it.copy(comments = newComments) }

        viewModelScope.launch {
            commentsRepository.likeComment(commentId.toLong(), !currentIsLiked)
                .onFailure {
                    _uiState.update {
                        val originalComments = it.comments.toMutableList()
                        originalComments[commentIndex] = comment
                        it.copy(comments = originalComments)
                    }
                }
        }
    }

    private fun toggleReplyLike(replyId: Int) {
        val comments = _uiState.value.comments
        var parentComment: CommentList? = null
        var reply: ReplyList? = null
        var parentCommentIndex = -1
        var replyIndex = -1

        // 전체 댓글 목록을 돌면서 좋아요 누른 답글과 그 부모를 찾기
        for ((pIndex, pComment) in comments.withIndex()) {
            val rIndex = pComment.replyList.indexOfFirst { it.commentId == replyId }
            if (rIndex != -1) {
                parentComment = pComment
                reply = pComment.replyList[rIndex]
                parentCommentIndex = pIndex
                replyIndex = rIndex
                break // 찾았으면 루프 종료
            }
        }

        // 답글이나 부모를 못 찾으면 함수 종료
        if (parentComment == null || reply == null || parentCommentIndex == -1 || replyIndex == -1) return

        val currentIsLiked = reply.isLike
        val newLikeCount = if (currentIsLiked) reply.likeCount - 1 else reply.likeCount + 1

        // 즉시 UI 업데이트
        val updatedReply = reply.copy(isLike = !currentIsLiked, likeCount = newLikeCount)
        val newReplyList = parentComment.replyList.toMutableList().apply { set(replyIndex, updatedReply) }
        val updatedParentComment = parentComment.copy(replyList = newReplyList)
        val newComments = comments.toMutableList().apply { set(parentCommentIndex, updatedParentComment) }
        _uiState.update { it.copy(comments = newComments) }

        viewModelScope.launch {
            commentsRepository.likeComment(replyId.toLong(), !currentIsLiked)
                .onFailure {
                    _uiState.update {
                        // 실패 시 롤백 로직 (기존과 동일)
                        val originalComments = it.comments.toMutableList()
                        originalComments[parentCommentIndex] = parentComment
                        it.copy(comments = originalComments)
                    }
                }
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
