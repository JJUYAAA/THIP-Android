package com.texthip.thip.ui.group.screen.room.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.topappbar.DefaultTopAppBar
import com.texthip.thip.ui.group.screen.room.component.GroupRoomHeader
import com.texthip.thip.ui.group.screen.room.mock.GroupRoomHeaderData
import com.texthip.thip.ui.theme.ThipTheme.colors

@Composable
fun GroupRoomScreen() {
    Scaffold(
        containerColor = colors.Black,
        topBar = {
            DefaultTopAppBar(
                isRightIconVisible = true,
                isTitleVisible = false,
                onLeftClick = {},
                onRightClick = {},
            )
        },
        content = { innerPadding ->
            Box {
                Image(
                    painter = painterResource(R.drawable.img_group_room), // ✅ 배경 이미지
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(344.dp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(innerPadding)
                ) {
                    GroupRoomHeader(
                        data = GroupRoomHeaderData(
                            groupRoomName = "호르몬 체인지 완독하는 방",
                            introductionContent = "‘시집만 읽는 사람들’ 3월 모임입니다. 이번 달 모임방은 심장보다 단단한 토마토 한 알 완독합니다.",
                            isPrivate = true,
                            period = "2023.10.01 ~ 2023.10.31",
                            participantCount = 22,
                            genre = "문학"
                        )
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun GroupRoomScreenPreview() {
    GroupRoomScreen()
}