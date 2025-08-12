package com.texthip.thip.ui.search.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.texthip.thip.R
import com.texthip.thip.ui.common.cards.CardItemRoom
import com.texthip.thip.ui.common.topappbar.DefaultTopAppBar
import com.texthip.thip.ui.group.myroom.mock.GroupCardItemRoomData
import com.texthip.thip.ui.search.viewmodel.SearchBookGroupViewModel
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography
import com.texthip.thip.utils.rooms.DateUtils


@Composable
fun SearchBookGroupScreen(
    isbn: String,
    onLeftClick: () -> Unit = {},
    onCardClick: (Int) -> Unit = {},
    onCreateRoomClick: () -> Unit = {},
    viewModel: SearchBookGroupViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(isbn) {
        viewModel.loadRecruitingRooms(isbn)
    }
    when {
        uiState.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = colors.White
                )
            }
        }
        uiState.error != null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = uiState.error!!,
                    color = colors.White,
                    style = typography.smalltitle_sb600_s16_h20
                )
            }
        }
        else -> {
            val recruitingList = uiState.recruitingRooms.map { item ->
                val daysLeft = DateUtils.extractDaysFromDeadline(item.deadlineEndDate)

                GroupCardItemRoomData(
                    id = item.roomId,
                    title = item.roomName,
                    participants = item.memberCount,
                    maxParticipants = item.recruitCount,
                    endDate = daysLeft,
                    imageUrl = item.bookImageUrl,
                    isRecruiting = true
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                // 기존 콘텐츠 Column
                Column(
                    Modifier.fillMaxSize()
                ) {
                    DefaultTopAppBar(
                        title = stringResource(R.string.group_recruiting_title),
                        onLeftClick = onLeftClick,
                    )

                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp)
                            .padding(top = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(
                                    R.string.group_searched_room_size,
                                    uiState.totalCount
                                ),
                                color = colors.Grey,
                                style = typography.menu_m500_s14_h24
                            )
                        }
                        Spacer(
                            modifier = Modifier
                                .padding(top = 4.dp, bottom = 20.dp)
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(colors.DarkGrey02)
                        )

                        if (recruitingList.isEmpty()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(bottom = 50.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = stringResource(R.string.book_recruiting_empty_message),
                                    color = colors.White,
                                    style = typography.smalltitle_sb600_s18_h24,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = stringResource(R.string.book_recruiting_empty_sub_message),
                                    color = colors.Grey,
                                    style = typography.feedcopy_r400_s14_h20,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        } else {
                            val listState = rememberLazyListState()
                            
                            // 무한 스크롤 로직
                            LaunchedEffect(listState) {
                                snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                                    .collect { lastVisibleIndex ->
                                        if (lastVisibleIndex != null && 
                                            lastVisibleIndex >= recruitingList.size - 3 && 
                                            uiState.canLoadMore) {
                                            viewModel.loadMoreRooms()
                                        }
                                    }
                            }
                            
                            LazyColumn(
                                state = listState,
                                verticalArrangement = Arrangement.spacedBy(20.dp),
                                contentPadding = PaddingValues(bottom = 80.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(recruitingList) { item ->
                                    CardItemRoom(
                                        title = item.title,
                                        participants = item.participants,
                                        maxParticipants = item.maxParticipants,
                                        isRecruiting = item.isRecruiting,
                                        endDate = item.endDate,
                                        imageUrl = item.imageUrl,
                                        onClick = { onCardClick(item.id) }
                                    )
                                }
                                
                                // 로딩 인디케이터
                                if (uiState.isLoadingMore) {
                                    item {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator(
                                                color = colors.White
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.Purple
                    ),
                    modifier = Modifier
                        .align(Alignment.BottomCenter) // Box의 하단 중앙에 정렬
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(0.dp),
                    onClick = onCreateRoomClick
                ) {
                    Text(
                        text = stringResource(R.string.group_recruiting_create_button),
                        style = typography.smalltitle_sb600_s18_h24,
                        color = colors.White
                    )
                }
            }
        }
    }
}


@Preview()
@Composable
fun GroupRecruitingScreenPreview() {
    ThipTheme {
        SearchBookGroupScreen(
            isbn = "9788954682152"
        )
    }
}