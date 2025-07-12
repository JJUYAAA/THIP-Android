package com.texthip.thip.ui.group.myroom.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.group.myroom.mock.GroupCardData
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun GroupPager(
    groupCards: List<GroupCardData>,
    onCardClick: (GroupCardData) -> Unit
) {
    val scale = 0.86f
    val desiredGap = 10.dp

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(192.dp)
    ) {
        val horizontalPadding = 30.dp
        val cardWidth = maxWidth - (horizontalPadding * 2)

        val pageSpacing = with(this) {
            (-(cardWidth - (cardWidth * scale)) / 2f) + desiredGap
        }

        // 데이터가 없는 경우 Empty State 표시
        if (groupCards.isEmpty()) {
            GroupEmptyCard(
                modifier = Modifier.padding(horizontal = horizontalPadding)
            )
        } else if (groupCards.size == 1) {
            // 데이터가 하나일 때는 스크롤 없이 고정 카드 표시
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = horizontalPadding),
                contentAlignment = Alignment.Center
            ) {
                GroupMainCard(
                    data = groupCards[0],
                    onClick = { onCardClick(groupCards[0]) },
                    backgroundColor = colors.White
                )
            }
        } else {
            // 여러 데이터일 때는 무한 스크롤 적용
            val infinitePageCount = Int.MAX_VALUE
            val startPage = infinitePageCount / 2

            val pagerState = rememberPagerState(
                initialPage = startPage,
                pageCount = { infinitePageCount }
            )

            // 시작 페이지로 이동
            LaunchedEffect(groupCards.size) {
                pagerState.scrollToPage(startPage)
            }

            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(start = horizontalPadding, end = horizontalPadding),
                pageSpacing = pageSpacing,
                modifier = Modifier.fillMaxWidth()
            ) { page ->
                val actualIndex = ((page - startPage) % groupCards.size + groupCards.size) % groupCards.size
                val currentPageIndex = ((pagerState.currentPage - startPage) % groupCards.size + groupCards.size) % groupCards.size

                val isCurrent = actualIndex == currentPageIndex
                val scale = if (isCurrent) 1f else 0.86f

                // 현재 카드와 양옆 카드의 색상 설정
                val bgColor = when {
                    isCurrent -> colors.White
                    else -> colors.DarkGrey
                }

                Box(
                    modifier = Modifier
                        .width(cardWidth)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            alpha = if (isCurrent) 1f else 0.7f
                        }
                ) {
                    GroupMainCard(
                        data = groupCards[actualIndex],
                        onClick = { onCardClick(groupCards[actualIndex]) },
                        backgroundColor = bgColor
                    )
                }
            }

            // 페이지 인디케이터 였던 것 (혹시 몰라 남겨둡니다)
            /*val currentPageIndex = ((pagerState.currentPage - startPage) % groupCards.size + groupCards.size) % groupCards.size

            SimplePagerIndicator(
                pageCount = groupCards.size,
                currentPage = currentPageIndex,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(top = 12.dp)
            )*/
        }
    }
}


@Preview()
@Composable
fun PreviewMyGroupPager() {
    ThipTheme {
        val list = listOf(
            GroupCardData(
                title = "호르몬 체인지 완독하는 방",
                members = 22,
                imageRes = R.drawable.bookcover_sample,
                progress = 40,
                nickname = "uibowl1님"
            ),
            GroupCardData(
                title = "명작 읽기방",
                members = 10,
                imageRes = R.drawable.bookcover_sample,
                progress = 70,
                nickname = "joyce님"
            ),
            GroupCardData(
                title = "또 다른 방",
                members = 13,
                imageRes = R.drawable.bookcover_sample,
                progress = 10,
                nickname = "other님"
            )
        )
        GroupPager(groupCards = list, onCardClick = {})
    }
}

@Preview()
@Composable
fun PreviewSingleGroupPager() {
    ThipTheme {
        val singleList = listOf(
            GroupCardData(
                title = "단일 그룹",
                members = 15,
                imageRes = R.drawable.bookcover_sample,
                progress = 60,
                nickname = "single님"
            )
        )
        GroupPager(groupCards = singleList, onCardClick = {})
    }
}

@Preview()
@Composable
fun PreviewEmptyGroupPager() {
    ThipTheme {
        GroupPager(groupCards = emptyList(), onCardClick = {})
    }
}