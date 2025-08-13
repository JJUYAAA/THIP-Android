package com.texthip.thip.ui.search.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.cards.CardBookList
import com.texthip.thip.ui.search.mock.BookData
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun SearchBookFilteredResult(
    resultCount: Int,
    bookList: List<BookData>,
    isLoading: Boolean = false,
    hasMore: Boolean = true,
    onLoadMore: () -> Unit = {},
    onBookClick: (BookData) -> Unit = {}
) {
    val listState = rememberLazyListState()
    
    // 무한 스크롤을 위한 로직
    val shouldLoadMore by remember(hasMore, isLoading) {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItemsCount = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1

            hasMore && !isLoading && totalItemsCount > 0 && lastVisibleItemIndex >= totalItemsCount - 3
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            onLoadMore()
        }
    }
    
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.group_searched_room_size, resultCount),
                color = colors.Grey,
                style = typography.menu_m500_s14_h24
            )
        }
        Spacer(
            modifier = Modifier
                .padding(top = 4.dp, bottom = 20.dp)
                .fillMaxWidth()
                .height(1.dp)
                .background(colors.DarkGrey02)
        )

        LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
                itemsIndexed(bookList) { index, book ->
                    Column {
                        CardBookList(
                            modifier = Modifier.clickable { onBookClick(book) },
                            title = book.title,
                            author = book.author,
                            publisher = book.publisher,
                            imageUrl = book.imageUrl
                        )
                        if (index < bookList.size - 1) {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(colors.DarkGrey02)
                            )
                        }
                    }
                }
                
                // 로딩 인디케이터
                if (isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = colors.White
                            )
                        }
                    }
                }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBookFilteredSearchResult() {
    ThipTheme {
        SearchBookFilteredResult(
            resultCount = 3,
            bookList = listOf(
                BookData(
                    title = "이기적 유전자",
                    author = "리처드 도킨스",
                    publisher = "을유문화사"
                ),
                BookData(
                    title = "총, 균, 쇠",
                    author = "재레드 다이아몬드",
                    publisher = "문학사상사"
                ),
                BookData(
                    title = "코스모스",
                    author = "칼 세이건",
                    publisher = "사이언스북스"
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBookFilteredSearchResultEmpty() {
    ThipTheme {
        SearchBookFilteredResult(
            resultCount = 0,
            bookList = emptyList()
        )
    }
}
