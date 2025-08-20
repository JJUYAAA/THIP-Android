package com.texthip.thip.ui.mypage.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.cards.CardBookList
import com.texthip.thip.ui.mypage.mock.BookItem
import com.texthip.thip.ui.mypage.viewmodel.SavedBookViewModel
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun BookContent(
    bookList: List<BookItem>, viewModel: SavedBookViewModel
) {
    if (bookList.isEmpty()) {
        EmptyBookContent()
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            itemsIndexed(bookList, key = { _, book -> book.id }) { index, book ->
                if (index == 0) {
                    Spacer(Modifier.height(32.dp))
                }

                CardBookList(
                    title = book.title,
                    author = book.author,
                    imageUrl = book.imageUrl,
                    publisher = book.publisher,
                    showBookmark = true,
                    isBookmarked = book.isSaved,
                    onBookmarkClick = { viewModel.toggleBookmark(book.isbn) }
                )

                if (index != bookList.lastIndex) {
                    Spacer(Modifier.height(20.dp))
                    HorizontalDivider(
                        color = colors.DarkGrey02,
                        thickness = 1.dp
                    )
                    Spacer(Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
fun EmptyBookContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.no_saved_book),
            style = typography.smalltitle_sb600_s18_h24,
            color = colors.White
        )
        Text(
            text = stringResource(R.string.do_thip_book),
            style = typography.feedcopy_r400_s14_h20,
            color = colors.Grey,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}