package com.texthip.thip.ui.common.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
fun MenuItemButton(
    text: String,
    icon: Painter? = null,
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
            .height(56.dp)
            .background(color = backgroundColor, shape = RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (hasLeftIcon) {
                Icon(
                    painter = icon,
                    contentDescription = null,
                    tint = contentColor,
                )
                Spacer(modifier = Modifier.width(12.dp))
            }
            Text(
                text = text,
                color = contentColor,
                style = typography.smalltitle_sb600_s16_h24,
            )
        }
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
private fun MenuItemButtonPreview() {
    MenuItemButton(
        text = stringResource(R.string.saved),
        icon = painterResource(R.drawable.ic_save),
        contentColor = colors.White,
        backgroundColor = colors.DarkGrey02,
        hasRightIcon = true,
        modifier = Modifier
            .fillMaxWidth(),
        onClick = {}
    )
}