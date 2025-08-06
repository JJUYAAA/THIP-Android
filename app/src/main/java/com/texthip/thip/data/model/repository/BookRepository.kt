package com.texthip.thip.data.model.repository

import com.texthip.thip.data.model.base.handleBaseResponse
import com.texthip.thip.data.model.book.response.BookSavedResponse
import com.texthip.thip.data.model.service.BookService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepository @Inject constructor(
    private val bookService: BookService
) {

    // 저장된/모임 책 조회 API 연동
    suspend fun getBooks(type: String): Result<List<BookSavedResponse>> {
        return try {
            bookService.getBooks(type)
                .handleBaseResponse()
                .mapCatching { bookListResponse ->
                    bookListResponse?.bookList ?: emptyList()
                }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}