package com.texthip.thip.data.repository

import com.texthip.thip.data.model.base.handleBaseResponse
import com.texthip.thip.data.model.book.response.BookSearchResponse
import com.texthip.thip.data.model.book.response.MostSearchedBooksResponse
import com.texthip.thip.data.service.BookService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepository @Inject constructor(
    private val bookService: BookService
) {

    /** 저장된 책 또는 모임 책 목록 조회 */
    suspend fun getBooks(type: String) = runCatching {
        bookService.getBooks(type)
            .handleBaseResponse()
            .getOrThrow()
            ?.bookList ?: emptyList()
    }

    /** 책 검색 */
    suspend fun searchBooks(keyword: String, page: Int = 1): Result<BookSearchResponse?> = runCatching {
        bookService.searchBooks(keyword, page)
            .handleBaseResponse()
            .getOrThrow()
    }

    /** 인기 책 조회 */
    suspend fun getMostSearchedBooks(): Result<MostSearchedBooksResponse?> = runCatching {
        bookService.getMostSearchedBooks()
            .handleBaseResponse()
            .getOrThrow()
    }
}