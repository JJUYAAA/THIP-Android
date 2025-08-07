package com.texthip.thip.ui.common.cards

import CurrentVote
import VoteItem
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun CardVote(
    voteData: List<CurrentVote>,
    onVoteClick: (CurrentVote) -> Unit = {}
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

        if (voteData.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .padding(vertical = 60.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.no_vote),
                    style = typography.info_m500_s12,
                    color = colors.Grey
                )
            }
        } else {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth()
            ) { page ->
                val vote = voteData[page]

                Column(
                    modifier = Modifier
                        .height(176.dp)
                        .padding(horizontal = 12.dp)
                        .clickable { onVoteClick(vote) }, // 전체 클릭 시 이동
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = vote.content,
                        style = typography.info_m500_s12,
                        color = colors.White,
                    )

                    vote.voteItems.forEachIndexed { index, item ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = colors.DarkGrey,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .height(44.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = "${index + 1}. ${item.itemName}",
                                    color = colors.White,
                                    style = typography.feedcopy_r400_s14_h20
                                )
                            }
                        }
                    }
                }
            }
        }

        Row(
            Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pageCount) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) colors.White else colors.Grey02
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CardVote(
            voteData = listOf(
                CurrentVote(
                    content = "3연에 나오는 심장은 무엇을 의미하는 걸까요?",
                    page = 12,
                    isOverview = false,
                    voteItems = listOf(
                        VoteItem("사랑"),
                        VoteItem("희생"),
                        VoteItem("고통"),
                    )
                ),
                CurrentVote(
                    content = "가장 인상 깊었던 구절은 무엇인가요?",
                    page = 25,
                    isOverview = false,
                    voteItems = listOf(
                        VoteItem("1연 1행"),
                        VoteItem("2연 3행"),
                    )
                )
            )
        )
    }
}