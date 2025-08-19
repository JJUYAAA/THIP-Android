package com.texthip.thip.ui.group.room.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.texthip.thip.R
import com.texthip.thip.data.model.rooms.response.TodayCommentList
import com.texthip.thip.ui.common.bottomsheet.MenuBottomSheet
import com.texthip.thip.ui.common.cards.CardCommentGroup
import com.texthip.thip.ui.common.forms.CommentTextField
import com.texthip.thip.ui.common.modal.ToastWithDate
import com.texthip.thip.ui.common.topappbar.DefaultTopAppBar
import com.texthip.thip.ui.common.view.CountingBar
import com.texthip.thip.ui.group.room.mock.MenuBottomSheetItem
import com.texthip.thip.ui.group.room.viewmodel.GroupRoomChatEvent
import com.texthip.thip.ui.group.room.viewmodel.GroupRoomChatUiState
import com.texthip.thip.ui.group.room.viewmodel.GroupRoomChatViewModel
import com.texthip.thip.ui.group.room.viewmodel.ToastType
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography
import com.texthip.thip.utils.rooms.advancedImePadding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@Composable
fun GroupRoomChatScreen(
    onBackClick: () -> Unit,
    viewModel: GroupRoomChatViewModel = hiltViewModel()
) {
    var inputText by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    var activeToast by remember { mutableStateOf<ToastType?>(null) }

    LaunchedEffect(key1 = Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is GroupRoomChatEvent.ShowToast -> {
                    activeToast = event.type
                }

                else -> Unit
            }
        }
    }

    GroupRoomChatContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        inputText = inputText,
        onInputTextChanged = { newText -> inputText = newText },
        onSendClick = {
            viewModel.postDailyGreeting(inputText)
            inputText = ""
        },
        onNavigateBack = onBackClick,
        activeToast = activeToast,
        onDismissToast = { activeToast = null }
    )
}

@Composable
fun GroupRoomChatContent(
    uiState: GroupRoomChatUiState,
    onEvent: (GroupRoomChatEvent) -> Unit,
    inputText: String,
    onInputTextChanged: (String) -> Unit,
    onSendClick: () -> Unit,
    onNavigateBack: () -> Unit,
    activeToast: ToastType?,
    onDismissToast: () -> Unit
) {
    var isBottomSheetVisible by remember { mutableStateOf(false) }
    var selectedMessage by remember { mutableStateOf<TodayCommentList?>(null) }
    val lazyListState = rememberLazyListState()

    LaunchedEffect(key1 = uiState.greetings) {
        if (uiState.greetings.isNotEmpty()) {
            lazyListState.animateScrollToItem(index = 0)
        }
    }

    val isScrolledToTop by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex == 0 && lazyListState.firstVisibleItemScrollOffset == 0
        }
    }

    LaunchedEffect(isScrolledToTop) {
        if (isScrolledToTop && !uiState.isLoadingMore && !uiState.isLastPage) {
            onEvent(GroupRoomChatEvent.LoadMore)
        }
    }

    Box(
        if (isBottomSheetVisible) {
            Modifier
                .fillMaxSize()
                .blur(5.dp)
        } else {
            Modifier.fillMaxSize()
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .advancedImePadding()
        ) {
            DefaultTopAppBar(
                title = stringResource(R.string.group_room_chat),
                onLeftClick = onNavigateBack,
            )

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = colors.White)
                }
            } else if (uiState.greetings.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.group_room_no_chat_title),
                            style = typography.smalltitle_sb600_s18_h24,
                            color = colors.White
                        )
                        Text(
                            text = stringResource(R.string.group_room_no_chat_content),
                            style = typography.copy_r400_s14,
                            color = colors.Grey
                        )
                    }
                }
            } else {
                LazyColumn(
                    state = lazyListState,
                    reverseLayout = true,
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.Bottom)
                ) {
                    if (uiState.isLoadingMore) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                            }
                        }
                    }

                    itemsIndexed(
                        uiState.greetings,
                        key = { _, item -> item.attendanceCheckId }) { index, message ->
                        val isNewDate = when {
                            index == uiState.greetings.lastIndex -> true
                            uiState.greetings[index + 1].date != message.date -> true
                            else -> false
                        }
                        val isBottomItem = index == 0

                        Column(
                            verticalArrangement = Arrangement.spacedBy(20.dp),
                            modifier = if (isBottomItem) Modifier.padding(bottom = 20.dp) else Modifier
                        ) {
                            if (isNewDate) {
                                HorizontalDivider(
                                    color = colors.DarkGrey03,
                                    thickness = 10.dp
                                )
                                CountingBar(
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    content = message.date
                                )
                            }

                            CardCommentGroup(
                                data = message,
                                onMenuClick = {
                                    selectedMessage = message
                                    isBottomSheetVisible = true
                                }
                            )
                        }
                    }
                }
            }

            CommentTextField(
                input = inputText,
                hint = stringResource(R.string.group_room_chat_hint),
                onInputChange = onInputTextChanged,
                onSendClick = onSendClick
            )
        }

        AnimatedVisibility(
            visible = activeToast != null,
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
            LaunchedEffect(activeToast) {
                if (activeToast != null) {
                    delay(3000L)
                    onDismissToast()
                }
            }

            when (activeToast) {
                ToastType.DAILY_GREETING_LIMIT -> {
                    ToastWithDate(color = colors.Red)
                }

                ToastType.FIRST_WRITE -> {
                    ToastWithDate()
                }

                null -> {}
            }
        }
    }

    if (isBottomSheetVisible && selectedMessage != null) {
        val menuItems = if (selectedMessage!!.isWriter) {
            listOf(
                MenuBottomSheetItem(
                    text = stringResource(R.string.delete),
                    color = colors.Red,
                    onClick = {
                        // TODO: 삭제 처리
                        isBottomSheetVisible = false
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
                        isBottomSheetVisible = false
                    }
                )
            )
        }

        MenuBottomSheet(
            items = menuItems,
            onDismiss = {
                isBottomSheetVisible = false
                selectedMessage = null
            }
        )
    }
}

@Preview
@Composable
private fun GroupRoomChatScreenPreview() {
    ThipTheme {
        var inputText by remember { mutableStateOf("") }
        GroupRoomChatContent(
            uiState = GroupRoomChatUiState(
                isLoading = false,
                greetings = listOf(
                    TodayCommentList(
                        attendanceCheckId = 3,
                        creatorId = 3,
                        creatorProfileImageUrl = "",
                        creatorNickname = "user.03",
                        todayComment = "세 번째 메시지입니다. 오늘 날씨가 좋네요.",
                        postDate = "10분 전",
                        date = "2025년 8월 18일",
                        isWriter = false
                    ),
                )
            ),
            onEvent = {},
            inputText = inputText,
            onInputTextChanged = { newText -> inputText = newText },
            onSendClick = {},
            onNavigateBack = {},
            activeToast = null,
            onDismissToast = {}
        )
    }
}