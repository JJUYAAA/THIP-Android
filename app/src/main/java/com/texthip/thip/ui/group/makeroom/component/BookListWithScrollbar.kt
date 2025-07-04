package com.texthip.thip.ui.group.makeroom.component


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.cards.CardBookSearch
import com.texthip.thip.ui.group.makeroom.mock.BookData
import com.texthip.thip.ui.common.modal.drawVerticalScrollbar
import com.texthip.thip.ui.theme.ThipTheme

@Composable
fun BookListWithScrollbar(
    books: List<BookData>,
    onBookClick: (BookData) -> Unit
) {
    val scrollState = rememberScrollState()

    Box(
        Modifier
            .fillMaxWidth()
            .height(320.dp)
    ) {
        Column(
            Modifier
                .verticalScroll(scrollState)
                .fillMaxWidth()
        ) {
            books.forEach { book ->
                CardBookSearch(
                    title = book.title,
                    imageRes = book.imageRes,
                    onClick = { onBookClick(book) }
                )
            }
        }
        // 커스텀 스크롤바
        Box(
            Modifier
                .align(Alignment.CenterEnd)
                .drawVerticalScrollbar(scrollState)
        )
    }
}

@Preview()
@Composable
fun PreviewBookListWithScrollbar() {
    ThipTheme {
        Column {
            BookListWithScrollbar(
                books = listOf(
                    BookData("단 한번의 삶", R.drawable.bookcover_sample),
                    BookData("토마토 컬러면", R.drawable.bookcover_sample),
                    BookData("사슴", R.drawable.bookcover_sample),
                    BookData("명작 읽기방", R.drawable.bookcover_sample),
                    BookData("또 다른 방", R.drawable.bookcover_sample)
                ),
                onBookClick = {}
            )
        }
    }
}