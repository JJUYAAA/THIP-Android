package com.texthip.thip.data.repository

import com.texthip.thip.data.model.base.handleBaseResponse
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
}