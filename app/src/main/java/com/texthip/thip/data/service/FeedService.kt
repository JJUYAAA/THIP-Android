package com.texthip.thip.data.service

import com.texthip.thip.data.model.base.BaseResponse
import com.texthip.thip.data.model.feed.response.CreateFeedResponse
import com.texthip.thip.data.model.feed.response.FeedWriteInfoResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface FeedService {

    /** 피드 작성에 필요한 카테고리 및 태그 목록 조회 */
    @GET("feeds/write-info")
    suspend fun getFeedWriteInfo(): BaseResponse<FeedWriteInfoResponse>

    /** 피드 생성 */
    @Multipart
    @POST("feeds")
    suspend fun createFeed(
        @Part("request") request: RequestBody,
        @Part images: List<MultipartBody.Part>?
    ): BaseResponse<CreateFeedResponse>
}