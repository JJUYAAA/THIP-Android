package com.texthip.thip.ui.common.topappbar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun FeedListTopAppBar(
    nickname: String = "ThipUser 01ThipUser 01",
    isRightIconVisible: Boolean = false,
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colors.Black)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_arrow_back),
            contentDescription = "Back Button",
            tint = Color.Unspecified,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clickable { onLeftClick() }
        )

        Row(
            modifier = Modifier.align(Alignment.Center),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = nickname,
                color = colors.White,
                style = typography.bigtitle_b700_s22_h24,
                modifier = Modifier.width(100.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = stringResource(R.string.subscriber),
                color = colors.White,
                style = typography.bigtitle_b700_s22_h24,
                modifier = Modifier.width(100.dp),
                maxLines = 1
            )
        }

        if (isRightIconVisible) {
            Icon(
                painter = painterResource(R.drawable.ic_more),
                contentDescription = "More Options",
                tint = Color.Unspecified,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable { onRightClick() }
            )
        }
    }
}

@Preview
@Composable
private fun FeedListTopAppBarPreview() {
    FeedListTopAppBar(
        isRightIconVisible = true,
        onLeftClick = {},
        onRightClick = {}
    )
}