package com.texthip.thip.ui.group.makeroom.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.bottomsheet.CustomBottomSheet
import com.texthip.thip.ui.common.forms.SearchBookTextField
import com.texthip.thip.ui.common.header.HeaderMenuBarTab
import com.texthip.thip.ui.group.makeroom.mock.BookData
import com.texthip.thip.ui.group.makeroom.mock.dummyGroupBooks
import com.texthip.thip.ui.group.makeroom.mock.dummySavedBooks
import com.texthip.thip.ui.theme.ThipTheme

@Composable
fun GroupBookSearchBottomSheet(
    onDismiss: () -> Unit,
    onBookSelect: (BookData) -> Unit,
    onRequestBook: () -> Unit,
    savedBooks: List<BookData>,
    groupBooks: List<BookData>,
    searchResults: List<BookData> = emptyList(),
    isLoading: Boolean = false,
    isSearching: Boolean = false,
    onSearch: (String) -> Unit = {}
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val tabs = listOf(
        stringResource(R.string.group_saved_book), stringResource(R.string.group_book)
    )

    var searchText by rememberSaveable { mutableStateOf("") }

    val currentBooks = if (selectedTab == 0) savedBooks else groupBooks

    // 검색어가 있으면 검색 결과 사용, 없으면 탭별 도서 목록 사용
    val displayBooks = if (searchText.isNotEmpty()) {
        searchResults
    } else {
        currentBooks
    }

    val showNoSearchResultsError = searchText.isNotEmpty() && displayBooks.isEmpty() && !isSearching

    CustomBottomSheet(
        onDismiss = onDismiss
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 90.dp)
        ) {
            Column(Modifier.padding(start = 20.dp, end = 20.dp, top = 20.dp)) {
                SearchBookTextField(
                    hint = stringResource(R.string.group_book_search_hint),
                    text = searchText,
                    onValueChange = {
                        searchText = it
                        onSearch(it)
                    },
                    onSearch = { onSearch(searchText) },
                )
                Spacer(Modifier.height(20.dp))
            }

            if (showNoSearchResultsError) {
                EmptyBookSheetContent(onRequestBook)
            } else {
                // 검색어가 없을 때만 탭 표시
                if (searchText.isEmpty()) {
                    HeaderMenuBarTab(
                        titles = tabs,
                        selectedTabIndex = selectedTab,
                        onTabSelected = { selectedTab = it },
                        indicatorColor = ThipTheme.colors.White,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(20.dp))
                }

                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    else -> {
                        Column(Modifier.padding(horizontal = 20.dp)) {
                            GroupBookListWithScrollbar(
                                books = displayBooks,
                                onBookClick = onBookSelect
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewBookSearchBottomSheet_HasBooks() {
    ThipTheme {
        var showSheet by remember { mutableStateOf(true) }
        if (showSheet) {
            GroupBookSearchBottomSheet(
                onDismiss = { showSheet = false },
                onBookSelect = {},
                onRequestBook = {},
                savedBooks = dummySavedBooks,   // 데이터 있음
                groupBooks = dummyGroupBooks,
                isLoading = false
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBookSearchBottomSheet_Empty() {
    ThipTheme {
        var showSheet by remember { mutableStateOf(true) }
        if (showSheet) {
            GroupBookSearchBottomSheet(
                onDismiss = { showSheet = false },
                onBookSelect = {},
                onRequestBook = {},
                savedBooks = emptyList(),   // 데이터 없음
                groupBooks = emptyList(),
                isLoading = false
            )
        }
    }
}