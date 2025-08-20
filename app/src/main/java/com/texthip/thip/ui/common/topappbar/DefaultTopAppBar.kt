package com.texthip.thip.ui.common.topappbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun DefaultTopAppBar(
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.page_name),
    isTitleVisible: Boolean = true,
    isRightIconVisible: Boolean = false,
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_arrow_back),
            contentDescription = "Back Button",
            tint = Color.Unspecified,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clickable(onClick = onLeftClick)
        )

        if (isTitleVisible) {
            Text(
                text = title,
                color = colors.White,
                style = typography.bigtitle_b700_s22_h24,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        if (isRightIconVisible) {
            Icon(
                painter = painterResource(R.drawable.ic_more),
                contentDescription = "More Options",
                tint = Color.Unspecified,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable(onClick = onRightClick)
            )
        }
    }
}

@Preview
@Composable
private fun DefaultTopAppBarPreview() {
    Column {
        DefaultTopAppBar(
            onLeftClick = {},
        )
        DefaultTopAppBar(
            isRightIconVisible = true,
            onLeftClick = {},
            onRightClick = {},
        )
        DefaultTopAppBar(
            isRightIconVisible = true,
            isTitleVisible = false,
            onLeftClick = {},
            onRightClick = {},
        )
        DefaultTopAppBar(
            isRightIconVisible = false,
            isTitleVisible = false,
            onLeftClick = {},
        )
    }
}