package com.texthip.thip.ui.myPage.component

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.texthip.thip.ui.common.cards.CardBookList
import com.texthip.thip.ui.myPage.viewmodel.SavedBookViewModel

@Composable
fun BookContent(viewModel: SavedBookViewModel = viewModel()) {
    val books by viewModel.bookList.collectAsState()

    LazyColumn {
        items(items = books, key = { it.id }) { book ->
            CardBookList(
                title = book.title,
                author = book.author,
                imageRes = null,
                publisher = book.publisher,
                isBookmarked = book.isSaved,
                onBookmarkClick = { viewModel.toggleBookmark(book.id) }
            )
        }
    }
}