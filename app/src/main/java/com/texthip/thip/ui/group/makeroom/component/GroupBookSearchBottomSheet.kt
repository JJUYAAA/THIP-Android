package com.texthip.thip.ui.group.makeroom.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.forms.SearchBookTextField
import com.texthip.thip.ui.group.makeroom.mock.BookData
import com.texthip.thip.ui.group.makeroom.mock.dummyGroupBooks
import com.texthip.thip.ui.group.makeroom.mock.dummySavedBooks
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.common.bottomsheet.CustomBottomSheet
import com.texthip.thip.ui.common.header.HeaderMenuBarTab

@Composable
fun GroupBookSearchBottomSheet(
    onDismiss: () -> Unit,
    onBookSelect: (BookData) -> Unit,
    onRequestBook: () -> Unit,
    savedBooks: List<BookData> = emptyList(),
    groupBooks: List<BookData> = emptyList()
) {
    val hasBooks = savedBooks.isNotEmpty() || groupBooks.isNotEmpty()
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val tabs = listOf(
        stringResource(R.string.group_saved_book), stringResource(R.string.group_book)
    )

    var searchText by rememberSaveable { mutableStateOf("") }

    val currentBooks = if (selectedTab == 0) savedBooks else groupBooks

    val filteredBooks by remember(currentBooks, searchText) {
        derivedStateOf {
            if (searchText.isEmpty()) {
                currentBooks
            } else {
                currentBooks.filter { book ->
                    book.title.contains(searchText, ignoreCase = true) ||
                            (book.author?.contains(searchText, ignoreCase = true) == true)
                }
            }
        }
    }

    // 검색 결과가 있는지 확인
    val hasSearchResults = searchText.isEmpty() || filteredBooks.isNotEmpty()

    CustomBottomSheet(
        onDismiss = onDismiss
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 20.dp)
        ) {
            // 검색창
            SearchBookTextField(
                hint = stringResource(R.string.group_book_search_hint),
                text = searchText,
                onValueChange = { searchText = it },
                onSearch = { /* 이미 실시간으로 필터링됨 */ },
            )
            Spacer(Modifier.height(20.dp))
        }

        // 책이 있고 검색 결과가 있을 때만 탭 표시
        if (hasBooks && hasSearchResults) {
            HeaderMenuBarTab(
                titles = tabs,
                selectedTabIndex = selectedTab,
                onTabSelected = {
                    selectedTab = it
                },
                indicatorColor = ThipTheme.colors.White,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Column(
            Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 90.dp)
        ) {
            Spacer(Modifier.height(20.dp))

            when {
                // 검색 결과가 없을 때 (검색어가 있지만 결과가 없음)
                searchText.isNotEmpty() && filteredBooks.isEmpty() -> {
                    SearchEmptyContent(
                        searchText = searchText,
                        onRequestBook = onRequestBook
                    )
                }
                // 전체 책이 없을 때
                !hasBooks -> {
                    EmptyBookSheetContent(onRequestBook = onRequestBook)
                }
                // 현재 탭의 책이 없을 때
                currentBooks.isEmpty() -> {
                    EmptyBookSheetContent(onRequestBook = onRequestBook)
                }
                // 정상적으로 책 목록이 있을 때
                else -> {
                    GroupBookListWithScrollbar(
                        books = filteredBooks,
                        onBookClick = onBookSelect
                    )
                }
            }
        }
    }
}


// 검색 결과가 없을 때 표시할 컴포넌트 (필요시 구현)
@Composable
private fun SearchEmptyContent(
    searchText: String,
    onRequestBook: () -> Unit
) {
    // 검색 결과가 없을 때의 UI
    // 예: "'{searchText}'에 대한 검색 결과가 없습니다" 메시지와 책 요청 버튼
    EmptyBookSheetContent(onRequestBook = onRequestBook)
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
                groupBooks = dummyGroupBooks
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
                groupBooks = emptyList()
            )
        }
    }
}