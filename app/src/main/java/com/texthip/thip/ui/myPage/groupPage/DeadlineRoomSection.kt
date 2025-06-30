package com.texthip.thip.ui.myPage.groupPage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.ui.common.cards.CardItemRoom
import com.texthip.thip.ui.myPage.myGroup.CardItemRoomData
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun DeadlineRoomSection(
    rooms: List<CardItemRoomData>,
    selectedGenre: Int,
    onRoomClick: (CardItemRoomData) -> Unit,
    genres: List<String>,
    onGenreSelect: (Int) -> Unit
) {
    val pagerState = rememberPagerState(initialPage = selectedGenre, pageCount = { genres.size })

    // pager <-> chip 연동
    LaunchedEffect(pagerState.currentPage) {
        if (selectedGenre != pagerState.currentPage) {
            onGenreSelect(pagerState.currentPage)
        }
    }
    LaunchedEffect(selectedGenre) {
        if (pagerState.currentPage != selectedGenre) {
            pagerState.scrollToPage(selectedGenre)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(colors.Grey03, colors.DarkGrey, colors.Black)
                    ),
                    shape = RoundedCornerShape(14.dp)
                )
                .padding(vertical = 20.dp, horizontal = 12.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "마감 임박한 독서 모임방",
                    style = typography.title_b700_s20_h24,
                    color = colors.White
                )
                Spacer(Modifier.height(40.dp))
                GenreChipRow(
                    genres = genres,
                    selectedIndex = selectedGenre,
                    onSelect = { idx -> onGenreSelect(idx) }
                )
                Spacer(Modifier.height(20.dp))

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(434.dp),
                    pageSpacing = 20.dp // 각 페이지(장르) 사이의 간격!
                ) { page ->
                    // page == genreIndex
                    val cards = rooms.filter { it.genreIndex == page }.take(3)
                    Column(
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        cards.forEach { room ->
                            CardItemRoom(
                                title = room.title,
                                participants = room.participants,
                                maxParticipants = room.maxParticipants,
                                isRecruiting = room.isRecruiting,
                                endDate = room.endDate,
                                imageRes = room.imageRes,
                                onClick = { onRoomClick(room) },
                                hasBorder = true, // 카드에 테두리 추가
                            )
                        }
                        // 카드가 3개보다 적을 때 Spacer를 아래에 추가
                        if (cards.size < 3) {
                            Spacer(
                                modifier = Modifier
                                    .weight(1f, fill = true)
                                    .fillMaxWidth()
                            )
                        }
                    }
                }
                // 아래 인디케이터 (장르 개수만큼)
                SimplePagerIndicator(
                    pageCount = genres.size,
                    currentPage = pagerState.currentPage,
                    modifier = Modifier
                        .padding(top = 28.dp)
                )
            }
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFF000000, widthDp = 360, heightDp = 720)
@Composable
fun PreviewDeadlineRoomSection() {
    val genres = listOf("문학", "과학·IT", "사회과학", "인문학", "예술")
    val rooms = listOf(
        CardItemRoomData(
            title = "시집만 읽는 사람들 3월",
            participants = 22,
            maxParticipants = 30,
            isRecruiting = true,
            endDate = 3,
            genreIndex = 0
        ),
        CardItemRoomData(
            title = "일본 소설 좋아하는 사람들",
            participants = 15,
            maxParticipants = 20,
            isRecruiting = true,
            endDate = 2,
            genreIndex = 0
        ),
        CardItemRoomData(
            title = "명작 같이 읽기방",
            participants = 22,
            maxParticipants = 30,
            isRecruiting = true,
            endDate = 3,
            genreIndex = 0
        ),
        CardItemRoomData(
            title = "물리책 읽는 방",
            participants = 13,
            maxParticipants = 20,
            isRecruiting = true,
            endDate = 1,
            genreIndex = 1
        ),
        CardItemRoomData(
            title = "코딩 과학 동아리",
            participants = 12,
            maxParticipants = 15,
            isRecruiting = true,
            endDate = 5,
            genreIndex = 1
        ),
        CardItemRoomData(
            title = "사회과학 인문 탐구",
            participants = 8,
            maxParticipants = 12,
            isRecruiting = true,
            endDate = 4,
            genreIndex = 2
        ),
    )
    var selectedGenre by remember { mutableStateOf(0) }
    DeadlineRoomSection(
        rooms = rooms,
        selectedGenre = selectedGenre,
        onRoomClick = {},
        genres = genres,
        onGenreSelect = { selectedGenre = it }
    )
}

