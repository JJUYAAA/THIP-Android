package com.texthip.thip.ui.booksearch.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.texthip.thip.ui.booksearch.mock.BookData
import com.texthip.thip.ui.common.cards.CardBookList
import com.texthip.thip.ui.theme.ThipTheme.colors

@Composable
fun BookLiveSearchResult(
    bookList: List<BookData>
) {
    LazyColumn(
        verticalArrangement = Arrangement.Center
    ) {
        itemsIndexed(bookList) { index, book ->
            CardBookList(
                title = book.title,
                author = book.author,
                publisher = book.publisher,
                imageRes = book.imageRes
            )
            if (index < bookList.size - 1) {
                Spacer(
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 12.dp)
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(colors.DarkGrey02)
                )
            }
        }
    }
}