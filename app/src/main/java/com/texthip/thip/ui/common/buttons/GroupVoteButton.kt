package com.texthip.thip.ui.common.buttons

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.ui.group.note.mock.VoteItem
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography


@Composable
fun GroupVoteButton(
    modifier: Modifier = Modifier,
    voteItems: List<VoteItem>,
    selectedIndex: Int?, // 선택한 인덱스
    hasVoted: Boolean = false, // 투표 여부
    onOptionSelected: (Int?) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        voteItems.forEachIndexed { index, item ->
            val isSelected = selectedIndex == index
            val votePercent = if (hasVoted) item.percentage.coerceIn(0, 100) else 0

            val animatedPercent by animateFloatAsState(
                targetValue = votePercent / 100f,
                animationSpec = tween(durationMillis = 500)
            )

            val backgroundColor by animateColorAsState(
                targetValue = if (isSelected) colors.PurpleDark else colors.DarkGrey
            )

            val percentBarColor by animateColorAsState(
                targetValue = if (isSelected) colors.Purple else colors.Grey02
            )

            val textColor by animateColorAsState(
                targetValue = if (isSelected) colors.NeonGreen else colors.White
            )

            val fontStyle = if (!hasVoted) typography.feedcopy_r400_s14_h20
            else typography.menu_sb600_s14_h24

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = backgroundColor, shape = RoundedCornerShape(12.dp))
                    .height(44.dp)
                    .clickable {
                        if (isSelected) {
                            onOptionSelected(null)
                        } else {
                            onOptionSelected(index)
                        }
                    }
            ) {
                if (hasVoted) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(animatedPercent)
                            .background(
                                color = percentBarColor,
                                shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
                            )
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${index + 1}. ${item.itemName}",
                        color = textColor,
                        style = fontStyle
                    )
                    if (hasVoted) {
                        Text(
                            text = "${item.percentage}%",
                            color = textColor,
                            style = fontStyle
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun GroupVoteButtonPreview() {
    var selected by remember { mutableStateOf<Int?>(null) }
    var voteItems by remember {
        mutableStateOf(
            listOf(
                VoteItem(1, "밥", 25, false),
                VoteItem(2, "국수", 35, false),
                VoteItem(3, "고기", 40, false)
            )
        )
    }

    val hasVoted = voteItems.any { it.isVoted }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        GroupVoteButton(
            voteItems = voteItems,
            selectedIndex = selected,
            hasVoted = hasVoted,
            onOptionSelected = {
                if (selected == it) {
                    // ✅ 이미 선택한 항목을 다시 클릭한 경우: 취소 처리
                    selected = null
                    voteItems = voteItems.map { it.copy(isVoted = false) }
                } else {
                    // ✅ 새로운 항목 선택
                    selected = it
                    voteItems = voteItems.mapIndexed { index, item ->
                        item.copy(isVoted = index == it)
                    }
                }
            }
        )
    }
}