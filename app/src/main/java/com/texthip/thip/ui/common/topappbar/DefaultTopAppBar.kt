package com.texthip.thip.ui.common.topappbar

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTopAppBar(
    title: String = "페이지명",
    isTitleVisible: Boolean = true,
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
            if (isTitleVisible) {
                Text(
                    text = title,
                    color = colors.White,
                    style = typography.bigtitle_b700_s22_h24
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