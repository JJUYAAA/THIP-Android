package com.texthip.thip.ui.group.myroom.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.texthip.thip.R
import com.texthip.thip.ui.common.cards.CardItemRoom
import com.texthip.thip.ui.common.topappbar.DefaultTopAppBar
import com.texthip.thip.ui.group.done.mock.MyRoomCardData
import com.texthip.thip.ui.group.done.mock.getEndDateInDays
import com.texthip.thip.ui.group.done.mock.isRecruitingByType
import com.texthip.thip.ui.group.myroom.component.GroupMyRoomFilterRow
import com.texthip.thip.ui.group.myroom.mock.RoomType
import com.texthip.thip.ui.group.myroom.viewmodel.GroupMyViewModel
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupMyScreen(
    onCardClick: (MyRoomCardData) -> Unit = {},
    onNavigateBack: () -> Unit = {},
    viewModel: GroupMyViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    
    // 무한 스크롤 로직
    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItems = listState.layoutInfo.totalItemsCount
            lastVisibleIndex >= totalItems - 3
        }
    }
    
    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore && uiState.canLoadMore) {
            viewModel.loadMoreMyRooms()
        }
    }
    
    // Filter 상태를 
    val selectedStates = remember(uiState.currentRoomType) {
        when (uiState.currentRoomType) {
            RoomType.PLAYING -> booleanArrayOf(true, false)
            RoomType.RECRUITING -> booleanArrayOf(false, true)
            else -> booleanArrayOf(false, false) // playingAndRecruiting
        }
    }

    Column(
        Modifier
            .fillMaxSize()
    ) {
        DefaultTopAppBar(
            title = stringResource(R.string.my_group_room),
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
                Spacer(modifier = Modifier.height(20.dp))

                GroupMyRoomFilterRow(
                    selectedStates = selectedStates,
                    onToggle = { idx ->
                        val newRoomType = when {
                            // 진행중 버튼을 눌렀을 때
                            idx == 0 -> {
                                if (selectedStates[0]) {
                                    // 이미 선택된 상태면 전체로 변경
                                    RoomType.PLAYING_AND_RECRUITING
                                } else {
                                    // 선택되지 않은 상태면 진행중만
                                    RoomType.PLAYING
                                }
                            }
                            // 모집중 버튼을 눌렀을 때  
                            idx == 1 -> {
                                if (selectedStates[1]) {
                                    RoomType.PLAYING_AND_RECRUITING
                                } else {
                                    RoomType.RECRUITING
                                }
                            }
                            else -> RoomType.PLAYING_AND_RECRUITING
                        }
                        viewModel.changeRoomType(newRoomType)
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                if (uiState.myRooms.isNotEmpty()) {
                    LazyColumn(
                        state = listState,
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        contentPadding = PaddingValues(bottom = 20.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(uiState.myRooms) { room ->
                            CardItemRoom(
                                title = room.roomName,
                                participants = room.memberCount,
                                maxParticipants = room.recruitCount,
                                isRecruiting = room.isRecruitingByType(),
                                endDate = room.getEndDateInDays(),
                                imageUrl = room.bookImageUrl,
                                onClick = { onCardClick(room) }
                            )
                        }
                    }
                } else if (!uiState.isLoading) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.group_myroom_error_comment1),
                            color = colors.White,
                            style = typography.smalltitle_sb600_s18_h24
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = stringResource(R.string.group_myroom_error_comment2),
                            color = colors.Grey,
                            style = typography.copy_r400_s14
                        )
                    }
                }
            }
        }
    }
}

