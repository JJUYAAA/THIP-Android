package com.texthip.thip.data.repository

import com.texthip.thip.data.model.base.handleBaseResponse
import com.texthip.thip.data.model.book.request.BookSaveRequest
import com.texthip.thip.data.model.book.response.BookDetailResponse
import com.texthip.thip.data.model.book.response.BookSaveResponse
import com.texthip.thip.data.model.book.response.BookSearchResponse
import com.texthip.thip.data.model.book.response.MostSearchedBooksResponse
import com.texthip.thip.data.model.book.response.RecentSearchResponse
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

    /** 최근 검색어 조회 */
    suspend fun getRecentSearches(type: String = "BOOK"): Result<RecentSearchResponse?> = runCatching {
        bookService.getRecentSearches(type)
            .handleBaseResponse()
            .getOrThrow()
    }

    /** 최근 검색어 삭제 */
    suspend fun deleteRecentSearch(recentSearchId: Int): Result<Unit> = runCatching {
        bookService.deleteRecentSearch(recentSearchId)
            .handleBaseResponse()
            .getOrThrow()
    }

    /** 책 상세 조회 */
    suspend fun getBookDetail(isbn: String): Result<BookDetailResponse?> = runCatching {
        bookService.getBookDetail(isbn)
            .handleBaseResponse()
            .getOrThrow()
    }

    /** 책 저장/저장취소 */
    suspend fun saveBook(isbn: String, type: Boolean): Result<BookSaveResponse?> = runCatching {
        bookService.saveBook(isbn, BookSaveRequest(type))
            .handleBaseResponse()
            .getOrThrow()
    }
}