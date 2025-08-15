package com.texthip.thip.data.service

import com.texthip.thip.data.model.base.BaseResponse
import com.texthip.thip.data.model.feed.response.FeedWriteInfoResponse
import retrofit2.http.GET

interface FeedService {

    /** 피드 작성에 필요한 카테고리 및 태그 목록 조회 */
    @GET("feeds/write-info")
    suspend fun getFeedWriteInfo(): BaseResponse<FeedWriteInfoResponse>
}