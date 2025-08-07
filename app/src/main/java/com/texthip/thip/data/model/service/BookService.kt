package com.texthip.thip.data.model.service

import com.texthip.thip.data.model.base.BaseResponse
import com.texthip.thip.data.model.book.response.BookListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {

    @GET("books")
    suspend fun getBooks(
        @Query("type") type: String
    ): BaseResponse<BookListResponse>
}