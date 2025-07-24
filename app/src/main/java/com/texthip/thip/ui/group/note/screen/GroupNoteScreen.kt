package com.texthip.thip.ui.group.note.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.texthip.thip.R
import com.texthip.thip.ui.common.bottomsheet.MenuBottomSheet
import com.texthip.thip.ui.common.buttons.ExpandableFloatingButton
import com.texthip.thip.ui.common.buttons.FabMenuItem
import com.texthip.thip.ui.common.buttons.FilterButton
import com.texthip.thip.ui.common.header.HeaderMenuBarTab
import com.texthip.thip.ui.common.modal.ToastWithDate
import com.texthip.thip.ui.common.topappbar.DefaultTopAppBar
import com.texthip.thip.ui.group.note.component.CommentBottomSheet
import com.texthip.thip.ui.group.note.component.FilterHeaderSection
import com.texthip.thip.ui.group.note.component.TextCommentCard
import com.texthip.thip.ui.group.note.component.VoteCommentCard
import com.texthip.thip.ui.group.note.mock.GroupNoteRecord
import com.texthip.thip.ui.group.note.mock.GroupNoteVote
import com.texthip.thip.ui.group.note.mock.mockComment
import com.texthip.thip.ui.group.note.mock.mockGroupNoteItems
import com.texthip.thip.ui.group.room.mock.MenuBottomSheetItem
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography
import kotlinx.coroutines.delay

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
        0 -> mockGroupNoteItems // 전체 기록
        1 -> mockGroupNoteItems.filter { it.isWriter } // 내 기록만
        else -> emptyList()
    }

    var isCommentBottomSheetVisible by rememberSaveable { mutableStateOf(false) }
    var selectedNoteRecord by remember { mutableStateOf<GroupNoteRecord?>(null) }
    var selectedNoteVote by remember { mutableStateOf<GroupNoteVote?>(null) }
    var selectedItemForMenu by remember { mutableStateOf<Any?>(null) }

    var isMenuBottomSheetVisible by rememberSaveable { mutableStateOf(false) }

    var showToast by remember { mutableStateOf(false) }

    // 토스트 3초
    LaunchedEffect(showToast) {
        if (showToast) {
            delay(6000) // 2초 등장, 4초 노출
            showToast = false // exit 에니메이션 2초
        }
    }

    Box(
        if (isCommentBottomSheetVisible || isMenuBottomSheetVisible) {
            Modifier
                .fillMaxSize()
                .blur(5.dp)
        } else {
            Modifier.fillMaxSize()
        }
    ) {
        Box {
            AnimatedVisibility(
                visible = showToast,
                enter = slideInVertically(
                    initialOffsetY = { -it }, // 위에서 아래로
                    animationSpec = tween(durationMillis = 2000)
                ),
                exit = slideOutVertically(
                    targetOffsetY = { -it }, // 위로 사라짐
                    animationSpec = tween(durationMillis = 2000)
                ),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .zIndex(3f)
            ) {
                ToastWithDate(
                    message = stringResource(R.string.condition_of_view_general_review),
                )
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
                        .padding(top = 20.dp)
                        .padding(bottom = if (selectedTabIndex == 0) 0.dp else 20.dp),
                )

                if (filteredItems.isEmpty()) {
                    // 기록이 없을 때 중앙에 메시지
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 102.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(
                            8.dp,
                            alignment = Alignment.CenterVertically
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.no_records_yet),
                            style = typography.smalltitle_sb600_s18_h24,
                            color = colors.White
                        )
                        Text(
                            text = when (selectedTabIndex) {
                                0 -> stringResource(R.string.no_group_record_subtext)
                                1 -> stringResource(R.string.no_my_record_subtext)
                                else -> ""
                            },
                            style = typography.copy_r400_s14,
                            color = colors.Grey
                        )
                    }
                } else {
                    // 피드 리스트 영역
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(32.dp)
                    ) {
                        if (selectedTabIndex == 0) {
                            item {
                                Row(
                                    modifier = Modifier.padding(top = 76.dp),
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
                        }
                        itemsIndexed(filteredItems) { index, item ->
                            val isLast = index == filteredItems.lastIndex

                            val itemModifier = if (isLast) {
                                Modifier.padding(bottom = 20.dp)
                            } else {
                                Modifier
                            }

                            when (item) {
                                is GroupNoteRecord -> TextCommentCard(
                                    data = item,
                                    modifier = itemModifier,
                                    onCommentClick = {
                                        selectedNoteRecord = item
                                        isCommentBottomSheetVisible = true
                                    },
                                    onLongPress = {
                                        selectedItemForMenu = item
                                        isMenuBottomSheetVisible = true
                                    }
                                )

                                is GroupNoteVote -> VoteCommentCard(
                                    data = item,
                                    modifier = itemModifier,
                                    onCommentClick = {
                                        selectedNoteVote = item
                                        isCommentBottomSheetVisible = true
                                    },
                                    onLongPress = {
                                        selectedItemForMenu = item
                                        isMenuBottomSheetVisible = true
                                    }
                                )
                            }
                        }
                    }
                }
            }

            if (selectedTabIndex == 0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 119.dp)
                        .background(color = colors.Black)
                        .padding(top = 20.dp)
                ) {
                    FilterButton(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 20.dp),
                        selectedOption = selectedFilter,
                        options = filters,
                        onOptionSelected = { selectedFilter = it }
                    )

                    FilterHeaderSection(
                        firstPage = firstPage,
                        lastPage = lastPage,
                        isTotalSelected = isTotalSelected,
                        totalEnabled = totalEnabled,
                        onFirstPageChange = { firstPage = it },
                        onLastPageChange = { lastPage = it },
                        onTotalToggle = { isTotalSelected = !isTotalSelected },
                        onDisabledClick = { showToast = true }
                    )
                }
            }

            ExpandableFloatingButton(
                menuItems = listOf(
                    FabMenuItem(
                        icon = painterResource(R.drawable.ic_write),
                        text = stringResource(R.string.write_record),
                        onClick = { }
                    ),
                    FabMenuItem(
                        icon = painterResource(R.drawable.ic_vote),
                        text = stringResource(R.string.create_vote),
                        onClick = { }
                    )
                )
            )
        }

        if (isCommentBottomSheetVisible && (selectedNoteRecord != null || selectedNoteVote != null)) {
            CommentBottomSheet(
                commentResponse = listOf(mockComment, mockComment, mockComment),
                onDismiss = {
                    isCommentBottomSheetVisible = false
                    selectedNoteRecord = null
                    selectedNoteVote = null
                },
                onSendReply = { replyText, commentId, replyTo ->
                    // 댓글 전송 로직 구현
                }
            )
        }
    }

    if (isMenuBottomSheetVisible && selectedItemForMenu != null) {
        val isWriter = when (val item = selectedItemForMenu) {
            is GroupNoteRecord -> item.isWriter
            is GroupNoteVote -> item.isWriter
            else -> false
        }

        val menuItems = if (isWriter) {
            listOf(
                MenuBottomSheetItem(
                    text = stringResource(R.string.delete),
                    color = colors.Red,
                    onClick = {
                        // TODO: 삭제 처리
                        isMenuBottomSheetVisible = false
                        selectedItemForMenu = null
                    }
                )
            )
        } else {
            listOf(
                MenuBottomSheetItem(
                    text = stringResource(R.string.report),
                    color = colors.Red,
                    onClick = {
                        // TODO: 신고 처리
                        isMenuBottomSheetVisible = false
                        selectedItemForMenu = null
                    }
                )
            )
        }

        MenuBottomSheet(
            items = menuItems,
            onDismiss = {
                isMenuBottomSheetVisible = false
                selectedItemForMenu = null
            }
        )
    }
}

@Preview
@Composable
private fun GroupNoteScreenPreview() {
    ThipTheme {
        GroupNoteScreen()
    }
}