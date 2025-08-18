package com.texthip.thip.ui.group.search.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.ui.common.cards.CardItemRoomSmall
import com.texthip.thip.data.model.rooms.response.SearchRoomItem
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors

@Composable
fun GroupLiveSearchResult(
    roomList: List<SearchRoomItem>,
    onRoomClick: (SearchRoomItem) -> Unit = {},
    canLoadMore: Boolean = false,
    isLoadingMore: Boolean = false,
    onLoadMore: () -> Unit = {}
) {
    val listState = rememberLazyListState()

    // 무한 스크롤 트리거 감지
    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem != null && lastVisibleItem.index >= roomList.size - 3 && canLoadMore
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            onLoadMore()
        }
    }

    LazyColumn(state = listState) {
        itemsIndexed(roomList) { index, room ->
            CardItemRoomSmall(
                title = room.roomName,
                participants = room.memberCount,
                maxParticipants = room.recruitCount,
                endDate = room.deadlineDate,
                imageUrl = room.bookImageUrl,
                isWide = true,
                isSecret = !room.isPublic,
                onClick = { onRoomClick(room) }
            )
            if (index < roomList.size - 1) {
                Spacer(
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 12.dp)
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(colors.DarkGrey02)
                )
            }
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
                    CircularProgressIndicator(color = colors.White)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GroupLiveSearchResultPreview() {
    ThipTheme {
        Box(
            modifier = Modifier
                .padding(16.dp)
        ) {
            GroupLiveSearchResult(
                roomList = listOf(
                    SearchRoomItem(
                        roomId = 1,
                        roomName = "해리포터 독서모임",
                        memberCount = 5,
                        recruitCount = 10,
                        deadlineDate = "7일 뒤",
                        bookImageUrl = null,
                        isPublic = true
                    ),
                    SearchRoomItem(
                        roomId = 2,
                        roomName = "소설 읽기 모임",
                        memberCount = 8,
                        recruitCount = 12,
                        deadlineDate = "3일 뒤",
                        bookImageUrl = null,
                        isPublic = false
                    ),
                    SearchRoomItem(
                        roomId = 3,
                        roomName = "비즈니스 서적 스터디",
                        memberCount = 3,
                        recruitCount = 8,
                        deadlineDate = "모집 중",
                        bookImageUrl = null,
                        isPublic = true
                    )
                )
            )
        }
    }
}
