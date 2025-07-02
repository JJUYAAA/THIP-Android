package com.texthip.thip.ui.group.myroom.component

import android.annotation.SuppressLint
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.cards.CardItemRoom
import com.texthip.thip.ui.group.myroom.mock.GroupRoomSectionData
import com.texthip.thip.ui.group.myroom.mock.GroupCardItemRoomData
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun GroupRoomDeadlineSection(
    roomSections: List<GroupRoomSectionData>,
    onRoomClick: (GroupCardItemRoomData) -> Unit
) {
    val cardWidth = 320.dp  // 카드 폭 원하는 값으로 맞추기!
    val pageSpacing = 12.dp

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { roomSections.size }
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(588.dp),
            contentAlignment = Alignment.Center
        ) {
            val horizontalPadding = ((maxWidth - cardWidth) / 2).coerceAtLeast(0.dp)

            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(horizontal = horizontalPadding),
                pageSpacing = pageSpacing,
                modifier = Modifier.fillMaxWidth()
            ) { page ->
                val section = roomSections[page]
                var selectedGenre by remember { mutableStateOf(0) }

                val isCurrent = pagerState.currentPage == page
                val scale = if (isCurrent) 1f else 0.9f

                Box(
                    modifier = Modifier
                        .width(cardWidth)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        }
                        .fillMaxHeight()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    colors.White.copy(0.25f),
                                    colors.Black.copy(0.2f)
                                )
                            ),
                            shape = RoundedCornerShape(14.dp)
                        )
                        .padding(vertical = 20.dp, horizontal = 12.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = section.title,
                            style = typography.title_b700_s20_h24,
                            color = colors.White
                        )
                        Spacer(Modifier.height(40.dp))

                        GenreChipRow(
                            genres = section.genres,
                            selectedIndex = selectedGenre,
                            onSelect = { idx -> selectedGenre = idx }
                        )
                        Spacer(Modifier.height(20.dp))

                        val cards = section.rooms.filter { it.genreIndex == selectedGenre }.take(3)
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
                                    hasBorder = true,
                                )
                            }
                            if (cards.size < 3) {
                                Spacer(
                                    modifier = Modifier
                                        .weight(1f, fill = true)
                                        .fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        }

        SimplePagerIndicator(
            pageCount = roomSections.size,
            currentPage = pagerState.currentPage,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp)
        )
    }
}

@Preview()
@Composable
fun PreviewGroupRoomPagerSection() {
    ThipTheme {
        val genres = listOf("문학", "과학·IT", "사회과학", "인문학", "예술")

        // 마감 임박한 독서 모임방
        val deadlineRooms = listOf(
            GroupCardItemRoomData(
                title = "시집만 읽는 사람들 3월",
                participants = 22,
                maxParticipants = 30,
                isRecruiting = true,
                endDate = 3,
                genreIndex = 0
            ),
            GroupCardItemRoomData(
                title = "일본 소설 좋아하는 사람들",
                participants = 15,
                maxParticipants = 20,
                isRecruiting = true,
                endDate = 2,
                genreIndex = 0
            ),
            GroupCardItemRoomData(
                title = "명작 같이 읽기방",
                participants = 22,
                maxParticipants = 30,
                isRecruiting = true,
                endDate = 3,
                genreIndex = 0
            ),
            GroupCardItemRoomData(
                title = "물리책 읽는 방",
                participants = 13,
                maxParticipants = 20,
                isRecruiting = true,
                endDate = 1,
                genreIndex = 1
            )
        )

        // 인기 있는 독서 모임방
        val popularRooms = listOf(
            GroupCardItemRoomData(
                title = "베스트셀러 토론방",
                participants = 28,
                maxParticipants = 30,
                isRecruiting = true,
                endDate = 7,
                genreIndex = 0
            ),
            GroupCardItemRoomData(
                title = "인기 소설 완독방",
                participants = 25,
                maxParticipants = 25,
                isRecruiting = false,
                endDate = 5,
                genreIndex = 0
            ),
            GroupCardItemRoomData(
                title = "트렌드 과학서 읽기",
                participants = 20,
                maxParticipants = 25,
                isRecruiting = true,
                endDate = 10,
                genreIndex = 1
            )
        )

        // 인플루언서, 작가 독서 모임방
        val influencerRooms = listOf(
            GroupCardItemRoomData(
                title = "작가와 함께하는 독서방",
                participants = 30,
                maxParticipants = 30,
                isRecruiting = false,
                endDate = 14,
                genreIndex = 0
            ),
            GroupCardItemRoomData(
                title = "유명 북튜버와 읽기",
                participants = 18,
                maxParticipants = 20,
                isRecruiting = true,
                endDate = 8,
                genreIndex = 2
            ),
            GroupCardItemRoomData(
                title = "작가 초청 인문학방",
                participants = 15,
                maxParticipants = 20,
                isRecruiting = true,
                endDate = 12,
                genreIndex = 3
            )
        )

        val roomSections = listOf(
            GroupRoomSectionData(
                title = stringResource(R.string.deadlineString),
                rooms = deadlineRooms,
                genres = genres
            ),
            GroupRoomSectionData(
                title = "인기 있는 독서 모임방",
                rooms = popularRooms,
                genres = genres
            ),
            GroupRoomSectionData(
                title = "인플루언서·작가 독서 모임방",
                rooms = influencerRooms,
                genres = genres
            )
        )

        GroupRoomDeadlineSection(
            roomSections = roomSections,
            onRoomClick = {}
        )
    }
}
