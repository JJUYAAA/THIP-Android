package com.texthip.thip.ui.mypage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.model.book.response.BookUserSaveList
import com.texthip.thip.data.repository.BookRepository
import com.texthip.thip.ui.mypage.mock.BookItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedBookViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _books = MutableStateFlow<List<BookItem>>(emptyList())
    val books = _books.asStateFlow()

    fun loadSavedBooks() {
        viewModelScope.launch {
            bookRepository.getSavedBooks()
                .onSuccess { response ->
                    _books.value = response?.bookList?.map { it.toBookItem() } ?: emptyList()
                }
                .onFailure {
                    it.printStackTrace()
                }
        }
    }

    fun toggleBookmark(isbn: String) {
        viewModelScope.launch {
            bookRepository.saveBook(isbn, type = false)
                .onSuccess {
                    loadSavedBooks()
                }
        }
    }
}

private fun BookUserSaveList.toBookItem(): BookItem {
    return BookItem(
        id = this.bookId,
        title = this.bookTitle,
        author = this.authorName,
        publisher = this.publisher,
        imageUrl = this.bookImageUrl,
        isSaved = this.isSaved,
        isbn = this.isbn,
    )
}