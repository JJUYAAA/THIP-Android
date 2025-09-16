package com.texthip.thip.data.service

import com.texthip.thip.data.model.base.BaseResponse
import com.texthip.thip.data.model.comments.request.CommentsCreateRequest
import com.texthip.thip.data.model.comments.request.CommentsLikesRequest
import com.texthip.thip.data.model.comments.response.CommentsCreateResponse
import com.texthip.thip.data.model.comments.response.CommentsDeleteResponse
import com.texthip.thip.data.model.comments.response.CommentsLikesResponse
import com.texthip.thip.data.model.comments.response.CommentsResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CommentsService {
    @GET("comments/{postId}")
    suspend fun getComments(
        @Path("postId") postId: Long,
        @Query("postType") postType: String = "RECORD",
        @Query("cursor") cursor: String? = null,
    ): BaseResponse<CommentsResponse>

    @POST("comments/{commentId}/likes")
    suspend fun likeComment(
        @Path("commentId") commentId: Long,
        @Body response: CommentsLikesRequest
    ): BaseResponse<CommentsLikesResponse>

    @POST("comments/{postId}")
    suspend fun createComment(
        @Path("postId") postId: Long,
        @Body request: CommentsCreateRequest
    ): BaseResponse<CommentsCreateResponse>

    @DELETE("comments/{commentId}")
    suspend fun deleteComment(
        @Path("commentId") commentId: Long
    ): BaseResponse<CommentsDeleteResponse>
}