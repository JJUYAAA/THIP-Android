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
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.buttons.GroupVoteButton
import com.texthip.thip.ui.group.note.mock.VoteItem
import com.texthip.thip.ui.group.room.mock.VoteData
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun CardVote(
    voteData: List<VoteData>
) {
    val pageCount = voteData.size
    val pagerState = rememberPagerState(pageCount = { pageCount })

    // 각 페이지별 상태 기억: 선택 인덱스, 선택 여부 포함한 voteItems
    val selectedIndexes = remember { mutableStateMapOf<Int, Int?>() }
    val voteItemStates = remember {
        voteData.map { it.voteItems.toMutableStateList() }.toMutableStateList()
    }

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
            val voteItems = voteItemStates[page]
            val selectedIndex = selectedIndexes[page]
            val hasVoted = voteItems.any { it.isVoted }

            Column(
                modifier = Modifier.padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = voteData[page].description,
                    style = typography.info_m500_s12,
                    color = colors.White,
                )

                GroupVoteButton(
                    voteItems = voteItems,
                    selectedIndex = selectedIndex,
                    hasVoted = hasVoted,
                    onOptionSelected = { index ->
                        selectedIndexes[page] = if (selectedIndex == index) null else index

                        voteItemStates[page] = voteItems.mapIndexed { i, item ->
                            item.copy(isVoted = i == index && selectedIndex != index)
                        }.toMutableStateList()
                    }
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
            voteItems = listOf(
                VoteItem(1, "김땡땡", 50, false),
                VoteItem(2, "이땡땡", 10, false),
                VoteItem(3, "박땡땡", 20, false),
                VoteItem(4, "최땡땡", 15, false),
                VoteItem(5, "정땡땡", 5, false)
            )
        ),
        VoteData(
            description = "옆으로 넘긴 다른 투표 01",
            voteItems = listOf(
                VoteItem(1, "어쩌구", 25, false),
                VoteItem(2, "저쩌구", 45, false),
                VoteItem(3, "삼번", 20, false),
                VoteItem(4, "사번", 10, false)
            )
        ),
        VoteData(
            description = "옆으로 넘긴 다른 투표 02",
            voteItems = listOf(
                VoteItem(1, "투표 제목과 항목 버튼이 가로 스크롤되고", 40, false),
                VoteItem(2, "아래 캐러셀 닷은", 35, false),
                VoteItem(3, "위치 그대로, 강조점만 바뀌도록.", 25, false)
            )
        )
    )

    CardVote(voteData = mockVoteData)
}