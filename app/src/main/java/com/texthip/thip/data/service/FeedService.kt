package com.texthip.thip.data.service

import com.texthip.thip.data.model.base.BaseResponse
import com.texthip.thip.data.model.feed.response.CreateFeedResponse
import com.texthip.thip.data.model.feed.response.FeedDetailResponse
import com.texthip.thip.data.model.feed.response.FeedUsersInfoResponse
import com.texthip.thip.data.model.feed.response.FeedUsersResponse
import com.texthip.thip.data.model.feed.response.FeedWriteInfoResponse
import com.texthip.thip.data.model.feed.response.FeedMineInfoResponse
import com.texthip.thip.data.model.feed.response.RelatedBooksResponse
import com.texthip.thip.data.model.feed.response.AllFeedResponse
import com.texthip.thip.data.model.feed.response.MyFeedResponse
import com.texthip.thip.data.model.feed.request.UpdateFeedRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query


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

    /** 전체 피드 목록 조회 */
    @GET("feeds")
    suspend fun getAllFeeds(
        @Query("cursor") cursor: String? = null
    ): BaseResponse<AllFeedResponse>

    /** 내 피드 목록 조회 */
    @GET("feeds/mine")
    suspend fun getMyFeeds(
        @Query("cursor") cursor: String? = null
    ): BaseResponse<MyFeedResponse>

    /** 내 피드 정보 조회 */
    @GET("feeds/mine/info")
    suspend fun getMyFeedInfo(): BaseResponse<FeedMineInfoResponse>

    /** 특정 책과 관련된 피드 목록 조회 */
    @GET("feeds/related-books/{isbn}")
    suspend fun getRelatedBookFeeds(
        @Path("isbn") isbn: String,
        @Query("sort") sort: String? = null,
        @Query("cursor") cursor: String? = null
    ): BaseResponse<RelatedBooksResponse>

    /** 피드 상세 조회 */
    @GET("feeds/{feedId}")
    suspend fun getFeedDetail(
        @Path("feedId") feedId: Int
    ): BaseResponse<FeedDetailResponse>

    @GET("feeds/users/{userId}/info")
    suspend fun getFeedUsersInfo(
        @Path("userId") userId: Long
    ): BaseResponse<FeedUsersInfoResponse>

    @GET("feeds/users/{userId}")
    suspend fun getFeedUsers(
        @Path("userId") userId: Long
    ): BaseResponse<FeedUsersResponse>

    /** 피드 수정 */
    @PATCH("feeds/{feedId}")
    suspend fun updateFeed(
        @Path("feedId") feedId: Int,
        @Body request: UpdateFeedRequest
    ): BaseResponse<CreateFeedResponse>
}