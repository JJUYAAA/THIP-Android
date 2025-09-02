package com.texthip.thip.data.service

import com.texthip.thip.data.model.base.BaseResponse
import com.texthip.thip.data.model.feed.request.CreateFeedRequest
import com.texthip.thip.data.model.feed.request.FeedLikeRequest
import com.texthip.thip.data.model.feed.request.FeedSaveRequest
import com.texthip.thip.data.model.feed.request.PresignedUrlRequest
import com.texthip.thip.data.model.feed.request.UpdateFeedRequest
import com.texthip.thip.data.model.feed.response.AllFeedResponse
import com.texthip.thip.data.model.feed.response.CreateFeedResponse
import com.texthip.thip.data.model.feed.response.FeedDetailResponse
import com.texthip.thip.data.model.feed.response.FeedLikeResponse
import com.texthip.thip.data.model.feed.response.FeedMineInfoResponse
import com.texthip.thip.data.model.feed.response.FeedSaveResponse
import com.texthip.thip.data.model.feed.response.FeedUsersInfoResponse
import com.texthip.thip.data.model.feed.response.FeedUsersResponse
import com.texthip.thip.data.model.feed.response.FeedWriteInfoResponse
import com.texthip.thip.data.model.feed.response.MyFeedResponse
import com.texthip.thip.data.model.feed.response.PresignedUrlResponse
import com.texthip.thip.data.model.feed.response.RelatedBooksResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface FeedService {

    /** 피드 작성에 필요한 카테고리 및 태그 목록 조회 */
    @GET("feeds/write-info")
    suspend fun getFeedWriteInfo(): BaseResponse<FeedWriteInfoResponse>

    /** 피드 이미지 업로드용 presigned URL 발급 */
    @POST("feeds/images/presigned-url")
    suspend fun getPresignedUrls(
        @Body request: PresignedUrlRequest
    ): BaseResponse<PresignedUrlResponse>

    /** 피드 생성 */
    @POST("feeds")
    suspend fun createFeed(
        @Body request: CreateFeedRequest
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
        @Path("feedId") feedId: Long
    ): BaseResponse<FeedDetailResponse>

    @GET("feeds/users/{userId}/info")
    suspend fun getFeedUsersInfo(
        @Path("userId") userId: Long
    ): BaseResponse<FeedUsersInfoResponse>

    @GET("feeds/users/{userId}")
    suspend fun getFeedUsers(
        @Path("userId") userId: Long
    ): BaseResponse<FeedUsersResponse>

    /** 피드 삭제 */
    @DELETE("feeds/{feedId}")
    suspend fun deleteFeed(
        @Path("feedId") feedId: Long
    ): BaseResponse<String>

    /** 피드 좋아요 상태 변경 */
    @POST("feeds/{feedId}/likes")
    suspend fun changeFeedLike(
        @Path("feedId") feedId: Long,
        @Body request: FeedLikeRequest
    ): BaseResponse<FeedLikeResponse>

    /** 피드 수정 */
    @PATCH("feeds/{feedId}")
    suspend fun updateFeed(
        @Path("feedId") feedId: Long,
        @Body request: UpdateFeedRequest
    ): BaseResponse<CreateFeedResponse>

    /** 피드 저장 상태 변경 */
    @POST("feeds/{feedId}/saved")
    suspend fun changeFeedSave(
        @Path("feedId") feedId: Long,
        @Body request: FeedSaveRequest
    ): BaseResponse<FeedSaveResponse>

    @GET("feeds/saved")
    suspend fun getSavedFeeds(
        @Query("cursor") cursor: String? = null
    ): BaseResponse<AllFeedResponse>
}