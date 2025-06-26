package com.texthip.thip.ui.common.topappbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedListTopAppBar(
    nickname: String = "ThipUser 01ThipUser 01",
    isRightIconVisible: Boolean = false,
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(onClick = {
                onLeftClick()
            }) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = "Back Button",
                    tint = Color.Unspecified
                )
            }
        },
        title = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = nickname,
                    color = colors.White,
                    style = typography.bigtitle_b700_s22_h24,
                    modifier = Modifier.width(100.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = stringResource(R.string.subscriber),
                    color = colors.White,
                    style = typography.bigtitle_b700_s22_h24,
                    modifier = Modifier.width(100.dp),
                    maxLines = 1
                )
            }
        },
        actions = {
            if (isRightIconVisible) {
                IconButton(onClick = {
                    onRightClick()
                }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_more),
                        contentDescription = "More Options",
                        tint = Color.Unspecified
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
        ),
    )
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