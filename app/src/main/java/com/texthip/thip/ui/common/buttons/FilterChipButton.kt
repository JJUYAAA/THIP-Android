package com.texthip.thip.ui.common.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun FilterChipButton(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    onCloseClick: () -> Unit
) {
    Row(
        modifier = modifier
            .height(36.dp)
            .background(
                color = if (isSelected) colors.Purple else colors.DarkGrey,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(start = 12.dp, end = if (isSelected) 8.dp else 12.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null // 클릭 효과 제거
            ) {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = text,
            style = typography.menu_r400_s14_h24,
            color = colors.White
        )

        if (isSelected) {
            Icon(
                painter = painterResource(R.drawable.ic_x),
                contentDescription = "Close Icon",
                tint = colors.White,
                modifier = Modifier.clickable(onClick = onCloseClick)
            )
        }
    }
}

@Preview
@Composable
private fun FilterChipButtonPreview() {
    var isSelected by remember { mutableStateOf(false) }
    FilterChipButton(
        text = "페이지별 보기",
        isSelected = isSelected,
        onClick = { isSelected = !isSelected },
        onCloseClick = { isSelected = false }
    )
}