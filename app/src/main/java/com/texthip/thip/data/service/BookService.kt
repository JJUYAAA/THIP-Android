package com.texthip.thip.data.service

import com.texthip.thip.data.model.base.BaseResponse
import com.texthip.thip.data.model.book.request.BookSaveRequest
import com.texthip.thip.data.model.book.response.BookDetailResponse
import com.texthip.thip.data.model.book.response.BookListResponse
import com.texthip.thip.data.model.book.response.BookSaveResponse
import com.texthip.thip.data.model.book.response.BookSearchResponse
import com.texthip.thip.data.model.book.response.MostSearchedBooksResponse
import com.texthip.thip.data.model.book.response.RecruitingRoomsResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface BookService {

    /** 저장된 책 또는 모임 책 목록 조회 */
    @GET("books/selectable-list")
    suspend fun getBooks(
        @Query("type") type: String
    ): BaseResponse<BookListResponse>

    /** 책 검색 */
    @GET("books")
    suspend fun searchBooks(
        @Query("keyword") keyword: String,
        @Query("page") page: Int = 1,
        @Query("isFinalized") isFinalized: Boolean = false
    ): BaseResponse<BookSearchResponse>

    /** 인기 책 조회 */
    @GET("books/most-searched")
    suspend fun getMostSearchedBooks(): BaseResponse<MostSearchedBooksResponse>

    /** 책 상세 조회 */
    @GET("books/{isbn}")
    suspend fun getBookDetail(
        @Path("isbn") isbn: String
    ): BaseResponse<BookDetailResponse>

    /** 책 저장/저장취소 */
    @POST("books/{isbn}/saved")
    suspend fun saveBook(
        @Path("isbn") isbn: String,
        @Body request: BookSaveRequest
    ): BaseResponse<BookSaveResponse>

    /** 모집중인 방 조회 */
    @GET("books/{isbn}/recruiting-rooms")
    suspend fun getRecruitingRooms(
        @Path("isbn") isbn: String,
        @Query("cursor") cursor: String? = null
    ): BaseResponse<RecruitingRoomsResponse>
}