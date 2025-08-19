package com.texthip.thip.ui.signin.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.topappbar.InputTopAppBar
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography
import kotlinx.coroutines.launch

// 튜토리얼 각 페이지에 표시될 데이터 클래스
private data class TutorialPage(
    val title: Int,
    val subtitle: Int,
    val imageRes: Int
)

// 튜토리얼 6개 화면에 대한 데이터
private val tutorialPages = listOf(
    TutorialPage(R.string.feed, R.string.tutorial_1, R.drawable.img_tutorial_1),
    TutorialPage(R.string.feed, R.string.tutorial_2, R.drawable.img_tutorial_2),
    TutorialPage(R.string.nav_group, R.string.tutorial_3, R.drawable.img_tutorial_3),
    TutorialPage(R.string.nav_group, R.string.tutorial_4, R.drawable.img_tutorial_4),
    TutorialPage(R.string.thip_plus, R.string.tutorial_5, R.drawable.img_tutorial_5),
    TutorialPage(R.string.thip_plus, R.string.tutorial_6, R.drawable.img_tutorial_6)
)


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TutorialScreen(
    onFinish: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { tutorialPages.size })
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        InputTopAppBar(
            title = "",
            rightButtonName = stringResource(R.string.next),
            isLeftIconVisible = false,
            isRightButtonEnabled = true,
            onRightClick = {
                scope.launch {
                    if (pagerState.currentPage < tutorialPages.lastIndex) {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    } else {
                        onFinish()
                    }
                }
            }
        )

        // 화면 콘텐츠 영역
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.padding(top = 10.dp).weight(1f)// 남은 공간을 모두 차지하도록 weight 설정
            ) { pageIndex ->
                TutorialPageContent(page = tutorialPages[pageIndex])
            }
            Spacer(modifier = Modifier.height(24.dp))
            PageIndicator(
                pageCount = tutorialPages.size,
                currentPage = pagerState.currentPage
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = stringResource(R.string.skip),
                style = typography.info_r400_s12,
                color = colors.Grey01,
                modifier = Modifier
                    .padding(bottom = 62.dp)
                    .clickable { onFinish() }
            )
        }
    }
}

@Composable
private fun TutorialPageContent(page: TutorialPage) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = page.title),
            style = typography.title_b700_s20_h24,
            color = colors.White
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(id = page.subtitle),
            style = typography.smalltitle_sb600_s16_h24,
            color = colors.Grey,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(40.dp))
        Image(
            painter = painterResource(id = page.imageRes),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun PageIndicator(pageCount: Int, currentPage: Int) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            val color = if (currentPage == index) colors.White else colors.Grey02
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TutorialScreenPreview() {
    ThipTheme {
        TutorialScreen(onFinish = {})
    }
}