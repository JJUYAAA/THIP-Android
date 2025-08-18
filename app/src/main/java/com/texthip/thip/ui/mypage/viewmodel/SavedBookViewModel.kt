package com.texthip.thip.ui.mypage.viewmodel

import androidx.lifecycle.ViewModel
import com.texthip.thip.ui.mypage.mock.BookItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class SavedBookViewModel : ViewModel() {

    protected val _books = MutableStateFlow<List<BookItem>>(emptyList())
    open val books: StateFlow<List<BookItem>> = _books

    init {
        loadMockBooks()
    }

    open fun loadMockBooks() {
        _books.value = listOf(
            BookItem(
                id = 1,
                title = "이기적 유전자",
                author = "리처드 도킨스",
                publisher = "을유문화사",
                imageUrl = null,
                isSaved = true
            ),
            BookItem(
                id = 2,
                title = "이기적 유전자",
                author = "리처드 도킨스",
                publisher = "을유문화사",
                imageUrl = null,
                isSaved = true
            ),
            BookItem(
                id = 3,
                title = "이기적 유전자",
                author = "리처드 도킨스",
                publisher = "을유문화사",
                imageUrl = null,
                isSaved = true
            ),
            BookItem(
                id = 4,
                title = "이기적 유전자",
                author = "리처드 도킨스",
                publisher = "을유문화사",
                imageUrl = null,
                isSaved = true
            ),
            BookItem(
                id = 5,
                title = "이기적 유전자",
                author = "리처드 도킨스",
                publisher = "을유문화사",
                imageUrl = null,
                isSaved = true
            ),
            BookItem(
                id = 6,
                title = "이기적 유전자",
                author = "리처드 도킨스",
                publisher = "을유문화사",
                imageUrl = null,
                isSaved = true
            ),
            BookItem(
                id = 7,
                title = "이기적 유전자",
                author = "리처드 도킨스",
                publisher = "을유문화사",
                imageUrl = null,
                isSaved = true
            ),
            BookItem(
                id = 8,
                title = "이기적 유전자",
                author = "리처드 도킨스",
                publisher = "을유문화사",
                imageUrl = null,
                isSaved = true
            )
        )
    }
    fun toggleBookmark(id: Int) {
        _books.value = _books.value.map {
            if (it.id == id) it.copy(isSaved = !it.isSaved) else it
        }
    }
}

class EmptySavedBookViewModel : SavedBookViewModel() {
    override fun loadMockBooks() {}

}