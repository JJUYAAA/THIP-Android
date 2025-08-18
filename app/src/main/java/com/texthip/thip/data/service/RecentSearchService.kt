package com.texthip.thip.data.service

import com.texthip.thip.data.model.base.BaseResponse
import com.texthip.thip.data.model.book.response.RecentSearchResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecentSearchService {

    /** 최근 검색어 조회 */
    @GET("recent-searches")
    suspend fun getRecentSearches(
        @Query("type") type: String
    ): BaseResponse<RecentSearchResponse>

    /** 최근 검색어 삭제 */
    @DELETE("recent-searches/{recentSearchId}")
    suspend fun deleteRecentSearch(
        @Path("recentSearchId") recentSearchId: Int
    ): BaseResponse<Unit>
}