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
    onCreateRoomClick: (isbn: String, title: String, imageUrl: String, author: String) -> Unit = { _, _, _, _ -> },
    viewModel: SearchBookGroupViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(isbn) {
        viewModel.loadRecruitingRooms(isbn)
    }

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

    SearchBookGroupScreenContent(
        isLoading = uiState.isLoading,
        error = uiState.error,
        recruitingList = recruitingList,
        totalCount = uiState.totalCount,
        isLoadingMore = uiState.isLoadingMore,
        canLoadMore = uiState.canLoadMore,
        isbn = isbn,
        bookTitle = uiState.bookDetail?.title ?: "",
        bookImageUrl = uiState.bookDetail?.imageUrl ?: "",
        bookAuthor = uiState.bookDetail?.authorName ?: "",
        onLeftClick = onLeftClick,
        onCardClick = onCardClick,
        onCreateRoomClick = onCreateRoomClick,
        onLoadMore = {
            viewModel.loadMoreRooms()
        }
    )
}

@Composable
private fun SearchBookGroupScreenContent(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    error: String? = null,
    recruitingList: List<GroupCardItemRoomData> = emptyList(),
    totalCount: Int = 0,
    isLoadingMore: Boolean = false,
    canLoadMore: Boolean = true,
    isbn: String = "",
    bookTitle: String = "",
    bookImageUrl: String = "",
    bookAuthor: String = "",
    onLeftClick: () -> Unit = {},
    onCardClick: (Int) -> Unit = {},
    onCreateRoomClick: (isbn: String, title: String, imageUrl: String, author: String) -> Unit = { _, _, _, _ -> },
    onLoadMore: () -> Unit = {}
) {
    when {
        isLoading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = colors.White
                )
            }
        }
        error != null -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = error,
                    color = colors.White,
                    style = typography.smalltitle_sb600_s16_h20
                )
            }
        }
        else -> {
            Box(
                modifier = modifier.fillMaxSize()
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
                                    totalCount
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
                            LaunchedEffect(listState, canLoadMore, isLoadingMore) {
                                snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                                    .collect { lastVisibleIndex ->
                                        if (lastVisibleIndex != null && 
                                            recruitingList.isNotEmpty() &&
                                            !isLoadingMore &&
                                            lastVisibleIndex >= recruitingList.size - 3 && 
                                            canLoadMore) {
                                            onLoadMore()
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
                                if (isLoadingMore) {
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
                    onClick = { 
                        onCreateRoomClick(isbn, bookTitle, bookImageUrl, bookAuthor)
                    }
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

// Preview용 Mock 데이터
private val mockRecruitingList = listOf(
    GroupCardItemRoomData(
        id = 1,
        title = "데미안 함께 읽기 📚",
        participants = 8,
        maxParticipants = 12,
        isRecruiting = true,
        endDate = 3,
        imageUrl = "https://example.com/demian.jpg",
        isSecret = false
    ),
    GroupCardItemRoomData(
        id = 2,
        title = "헤르만 헤세 작품 토론방",
        participants = 15,
        maxParticipants = 20,
        isRecruiting = true,
        endDate = 7,
        imageUrl = "https://example.com/demian.jpg",
        isSecret = true
    ),
    GroupCardItemRoomData(
        id = 3,
        title = "클래식 문학 읽기 모임",
        participants = 5,
        maxParticipants = 10,
        isRecruiting = true,
        endDate = 1,
        imageUrl = "https://example.com/demian.jpg",
        isSecret = false
    )
)

@Preview(showBackground = true)
@Composable
fun SearchBookGroupScreenContentPreview() {
    ThipTheme {
        SearchBookGroupScreenContent(
            recruitingList = mockRecruitingList,
            totalCount = 8
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SearchBookGroupScreenContentEmptyPreview() {
    ThipTheme {
        SearchBookGroupScreenContent(
            recruitingList = emptyList(),
            totalCount = 0
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SearchBookGroupScreenContentLoadingMorePreview() {
    ThipTheme {
        SearchBookGroupScreenContent(
            recruitingList = mockRecruitingList,
            totalCount = 15,
            isLoadingMore = true,
            canLoadMore = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SearchBookGroupScreenContentLoadingPreview() {
    ThipTheme {
        SearchBookGroupScreenContent(
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SearchBookGroupScreenContentErrorPreview() {
    ThipTheme {
        SearchBookGroupScreenContent(
            error = "모집 중인 그룹을 불러오는데 실패했습니다."
        )
    }
}