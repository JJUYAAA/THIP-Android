package com.texthip.thip.data.service

import com.texthip.thip.data.model.base.BaseResponse
import com.texthip.thip.data.model.book.response.BookListResponse
import com.texthip.thip.data.model.book.response.BookSearchResponse
import com.texthip.thip.data.model.book.response.MostSearchedBooksResponse
import com.texthip.thip.data.model.book.response.RecentSearchResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BookService {

    /** 저장된 책 또는 모임 책 목록 조회 */
    @GET("books")
    suspend fun getBooks(
        @Query("type") type: String
    ): BaseResponse<BookListResponse>

    /** 책 검색 */
    @GET("books")
    suspend fun searchBooks(
        @Query("keyword") keyword: String,
        @Query("page") page: Int = 1
    ): BaseResponse<BookSearchResponse>

    /** 인기 책 조회 */
    @GET("books/most-searched")
    suspend fun getMostSearchedBooks(): BaseResponse<MostSearchedBooksResponse>

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