package com.texthip.thip.ui.group.room.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.topappbar.DefaultTopAppBar
import com.texthip.thip.ui.group.room.component.GroupRoomMatesList
import com.texthip.thip.ui.group.room.mock.GroupRoomMateData
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors

@Composable
fun GroupRoomMatesScreen() {
    Scaffold(
        containerColor = colors.Black,
        topBar = {
            DefaultTopAppBar(
                title = stringResource(R.string.group_room_mates),
                onLeftClick = {},
            )
        },
        content = { innerPadding ->
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(scrollState)
            ) {
                Spacer(Modifier.height(40.dp))
                GroupRoomMatesList(
                    members = listOf(
                        GroupRoomMateData(
                            profileImageUrl = null,
                            nickname = "Thiper",
                            role = "칭호칭호",
                            roleColor = colors.Yellow,
                            subscriberCount = 100
                        ),
                        GroupRoomMateData(
                            profileImageUrl = null,
                            nickname = "thipthip",
                            role = "공식 인플루언서",
                            roleColor = colors.NeonGreen,
                            subscriberCount = 50
                        ),
                        GroupRoomMateData(
                            profileImageUrl = null,
                            nickname = "Thiper",
                            role = "칭호칭호",
                            roleColor = colors.Yellow,
                            subscriberCount = 100
                        ),
                        GroupRoomMateData(
                            profileImageUrl = null,
                            nickname = "thip01",
                            role = "작가",
                            roleColor = colors.NeonGreen,
                            subscriberCount = 100
                        ),
                        GroupRoomMateData(
                            profileImageUrl = null,
                            nickname = "Thiper",
                            role = "칭호칭호",
                            roleColor = colors.Yellow,
                            subscriberCount = 100
                        ),
                        GroupRoomMateData(
                            profileImageUrl = null,
                            nickname = "thipthip",
                            role = "공식 인플루언서",
                            roleColor = colors.NeonGreen,
                            subscriberCount = 50
                        ),
                        GroupRoomMateData(
                            profileImageUrl = null,
                            nickname = "Thiper",
                            role = "칭호칭호",
                            roleColor = colors.Yellow,
                            subscriberCount = 100
                        ),
                        GroupRoomMateData(
                            profileImageUrl = null,
                            nickname = "thip01",
                            role = "작가",
                            roleColor = colors.NeonGreen,
                            subscriberCount = 100
                        ),
                        GroupRoomMateData(
                            profileImageUrl = null,
                            nickname = "Thiper",
                            role = "칭호칭호",
                            roleColor = colors.Yellow,
                            subscriberCount = 100
                        ),
                        GroupRoomMateData(
                            profileImageUrl = null,
                            nickname = "thipthip",
                            role = "공식 인플루언서",
                            roleColor = colors.NeonGreen,
                            subscriberCount = 50
                        ),
                        GroupRoomMateData(
                            profileImageUrl = null,
                            nickname = "Thiper",
                            role = "칭호칭호",
                            roleColor = colors.Yellow,
                            subscriberCount = 100
                        ),
                        GroupRoomMateData(
                            profileImageUrl = null,
                            nickname = "thip01",
                            role = "작가",
                            roleColor = colors.NeonGreen,
                            subscriberCount = 100
                        ),
                    )
                )
            }
        }
    )
}

@Preview
@Composable
private fun GroupRoomMatesScreenPreview() {
    ThipTheme {
        GroupRoomMatesScreen()
    }
}