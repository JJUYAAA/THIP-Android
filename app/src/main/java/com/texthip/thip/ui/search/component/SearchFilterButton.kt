package com.texthip.thip.ui.search.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

// 단순 FilterButton 컴포넌트 (드롭다운 제거)
@Composable
fun SearchFilterButton(
    modifier: Modifier = Modifier,
    selectedOption: String,
    isExpanded: Boolean = false,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopEnd
    ) {
        Row(
            modifier = Modifier
                .height(36.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onClick() },
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedOption,
                color = colors.Grey01,
                style = typography.menu_m500_s14_h24
            )
            Icon(
                painter = painterResource(
                    id = if (isExpanded) R.drawable.ic_upmore else R.drawable.ic_downmore
                ),
                contentDescription = "Dropdown",
                tint = colors.Grey02,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

// Screen 레벨 드롭다운 오버레이
@Composable
fun SearchFilterDropdownOverlay(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    options: List<String>,
    selectedOption: String,
    filterButtonPosition: IntOffset,
    density: Density,
    onOptionSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    if (isVisible) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onDismiss() }
        )
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopEnd
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        top = with(density) { (filterButtonPosition.y - 36).toDp() },
                        end = 20.dp // 기존 FilterButton과 동일하게 화면 오른쪽에서 20dp
                    )
                    .width(144.dp)
                    .border(
                        width = 1.dp,
                        color = colors.Grey01,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .background(color = colors.Black, shape = RoundedCornerShape(16.dp))
                    .padding(vertical = 20.dp, horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                options.forEach { option ->
                    val isSelected = option == selectedOption
                    Text(
                        text = option,
                        color = if (isSelected) colors.White else colors.Grey02,
                        style = typography.feedcopy_r400_s14_h20,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onOptionSelected(option)
                                onDismiss()
                            }
                    )
                }
            }
        }
    }
}