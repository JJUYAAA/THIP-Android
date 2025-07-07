package com.texthip.thip.ui.common.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun ActionMediumButton(
    text: String,
    icon: Painter? = null,
    iconSize: Int = 24,
    iconTint: Color = Color.Unspecified,
    contentColor: Color,
    backgroundColor: Color,
    hasRightIcon: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val hasLeftIcon = icon != null

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .background(color = backgroundColor, shape = RoundedCornerShape(12.dp))
            .clickable {
                onClick()
            },
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (hasLeftIcon) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(iconSize.dp)
            )
        }

        Text(
            text = text,
            color = contentColor,
            style = typography.smalltitle_sb600_s16_h24,
        )

        if (hasRightIcon) {
            Icon(
                painter = painterResource(R.drawable.ic_chevron),
                contentDescription = null,
                tint = contentColor,
            )
        }
    }
}

@Preview
@Composable
private fun ActionMediumButtonPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
    ) {
        ActionMediumButton(
            text = stringResource(R.string.add_to_my_feed),
            icon = painterResource(R.drawable.ic_search),
            contentColor = colors.White,
            backgroundColor = colors.Grey02,
            hasRightIcon = true,
            modifier = Modifier.width(180.dp),
            onClick = {},
        )

        ActionMediumButton(
            text = stringResource(R.string.add_to_my_feed),
            icon = painterResource(R.drawable.ic_plus),
            contentColor = colors.White,
            backgroundColor = colors.Purple,
            hasRightIcon = true,
            modifier = Modifier.width(180.dp),
            onClick = {},
        )

        ActionMediumButton(
            text = stringResource(R.string.add_to_my_feed),
            icon = painterResource(R.drawable.ic_search),
            contentColor = colors.Grey,
            backgroundColor = Color.Transparent,
            hasRightIcon = true,
            modifier = Modifier
                .width(180.dp)
                .border(width = 1.dp, color = colors.Grey02, shape = RoundedCornerShape(12.dp)),
            onClick = {},
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally),
        ) {

        ActionMediumButton(
            text = stringResource(R.string.yes),
            contentColor = colors.White,
            backgroundColor = colors.Purple,
            modifier = Modifier.weight(1f),
            onClick = {},
        )

        ActionMediumButton(
            text = stringResource(R.string.no),
            contentColor = colors.White,
            backgroundColor = colors.Grey02,
            modifier = Modifier.weight(1f),
            onClick = {},
        )
        }
    }
}