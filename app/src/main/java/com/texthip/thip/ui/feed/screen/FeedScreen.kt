package com.texthip.thip.ui.feed.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.texthip.thip.R
import com.texthip.thip.ui.common.topappbar.LeftNameTopAppBar
import com.texthip.thip.ui.common.topappbar.LogoTopAppBar
import com.texthip.thip.ui.theme.ThipTheme.colors

@Composable
fun FeedScreen(navController: NavController) {
    Column(
        Modifier
            .background(colors.Black)
            .fillMaxSize()
    ) {
        LogoTopAppBar(
            leftIcon = painterResource(R.drawable.ic_plusfriend),
            hasNotification = true,
            onLeftClick = {},
            onRightClick = {}
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxSize()
        ) {

        }
    }
}