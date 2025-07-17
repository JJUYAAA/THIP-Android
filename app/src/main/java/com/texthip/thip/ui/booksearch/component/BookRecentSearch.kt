package com.texthip.thip.ui.booksearch.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.booksearch.mock.BookData
import com.texthip.thip.ui.common.buttons.GenreChipButton
import com.texthip.thip.ui.common.cards.CardBookSearch
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun BookRecentSearch(
    recentSearches: List<String>,
    popularBooks: List<BookData>,
    popularBookDate: String,
    onSearchClick: (String) -> Unit,
    onRemove: (String) -> Unit,
    onBookClick: (BookData) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // 최근 검색어
        Text(
            text = stringResource(R.string.group_recent_search),
            color = colors.White,
            style = typography.menu_r400_s14_h24
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (recentSearches.isEmpty()) {
            Text(
                text = stringResource(R.string.group_no_recent_search),
                color = colors.Grey01,
                style = typography.menu_r400_s14_h24
            )
        } else {
            FlowRow(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                maxLines = 2,
            ) {
                recentSearches.take(9).forEach { keyword ->
                    GenreChipButton(
                        text = keyword,
                        onClick = { onSearchClick(keyword) },
                        onCloseClick = { onRemove(keyword) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 가장 많이 검색된 책 헤더
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.book_popular_search),
                color = colors.White,
                style = typography.menu_r400_s14_h24
            )
            Text(
                text = stringResource(R.string.book_search_date, popularBookDate),
                color = colors.Grey02,
                style = typography.timedate_r400_s11
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // 인기책 리스트만 LazyColumn, 나머지는 고정
        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(1f, fill = true)
        ) {
            if (popularBooks.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.book_no_most_search_result_comment_1),
                        style = typography.smalltitle_sb600_s18_h24,
                        color = colors.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.book_no_most_search_result_comment_2),
                        style = typography.copy_r400_s14,
                        color = colors.Grey
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(popularBooks.size) { index ->
                        val book = popularBooks[index]
                        CardBookSearch(
                            number = index + 1,
                            title = book.title,
                            imageRes = book.imageRes,
                            onClick = { onBookClick(book) }
                        )
                        if (index < popularBooks.size - 1) {
                            Spacer(
                                modifier = Modifier
                                    .padding(top = 12.dp, bottom = 12.dp)
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(ThipTheme.colors.DarkGrey02)
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun PreviewBookRecentSearch() {
    ThipTheme {
        BookRecentSearch(
            recentSearches = listOf("소설", "심리학", "철학", "역사", "과학", "에세이"),
            popularBooks = listOf(
                BookData(
                    title = "이기적 유전자",
                    author = "리처드 도킨스",
                    publisher = "을유문화사",
                    imageRes = R.drawable.bookcover_sample
                ),
                BookData(
                    title = "코스모스",
                    author = "칼 세이건",
                    publisher = "사이언스북스",
                    imageRes = R.drawable.bookcover_sample
                ),
                BookData(
                    title = "총, 균, 쇠",
                    author = "재레드 다이아몬드",
                    publisher = "문학사상사",
                    imageRes = R.drawable.bookcover_sample
                )
            ),
            popularBookDate = "01.12",
            onSearchClick = {},
            onRemove = {},
            onBookClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBookRecentSearch_EmptyPopular() {
    ThipTheme {
        BookRecentSearch(
            recentSearches = emptyList(),
            popularBooks = emptyList(),
            popularBookDate = "01.12.",
            onSearchClick = {},
            onRemove = {},
            onBookClick = {}
        )
    }
}
