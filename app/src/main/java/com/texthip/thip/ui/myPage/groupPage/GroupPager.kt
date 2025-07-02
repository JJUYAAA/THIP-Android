package com.texthip.thip.ui.myPage.groupPage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.ui.theme.ThipTheme.colors

@Composable
fun GroupPager(
    groupCards: List<GroupCardData>,
    onCardClick: (GroupCardData) -> Unit
) {
    // Pager 상태
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { maxOf(1, groupCards.size) }
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(208.dp),
        contentAlignment = Alignment.Center
    ) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 20.dp), // peek 공간
            pageSpacing = (-10).dp,
            modifier = Modifier.fillMaxWidth()
        ) { page ->

            // 페이지와 현재페이지의 거리(0이면 중앙, 1/-1이면 peek)
            val distanceFromCenter = (pagerState.currentPage - page).toFloat()
            // scale 계산 (중앙은 1f, peek은 0.93~0.97f)
            val scale = if (pagerState.currentPage == page) 1f else 0.86f
            // 색상 변경 (peek이면 밝은 회색, 중앙이면 흰색)
            val bgColor = if (pagerState.currentPage == page) colors.White else colors.DarkGrey
            // alpha(살짝 흐리게)도 가능

            Box(
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        alpha = if (pagerState.currentPage == page) 1f else 0.7f
                    }
            ) {
                GroupMainCard(
                    data = groupCards[page],
                    onClick = { onCardClick(groupCards[page]) },
                    backgroundColor = bgColor // Card에서 배경색 파라미터 추가
                )
            }
        }
        // 아래 Indicator
        SimplePagerIndicator(
            pageCount = groupCards.size,
            currentPage = pagerState.currentPage,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(top = 12.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, widthDp = 360, heightDp = 220)
@Composable
fun PreviewMyGroupPager() {
    val list = listOf(
        GroupCardData(
            title = "호르몬 체인지 완독하는 방",
            members = 22,
            imageRes = com.texthip.thip.R.drawable.bookcover_sample,
            progress = 40,
            nickname = "uibowl1님"
        ),
        GroupCardData(
            title = "명작 읽기방",
            members = 10,
            imageRes = com.texthip.thip.R.drawable.bookcover_sample,
            progress = 70,
            nickname = "joyce님"
        ),
        GroupCardData(
            title = "또 다른 방",
            members = 13,
            imageRes = com.texthip.thip.R.drawable.bookcover_sample,
            progress = 10,
            nickname = "other님"
        )
    )
    GroupPager(groupCards = list, onCardClick = {})
}

