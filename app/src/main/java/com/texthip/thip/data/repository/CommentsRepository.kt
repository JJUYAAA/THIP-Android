package com.texthip.thip.data.repository

import com.texthip.thip.data.model.base.handleBaseResponse
import com.texthip.thip.data.model.comments.request.CommentsCreateRequest
import com.texthip.thip.data.model.comments.request.CommentsLikesRequest
import com.texthip.thip.data.service.CommentsService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentsRepository @Inject constructor(
    private val commentsService: CommentsService,
) {
    suspend fun getComments(
        postId: Long,
        postType: String = "RECORD",
        cursor: String? = null,
    ) = runCatching {
        commentsService.getComments(
            postId = postId,
            postType = postType,
            cursor = cursor
        ).handleBaseResponse().getOrThrow()
    }

    suspend fun likeComment(
        commentId: Long,
        type: Boolean
    ) = runCatching {
        commentsService.likeComment(
            commentId = commentId,
            response = CommentsLikesRequest(type)
        ).handleBaseResponse().getOrThrow()
    }

    suspend fun createComment(
        postId: Long,
        content: String,
        isReplyRequest: Boolean,
        parentId: Int? = null,
        postType: String = "RECORD",
    ) = runCatching {
        commentsService.createComment(
            postId = postId,
            request = CommentsCreateRequest(
                content = content,
                isReplyRequest = isReplyRequest,
                parentId = parentId,
                postType = postType
            )
        ).handleBaseResponse().getOrThrow()
    }
}