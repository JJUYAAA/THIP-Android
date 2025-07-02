package com.texthip.thip.ui.common.topappbar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
fun LeftNameTopAppBar(
    title: String = stringResource(R.string.page_name),
    isLeftIconVisible: Boolean = false,
    isRightIconVisible: Boolean = false,
    leftIcon: Painter = painterResource(R.drawable.ic_search),
    rightIcon: Painter = painterResource(R.drawable.ic_plus),
    onLeftClick: () -> Unit = {},
    onRightClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colors.Black)
            .padding(horizontal = 20.dp, vertical = 20.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = title,
            color = colors.White,
            style = typography.bigtitle_b700_s22_h24,
            modifier = Modifier.align(Alignment.CenterStart)
        )

        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd),
            horizontalArrangement = spacedBy(20.dp),
        ) {
            if (isLeftIconVisible) {
                Icon(
                    painter = leftIcon,
                    contentDescription = "Left Icon",
                    tint = Color.Unspecified,
                    modifier = Modifier.clickable { onLeftClick() }
                )
            }
            if (isRightIconVisible) {
                Icon(
                    painter = rightIcon,
                    contentDescription = "Right Icon",
                    tint = Color.Unspecified,
                    modifier = Modifier.clickable { onRightClick() }
                )
            }
        }
    }
}


@Preview
@Composable
private fun LeftNameTopAppBarPreview() {
    Column {
        LeftNameTopAppBar()
        LeftNameTopAppBar(
            isLeftIconVisible = true,
            isRightIconVisible = true
        )
    }
}
