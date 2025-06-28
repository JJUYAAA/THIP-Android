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
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography


@Composable
fun GroupVoteButton(
    modifier: Modifier = Modifier,
    options: List<String>,
    voteResults: List<Int>
) {
    var selectedIndex by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        options.forEachIndexed { index, option ->
            val isSelected = selectedIndex == index
            val hasVoted = selectedIndex != null
            val votePercent = if (hasVoted) voteResults.getOrNull(index)?.coerceIn(0, 100) ?: 0 else 0

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
                        selectedIndex = if (isSelected) null else index
                    }
            ) {
                // 퍼센트 채우기
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
                        text = "${index + 1}. $option",
                        color = textColor,
                        style = fontStyle
                    )
                    if (hasVoted) {
                        Text(
                            text = "${votePercent}%",
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
    val options = listOf("밥", "국수", "고기")
    val results = listOf(20, 30, 50)

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        GroupVoteButton(
            options = options,
            voteResults = results
        )
    }
}