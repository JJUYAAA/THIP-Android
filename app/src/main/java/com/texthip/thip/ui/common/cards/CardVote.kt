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
import com.texthip.thip.ui.group.room.mock.VoteData
import com.texthip.thip.ui.group.room.mock.mockVoteData
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
    CardVote(voteData = mockVoteData)
}