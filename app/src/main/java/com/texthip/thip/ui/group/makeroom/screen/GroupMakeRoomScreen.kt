package com.texthip.thip.ui.group.makeroom.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.topappbar.InputTopAppBar
import com.texthip.thip.ui.group.makeroom.component.GroupSelectBook
import com.texthip.thip.ui.group.makeroom.component.BookSearchBottomSheet
import com.texthip.thip.ui.group.makeroom.mock.BookData
import com.texthip.thip.ui.group.makeroom.mock.dummySavedBooks
import com.texthip.thip.ui.group.makeroom.mock.dummyGroupBooks
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors

@Composable
fun GroupMakeRoomScreen(modifier: Modifier = Modifier) {
    var isButtonEnable by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    var showBookSearchSheet by remember { mutableStateOf(false) }

    Box {
        Column(
            modifier = modifier
                .verticalScroll(scrollState)
                .fillMaxSize()
                .then(
                    if (showBookSearchSheet) Modifier.blur(5.dp) else Modifier
                ),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InputTopAppBar(
                title = stringResource(R.string.group_making_group),
                isRightButtonEnabled = isButtonEnable,
                onLeftClick = {},
                onRightClick = {}
            )
            Spacer(modifier = Modifier.padding(top = 20.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GroupSelectBook(
                    onButtonClick = { showBookSearchSheet = true } // 검색해서 찾기 버튼에서 호출
                )
                Spacer(modifier = Modifier.padding(top = 32.dp))
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(colors.DarkGrey02)
                )
                Spacer(modifier = Modifier.padding(top = 32.dp))
            }
        }

        if (showBookSearchSheet) {
            BookSearchBottomSheet(
                onDismiss = { showBookSearchSheet = false },
                onBookSelect = { _: BookData ->
                    // 책 선택 처리
                    showBookSearchSheet = false
                },
                onRequestBook = {
                    // 책 신청하기 버튼
                    showBookSearchSheet = false
                },
                savedBooks = dummySavedBooks,
                groupBooks = dummyGroupBooks,
                defaultTab = 0
            )
        }
    }
}

@Preview
@Composable
private fun GroupMakeRoomScreenPreview() {
    ThipTheme {
        GroupMakeRoomScreen()
    }
}
