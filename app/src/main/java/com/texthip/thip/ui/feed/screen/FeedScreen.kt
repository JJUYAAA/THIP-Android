package com.texthip.thip.ui.feed.screen

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.texthip.thip.R
import com.texthip.thip.ui.common.header.AuthorHeader
import com.texthip.thip.ui.common.header.HeaderMenuBarTab
import com.texthip.thip.ui.common.topappbar.LogoTopAppBar
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors

@Composable
fun FeedScreen(navController: NavController? = null,
               nickname: String,
               userRole: String) {
    var selectedIndex = rememberSaveable { mutableIntStateOf(0) }

    Column(
        Modifier
            .fillMaxSize()
    ) {
        LogoTopAppBar(
            leftIcon = painterResource(R.drawable.ic_plusfriend),
            hasNotification = false,
            onLeftClick = {},
            onRightClick = {}
        )
        Spacer(modifier = Modifier.height(40.dp))
        HeaderMenuBarTab(
            titles = listOf("피드", "내 피드"),
            selectedTabIndex = selectedIndex.value,
            onTabSelected = { selectedIndex.value = it }
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            AuthorHeader(
                profileImage = null,
                nickname = nickname,
                badgeText = userRole,
                buttonText = "",
                buttonWidth = 60.dp,
                showButton = false
            )
        }
    }
}

@Preview
@Composable
private fun FeesScreenPrev() {
    ThipTheme {
        FeedScreen(nickname = "ThipUser01", userRole = "문학 칭호")
    }
}