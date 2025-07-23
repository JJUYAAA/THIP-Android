package com.texthip.thip.ui.booksearch.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.texthip.thip.R
import com.texthip.thip.ui.booksearch.component.BookEmptyResult
import com.texthip.thip.ui.booksearch.component.BookFilteredSearchResult
import com.texthip.thip.ui.booksearch.component.BookLiveSearchResult
import com.texthip.thip.ui.booksearch.component.BookRecentSearch
import com.texthip.thip.ui.booksearch.mock.BookData
import com.texthip.thip.ui.common.forms.SearchBookTextField
import com.texthip.thip.ui.common.topappbar.LeftNameTopAppBar
import com.texthip.thip.ui.theme.ThipTheme

@Composable
fun BookSearchScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController? = null,
    bookList: List<BookData> = emptyList(),
    popularBooks: List<BookData> = emptyList()
) {
    var recentSearches by rememberSaveable {
        mutableStateOf(listOf("asd", "qwe", "xcv", "dfggfd", "asdasd", "gfhjghj"))
    }
    var searchText by rememberSaveable { mutableStateOf("") }
    var isSearched by rememberSaveable { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val liveFilteredBookList by remember(searchText) {
        derivedStateOf {
            if (searchText.isBlank()) emptyList() else
                bookList.filter { book ->
                    book.title.contains(searchText, ignoreCase = true) ||
                            book.author.contains(searchText, ignoreCase = true) ||
                            book.publisher.contains(searchText, ignoreCase = true)
                }
        }
    }

    val filteredBookList by remember(searchText, isSearched) {
        derivedStateOf {
            if (!isSearched) emptyList()
            else {
                bookList.filter { book ->
                    searchText.isBlank() ||
                            book.title.contains(searchText, ignoreCase = true) ||
                            book.author.contains(searchText, ignoreCase = true) ||
                            book.publisher.contains(searchText, ignoreCase = true)
                }
            }
        }
    }

    LaunchedEffect(isSearched) {
        if (isSearched) {
            focusManager.clearFocus()
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            LeftNameTopAppBar(
                title = stringResource(R.string.book_search_topappbar)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                SearchBookTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    hint = stringResource(R.string.book_search_hint),
                    text = searchText,
                    onValueChange = {
                        searchText = it
                        isSearched = false
                    },
                    onSearch = { query ->
                        if (query.isNotBlank() && !recentSearches.contains(query)) {
                            recentSearches = listOf(query) + recentSearches
                        }
                        isSearched = true
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))

                when {
                    searchText.isBlank() && !isSearched -> {
                        BookRecentSearch(
                            recentSearches = recentSearches,
                            popularBooks = popularBooks,
                            popularBookDate = "01.12", // TODO: 서버로 날짜를 받아 오게 수정
                            onSearchClick = { keyword ->
                                searchText = keyword
                                isSearched = true
                            },
                            onRemove = { keyword ->
                                recentSearches = recentSearches.filterNot { it == keyword }
                            },
                            onBookClick = { book ->
                                // 책 클릭 시 처리
                            }
                        )
                    }

                    searchText.isNotBlank() && !isSearched -> {
                        if (liveFilteredBookList.isEmpty()) {
                            BookEmptyResult(
                                mainText = stringResource(R.string.book_no_search_result1),
                                subText = stringResource(R.string.book_no_search_result2),
                                onRequestBook = { /*책 요청 처리*/ }
                            )
                        } else {
                            BookLiveSearchResult(
                                bookList = liveFilteredBookList
                            )
                        }
                    }

                    isSearched -> {
                        BookFilteredSearchResult(
                            resultCount = filteredBookList.size,
                            bookList = filteredBookList,
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewBookSearchScreen_Default() {
    ThipTheme {
        BookSearchScreen(
            bookList = listOf(
                BookData("aaa", "리처드 도킨스", "을유문화사", R.drawable.bookcover_sample),
                BookData("abc", "마틴 셀리그만", "물푸레", R.drawable.bookcover_sample),
                BookData("abcd", "빅터 프랭클", "청림출판", R.drawable.bookcover_sample),
                BookData("abcde", "칼 융", "문학과지성사", R.drawable.bookcover_sample),
                BookData("abcdef", "에릭 프롬", "까치글방", R.drawable.bookcover_sample),
                BookData("abcedfg", "알베르 카뮈", "민음사", R.drawable.bookcover_sample),
                BookData("abcdefgh", "장 폴 사르트르", "문학동네", R.drawable.bookcover_sample),
            ),
            popularBooks = listOf(
                BookData("단 한번의 삶", "리처드 도킨스", "을유문화사", R.drawable.bookcover_sample),
                BookData("사랑", "마틴 셀리그만", "물푸레", R.drawable.bookcover_sample),
                BookData("호모 사피엔스", "빅터 프랭클", "청림출판", R.drawable.bookcover_sample),
                BookData("코스모스 실버", "칼 융", "문학과지성사", R.drawable.bookcover_sample),
                BookData("오만과 편견", "에릭 프롬", "까치글방", R.drawable.bookcover_sample),
            )
        )
    }
}

@Preview
@Composable
fun PreviewBookSearchScreen_EmptyPopular() {
    ThipTheme {
        BookSearchScreen(
            bookList = listOf(
                BookData("aaa", "리처드 도킨스", "을유문화사", R.drawable.bookcover_sample),
                BookData("abc", "마틴 셀리그만", "물푸레", R.drawable.bookcover_sample),
                BookData("abcd", "빅터 프랭클", "청림출판", R.drawable.bookcover_sample),
                BookData("abcde", "칼 융", "문학과지성사", R.drawable.bookcover_sample),
                BookData("abcdef", "에릭 프롬", "까치글방", R.drawable.bookcover_sample),
                BookData("abcedfg", "알베르 카뮈", "민음사", R.drawable.bookcover_sample),
                BookData("abcdefgh", "장 폴 사르트르", "문학동네", R.drawable.bookcover_sample),
            ),
            popularBooks = emptyList()
        )
    }
}
