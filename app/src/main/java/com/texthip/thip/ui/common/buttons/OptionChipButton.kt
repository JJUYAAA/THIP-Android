package com.texthip.thip.ui.common.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun OptionChipButton(
    modifier: Modifier = Modifier,
    text: String,
    isFilled: Boolean = false,
    isSelected: Boolean? = null,  // 추가
    onClick: () -> Unit,
) {
    var isClicked by remember { mutableStateOf(false) }
    val checked = isSelected ?: isClicked

    val textColor = when {
        isFilled -> colors.White
        checked -> colors.Purple
        else -> colors.Grey01
    }
    val backgroundColor = when {
        isFilled && checked -> colors.Purple
        isFilled -> colors.DarkGrey
        else -> Color.Transparent
    }
    val borderColor = when {
        !isFilled && checked -> colors.Purple
        !isFilled -> colors.Grey02
        else -> Color.Transparent
    }

    Box(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable {
                if (isSelected == null) isClicked = !isClicked
                onClick()
            }
            .padding(vertical = 8.dp, horizontal = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = typography.info_r400_s12,
            color = textColor
        )
    }

}

@Preview
@Composable
private fun OptionChipButtonPreview() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
    ) {
        OptionChipButton(
            text = stringResource(R.string.essay),
            onClick = {}
        )

        OptionChipButton(
            text = stringResource(R.string.essay),
            isFilled = true,
            onClick = {}
        )
    }
}