package com.texthip.thip.data.service

import com.texthip.thip.data.model.base.BaseResponse
import com.texthip.thip.data.model.comments.response.CommentsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CommentsService {
    @GET("comments/{postId}")
    suspend fun getComments(
        @Path("postId") postId: Long,
        @Query("postType") postType: String = "RECORD",
        @Query("cursor") cursor: String? = null,
    ): BaseResponse<CommentsResponse>
}