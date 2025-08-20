package com.texthip.thip.ui.group.makeroom.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.ui.common.cards.CardBookSearch
import com.texthip.thip.ui.common.modal.drawVerticalScrollbar
import com.texthip.thip.ui.group.makeroom.mock.BookData
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors


@Composable
fun GroupBookListWithScrollbar(
    books: List<BookData>,
    onBookClick: (BookData) -> Unit
) {
    val scrollState = rememberScrollState()

    Box(
        Modifier
            .fillMaxWidth()
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .drawVerticalScrollbar(scrollState)
        ) {
            books.forEachIndexed { index, book ->
                CardBookSearch(
                    title = book.title,
                    imageUrl = book.imageUrl,
                    onClick = { onBookClick(book) }
                )
                Spacer(modifier = Modifier.height(12.dp))
                if (index < books.size - 1) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 6.dp)
                            .height(1.dp)
                            .background(color = colors.Grey02)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Preview()
@Composable
fun PreviewBookListWithScrollbar() {
    ThipTheme {
        Column {
            GroupBookListWithScrollbar(
                books = List(20) { BookData("Book $it", null) },
                onBookClick = {}
            )
        }
    }
}

