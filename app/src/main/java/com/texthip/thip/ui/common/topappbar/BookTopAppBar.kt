package com.texthip.thip.ui.common.topappbar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.texthip.thip.R
import com.texthip.thip.ui.common.view.CountingBar
import com.texthip.thip.ui.theme.ThipTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookTopAppBar(
    count: Int = 0,
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit,
) {
    TopAppBar(
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
            CountingBar(
                count = count,
            )
        },
        actions = {
            IconButton(onClick = {
                onRightClick()
            }) {
                Icon(
                    painter = painterResource(R.drawable.ic_more),
                    contentDescription = "More Options",
                    tint = Color.Unspecified
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
        ),
    )
}

@Preview(showBackground = false)
@Composable
private fun BookTopAppBarPreview() {
    ThipTheme {
        BookTopAppBar(
            onLeftClick = { },
            onRightClick = { },
            count = 210
        )
    }
}