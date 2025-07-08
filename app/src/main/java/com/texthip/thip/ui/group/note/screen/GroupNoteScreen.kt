package com.texthip.thip.ui.group.note.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.buttons.FilterButton
import com.texthip.thip.ui.common.buttons.FloatingButton
import com.texthip.thip.ui.common.header.HeaderMenuBarTab
import com.texthip.thip.ui.common.topappbar.DefaultTopAppBar
import com.texthip.thip.ui.group.note.component.FilterHeaderSection
import com.texthip.thip.ui.group.note.component.TextCommentCard
import com.texthip.thip.ui.group.note.component.VoteCommentCard
import com.texthip.thip.ui.group.note.mock.GroupNoteRecord
import com.texthip.thip.ui.group.note.mock.GroupNoteVote
import com.texthip.thip.ui.group.note.mock.mockGroupNoteItems
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun GroupNoteScreen() {
    val tabs = listOf(stringResource(R.string.group_record), stringResource(R.string.my_record))
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

    var firstPage by rememberSaveable { mutableStateOf("") }
    var lastPage by rememberSaveable { mutableStateOf("") }
    var isTotalSelected by rememberSaveable { mutableStateOf(false) }
    var totalEnabled by rememberSaveable { mutableStateOf(false) }

    var selectedFilter by rememberSaveable { mutableStateOf("최신순") }
    val filters = listOf("최신순", "인기순", "댓글 많은 순")

    val filteredItems = when (selectedTabIndex) {
        0 -> mockGroupNoteItems.filter { !it.isWriter } // 다른 사람 기록
        1 -> mockGroupNoteItems.filter { it.isWriter } // 내 기록
        else -> emptyList()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        DefaultTopAppBar(
            title = stringResource(R.string.record_book),
            onLeftClick = {}
        )

        HeaderMenuBarTab(
            titles = tabs,
            selectedTabIndex = selectedTabIndex,
            onTabSelected = { selectedTabIndex = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 56.dp)
        )

        // 피드 리스트 영역
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 82.dp),
        ) {
            item {
                Row(
                    modifier = Modifier.padding(
                        start = 20.dp,
                        end = 20.dp,
                        bottom = 16.dp,
                        top = 20.dp
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Icon(
                        painterResource(R.drawable.ic_information),
                        contentDescription = null,
                        tint = colors.White,
                    )
                    Text(
                        text = stringResource(R.string.group_note_info),
                        modifier = Modifier.padding(start = 8.dp),
                        color = colors.Grey01,
                        style = typography.info_r400_s12
                    )
                }
            }
            items(filteredItems) { item ->
                when (item) {
                    is GroupNoteRecord -> TextCommentCard(data = item)
                    is GroupNoteVote -> VoteCommentCard(data = item)
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 136.dp),
    ) {
        FilterHeaderSection(
            firstPage = firstPage,
            lastPage = lastPage,
            isTotalSelected = isTotalSelected,
            totalEnabled = totalEnabled,
            onFirstPageChange = { firstPage = it },
            onLastPageChange = { lastPage = it },
            onTotalToggle = { isTotalSelected = !isTotalSelected },
        )

        FilterButton(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 20.dp),
            selectedOption = selectedFilter,
            options = filters,
            onOptionSelected = { selectedFilter = it }
        )
    }

    FloatingButton(
        icon = painterResource(id = R.drawable.ic_plus),
        onClick = { /* 새 글 작성 */ }
    )
}

@Preview
@Composable
private fun GroupNoteScreenPreview() {
    ThipTheme {
        GroupNoteScreen()
    }
}