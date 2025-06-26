package com.texthip.thip.ui.common.topappbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogoTopAppBar(
    leftIcon: Painter,
    hasNotification: Boolean,
    onLeftClick: () -> Unit = {},
    onRightClick: () -> Unit = {}
) {
    val rightIcon = if (hasNotification) {
        painterResource(R.drawable.ic_notice_yes)
    } else {
        painterResource(R.drawable.ic_notice)
    }

    TopAppBar(
        navigationIcon = {
            Image(
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .height(24.dp)
                    .padding(start = 18.dp),
            )
        },
        title = {
            Spacer(modifier = Modifier)
        },
        actions = {
            IconButton(onClick = onLeftClick) {
                Icon(
                    painter = leftIcon,
                    contentDescription = "Left Icon",
                    tint = Color.Unspecified
                )
            }
            IconButton(onClick = onRightClick) {
                Icon(
                    painter = rightIcon,
                    contentDescription = "Right Icon",
                    tint = Color.Unspecified
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
        )
    )
}

@Preview
@Composable
private fun LogoTopAppBarPreview() {
    LogoTopAppBar(
        leftIcon = painterResource(R.drawable.ic_search),
        hasNotification = true,
        onLeftClick = { },
        onRightClick = { }
    )
}