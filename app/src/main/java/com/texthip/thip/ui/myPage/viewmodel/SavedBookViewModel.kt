package com.texthip.thip.ui.myPage.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.texthip.thip.ui.myPage.mock.BookItem

class SavedBookViewModel : ViewModel() {

    var bookList by mutableStateOf<List<BookItem>>(emptyList())
        private set
    init {
        loadMockBooks()
    }

    private fun loadMockBooks() {
        bookList = listOf(
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
        bookList = bookList.map {
            if (it.id == id) it.copy(isSaved = !it.isSaved) else it
        }
    }
}