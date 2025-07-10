package com.texthip.thip.ui.group.makeroom.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
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
    // 책이 있는지 여부 체크
    val hasBooks = savedBooks.isNotEmpty() || groupBooks.isNotEmpty()
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val tabs = listOf(
        stringResource(R.string.group_saved_book), stringResource(R.string.group_book)
    )
    val books = if (selectedTab == 0) savedBooks else groupBooks

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
                onSearch = { /* 검색 구현 */ }
            )
            Spacer(Modifier.height(20.dp))
        }

        if (hasBooks) {
            HeaderMenuBarTab(
                titles = tabs,
                selectedTabIndex = selectedTab,
                onTabSelected = { selectedTab = it },
                indicatorColor = ThipTheme.colors.White,
                modifier = Modifier.fillMaxWidth()
            )

            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
            ) {
                Spacer(Modifier.height(20.dp))
                if (books.isEmpty()) {
                    EmptyBookSheetContent(onRequestBook = onRequestBook)
                } else {
                    GroupBookListWithScrollbar(
                        books = books,
                        onBookClick = onBookSelect
                    )
                }
            }
        } else {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
            ) {
                Spacer(Modifier.height(20.dp))
                EmptyBookSheetContent(onRequestBook = onRequestBook)
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