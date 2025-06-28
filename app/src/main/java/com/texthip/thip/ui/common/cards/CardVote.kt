package com.texthip.thip.ui.common.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.buttons.GroupVoteButton
import com.texthip.thip.ui.group.screen.room.mock.VoteData
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun CardVote(
    voteData: List<VoteData>
) {
    val pageCount = voteData.size
    val pagerState = rememberPagerState(pageCount = { pageCount })

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colors.DarkGrey02, shape = RoundedCornerShape(12.dp))
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = stringResource(R.string.vote_title),
            style = typography.smalltitle_sb600_s16_h24,
            color = colors.White,
            modifier = Modifier.padding(horizontal = 12.dp)
        )


        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            val item = voteData[page]

            Column(
                modifier = Modifier.padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = item.description,
                    style = typography.info_m500_s12,
                    color = colors.White,
                )

                GroupVoteButton(
                    options = item.options,
                    voteResults = item.votes
                )
            }
        }

        Row(
            Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) colors.White else colors.Grey02
                Box(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .background(color, CircleShape)
                        .size(4.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun CardVotePreview() {
    val mockVoteData = listOf(
        VoteData(
            description = "투표 내용입니다...",
            options = listOf("김땡땡", "이땡땡", "박땡땡", "최땡땡", "정땡땡"),
            votes = listOf(50, 10, 20, 15, 5)
        ),
        VoteData(
            description = "옆으로 넘긴 다른 투표 01",
            options = listOf("어쩌구", "저쩌구", "삼번", "사번"),
            votes = listOf(25, 45, 20, 10)
        ),
        VoteData(
            description = "옆으로 넘긴 다른 투표 02",
            options = listOf("투표 제목과 항목 버튼이 가로 스크롤되고", "아래 캐러셀 닷은", "위치 그대로, 강조점만 바뀌도록."),
            votes = listOf(40, 35, 25)
        )
    )

    CardVote(voteData = mockVoteData)
}