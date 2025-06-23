package com.texthip.thip.ui.common.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun OutlinedButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: Painter? = null,
    textStyle: TextStyle = typography.menu_sb600_s12_h20,
    onClick: () -> Unit = {},
) {
    val hasIcon = icon != null

    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                color = colors.Grey02,
                shape = RoundedCornerShape(20.dp)
            )
            .background(color = Color.Transparent, shape = RoundedCornerShape(12.dp))
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (hasIcon) {
                Icon(
                    painter = icon,
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(20.dp)
                )
            }
            Text(
                text = text,
                color = colors.Grey,
                style = textStyle
            )
        }
    }
}

@Preview
@Composable
private fun OutlinedButtonPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        verticalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterVertically),
    ) {
        OutlinedButton(
            modifier = Modifier
                .size(width = 68.dp, height = 28.dp),
            text = stringResource(R.string.follow),
            icon = painterResource(id = R.drawable.ic_reset_20),
            onClick = {}
        )

        OutlinedButton(
            modifier = Modifier
                .size(width = 64.dp, height = 32.dp),
            text = stringResource(R.string.change_book),
            textStyle = typography.view_m500_s14,
            onClick = {}
        )
    }
}