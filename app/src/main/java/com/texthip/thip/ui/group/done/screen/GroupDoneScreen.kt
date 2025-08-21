package com.texthip.thip.ui.group.done.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.texthip.thip.R
import com.texthip.thip.data.model.rooms.response.MyRoomResponse
import com.texthip.thip.ui.common.cards.CardItemRoom
import com.texthip.thip.ui.common.topappbar.DefaultTopAppBar
import com.texthip.thip.ui.group.done.viewmodel.GroupDoneUiState
import com.texthip.thip.ui.group.done.viewmodel.GroupDoneViewModel
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography
import com.texthip.thip.utils.rooms.RoomUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDoneScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: GroupDoneViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    GroupDoneContent(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onRefresh = { viewModel.refreshData() },
        onLoadMore = { viewModel.loadMoreExpiredRooms() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDoneContent(
    uiState: GroupDoneUiState,
    onNavigateBack: () -> Unit = {},
    onRefresh: () -> Unit = {},
    onLoadMore: () -> Unit = {}
) {
    val listState = rememberLazyListState()

    // 무한 스크롤을 위한 로직
    val shouldLoadMore by remember(uiState.canLoadMore, uiState.isLoadingMore) {
        derivedStateOf {
            val lastVisibleIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItems = listState.layoutInfo.totalItemsCount
            uiState.canLoadMore && !uiState.isLoadingMore && totalItems > 0 && lastVisibleIndex >= totalItems - 3
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            onLoadMore()
        }
    }

    Column(
        Modifier.fillMaxSize()
    ) {
        DefaultTopAppBar(
            title = stringResource(R.string.group_done_title),
            onLeftClick = onNavigateBack,
        )
        
        PullToRefreshBox(
            isRefreshing = uiState.isLoading,
            onRefresh = onRefresh,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                Modifier
                    .background(colors.Black)
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                LazyColumn(
                    state = listState,
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    contentPadding = PaddingValues(bottom = 20.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp)
                ) {
                    item {
                        Text(
                            text = stringResource(R.string.group_done_user_comment, uiState.userName),
                            color = colors.White,
                            style = typography.menu_r400_s14_h24
                        )
                    }

                    items(uiState.expiredRooms) { room ->
                        CardItemRoom(
                            title = room.roomName,
                            imageUrl = room.bookImageUrl,
                            participants = room.memberCount,
                            maxParticipants = room.recruitCount, // 모집 인원 수 사용
                            isRecruiting = RoomUtils.isRecruitingByType(room.type),
                            isSecret = !room.isPublic,
                            onClick = { /* 완료된 모임방은 클릭 불가 */ }
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun GroupDoneScreenPreview() {
    ThipTheme {
        GroupDoneContent(
            uiState = GroupDoneUiState(
                userName = "김독서",
                expiredRooms = listOf(
                    MyRoomResponse(
                        roomId = 1,
                        roomName = "🌙 미드나이트 라이브러리 함께읽기",
                        bookImageUrl = "https://picsum.photos/300/400?1",
                        memberCount = 18,
                        recruitCount = 20,
                        endDate = "2025-01-31",
                        type = "EXPIRED",
                        isPublic = true
                    ),
                    MyRoomResponse(
                        roomId = 2,
                        roomName = "📚 현대문학 깊이읽기 모임",
                        bookImageUrl = "https://picsum.photos/300/400?2",
                        memberCount = 12,
                        recruitCount = 15,
                        endDate = "2024-12-28",
                        type = "EXPIRED",
                        isPublic = false
                    ),
                    MyRoomResponse(
                        roomId = 3,
                        roomName = "🔬 과학책으로 세상보기",
                        bookImageUrl = "https://picsum.photos/300/400?3",
                        memberCount = 25,
                        recruitCount = 30,
                        endDate = "2024-12-15",
                        type = "EXPIRED",
                        isPublic = true
                    ),
                    MyRoomResponse(
                        roomId = 4,
                        roomName = "✨ 철학 고전 탐구하기",
                        bookImageUrl = "https://picsum.photos/300/400?4",
                        memberCount = 10,
                        recruitCount = 12,
                        endDate = "2024-11-20",
                        type = "EXPIRED",
                        isPublic = true
                    ),
                    MyRoomResponse(
                        roomId = 5,
                        roomName = "🎨 예술과 문학의 만남",
                        bookImageUrl = "https://picsum.photos/300/400?5",
                        memberCount = 16,
                        recruitCount = 20,
                        endDate = "2024-10-31",
                        type = "EXPIRED",
                        isPublic = false
                    )
                ),
                isLoading = false,
                hasMore = true
            )
        )
    }
}

