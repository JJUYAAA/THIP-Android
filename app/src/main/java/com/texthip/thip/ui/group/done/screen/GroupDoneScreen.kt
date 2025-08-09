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
import com.texthip.thip.ui.common.cards.CardItemRoom
import com.texthip.thip.ui.common.topappbar.DefaultTopAppBar
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
    val listState = rememberLazyListState()

    // 무한 스크롤을 위한 로직
    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItems = listState.layoutInfo.totalItemsCount
            lastVisibleIndex >= totalItems - 3 // 마지막 3개 아이템에 도달했을 때
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore && uiState.canLoadMore) {
            viewModel.loadMoreExpiredRooms()
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
            onRefresh = { viewModel.refreshData() },
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
        // Preview에서는 ViewModel을 사용할 수 없으므로 기본 레이아웃만 표시
        GroupDoneScreen()
    }
}

