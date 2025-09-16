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

data class SavedBookUiState(
    val books: List<BookItem> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val isLast: Boolean = false,
    val error: String? = null
) {
    val canLoadMore: Boolean get() = !isLoading && !isLoadingMore && !isLast
}

@HiltViewModel
class SavedBookViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SavedBookUiState())
    val uiState = _uiState.asStateFlow()
    
    private var nextCursor: String? = null
    private var isLoadingBooks = false

    private fun updateState(update: (SavedBookUiState) -> SavedBookUiState) {
        _uiState.value = update(_uiState.value)
    }

    fun loadSavedBooks(isInitial: Boolean = true) {
        if (isLoadingBooks && !isInitial) return
        if (_uiState.value.isLast && !isInitial) return

        viewModelScope.launch {
            try {
                isLoadingBooks = true
                
                if (isInitial) {
                    updateState { it.copy(isLoading = true, books = emptyList(), isLast = false) }
                    nextCursor = null
                } else {
                    updateState { it.copy(isLoadingMore = true) }
                }

                val cursor = if (isInitial) null else nextCursor

                bookRepository.getSavedBooks(cursor)
                    .onSuccess { response ->
                        if (response != null) {
                            val currentList = if (isInitial) emptyList() else _uiState.value.books
                            val newBooks = response.bookList.map { it.toBookItem() }
                            updateState {
                                it.copy(
                                    books = currentList + newBooks,
                                    error = null,
                                    isLast = response.isLast
                                )
                            }
                            nextCursor = response.nextCursor
                        } else {
                            updateState { it.copy(isLast = true) }
                        }
                    }
                    .onFailure { exception ->
                        updateState { it.copy(error = exception.message) }
                        exception.printStackTrace()
                    }
            } finally {
                isLoadingBooks = false
                updateState { it.copy(isLoading = false, isLoadingMore = false) }
            }
        }
    }

    fun loadMoreBooks() {
        if (_uiState.value.canLoadMore) {
            loadSavedBooks(isInitial = false)
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