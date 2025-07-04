package com.texthip.thip.ui.group.makeroom.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.ui.common.forms.SearchBookTextField
import com.texthip.thip.ui.group.makeroom.mock.BookData
import com.texthip.thip.ui.group.makeroom.mock.dummyGroupBooks
import com.texthip.thip.ui.group.makeroom.mock.dummySavedBooks
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookSearchBottomSheet(
    onDismiss: () -> Unit,
    onBookSelect: (BookData) -> Unit
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val tabs = listOf("저장한 책", "모임 책")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = colors.DarkGrey
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // 검색창
            SearchBookTextField(
                hint = "책 제목, 저자검색",
                onSearch = { /* 실제 검색 구현 시 여기에 */ }
            )
            Spacer(Modifier.height(20.dp))
            // 탭
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = colors.DarkGrey02,
                contentColor = colors.White,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = colors.White,
                        height = 2.dp
                    )
                }
            ) {
                tabs.forEachIndexed { idx, tab ->
                    Tab(
                        selected = selectedTab == idx,
                        onClick = { selectedTab = idx },
                        text = { Text(tab, color = if (selectedTab == idx) colors.White else colors.Grey03) }
                    )
                }
            }
            Spacer(Modifier.height(10.dp))
            // 리스트 + 스크롤바
            BookListWithScrollbar(
                books = if (selectedTab == 0) dummySavedBooks else dummyGroupBooks,
                onBookClick = onBookSelect
            )
        }
    }
}

@Preview()
@Composable
fun PreviewBookSearchBottomSheet() {
    ThipTheme {
        var showSheet by remember { mutableStateOf(true) }
        val onBookSelect: (BookData) -> Unit = {}

        if (showSheet) {
            BookSearchBottomSheet(
                onDismiss = { showSheet = false },
                onBookSelect = onBookSelect
            )
        }
    }
}