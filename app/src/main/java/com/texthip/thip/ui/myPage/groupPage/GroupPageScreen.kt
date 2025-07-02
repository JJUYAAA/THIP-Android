package com.texthip.thip.ui.myPage.groupPage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.texthip.thip.R
import com.texthip.thip.ui.common.buttons.FloatingButton
import com.texthip.thip.ui.common.topappbar.LogoTopAppBar
import com.texthip.thip.ui.myPage.viewModel.MyPageViewModel
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors


@Composable
fun GroupPageScreen(
    navController: NavHostController? = null,
    viewModel: MyPageViewModel = viewModel()
) {
    val myGroups by viewModel.myGroups.collectAsState()
    val roomSections by viewModel.roomSections.collectAsState()

    Box(
        Modifier
            .background(colors.Black)
            .padding(bottom = 32.dp)
            .fillMaxSize()
    ) {
        LazyColumn(
            Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // 상단바
            item {
                LogoTopAppBar(
                    leftIcon = painterResource(R.drawable.ic_done),
                    hasNotification = false,
                    onLeftClick = { },
                    onRightClick = { }
                )
                Spacer(Modifier.height(16.dp))
            }
            // 검색창
            item {
                GroupSearchTextField(onValueChange = { })
                Spacer(Modifier.height(32.dp))
            }
            // 내 모임방 헤더 + 카드
            item {
                GroupMySectionHeader(
                    onClick = { viewModel.onMyGroupHeaderClick() }
                )
                Spacer(Modifier.height(20.dp))

                GroupPager(
                    groupCards = myGroups,
                    onCardClick = { viewModel.onMyGroupCardClick(it) }
                )
                Spacer(Modifier.height(40.dp))
            }
            item {
                Spacer(
                    Modifier
                    .height(10.dp)
                    .fillMaxWidth()
                    .background(color = colors.DarkGrey02)
                )
                Spacer(Modifier.height(32.dp))
            }
            // 마감 임박한 독서 모임방
            item {
                GroupRoomDeadlineSection(
                    roomSections = roomSections,
                    onRoomClick = { viewModel.onRoomCardClick(it) }
                )
            }
        }
        // 오른쪽 하단 FAB
        FloatingButton(
            icon = painterResource(id = com.texthip.thip.R.drawable.ic_makegroup),
            onClick = { viewModel.onFabClick() }
        )
    }
}


@Preview()
@Composable
fun PreviewMainGroupScreen() {
    ThipTheme {
        val previewViewModel = remember { MyPageViewModel() }
        GroupPageScreen(viewModel = previewViewModel)
    }
}
