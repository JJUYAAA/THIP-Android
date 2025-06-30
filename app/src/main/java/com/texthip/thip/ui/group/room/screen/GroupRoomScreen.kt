package com.texthip.thip.ui.group.room.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.topappbar.GradationTopAppBar
import com.texthip.thip.ui.group.room.component.GroupRoomBody
import com.texthip.thip.ui.group.room.component.GroupRoomHeader
import com.texthip.thip.ui.group.room.mock.GroupRoomBodyData
import com.texthip.thip.ui.group.room.mock.GroupRoomHeaderData
import com.texthip.thip.ui.group.room.mock.VoteData
import com.texthip.thip.ui.theme.ThipTheme

@Composable
fun GroupRoomScreen() {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier.verticalScroll(scrollState)
        ) {
            Image(
                painter = painterResource(R.drawable.img_group_room),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(360.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 76.dp, bottom = 20.dp)
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

                Spacer(Modifier.height(30.dp))

                GroupRoomBody(
                    data = GroupRoomBodyData(
                        bookTitle = "호르몬 체인지",
                        bookAuthor = "최정화",
                        currentPage = 100,
                        percentage = 50,
                        voteList = listOf(
                            VoteData(
                                description = "투표 내용입니다...",
                                options = listOf("김땡땡", "이땡땡", "박땡땡", "최땡땡", "정땡땡"),
                                votes = listOf(50, 10, 20, 15, 5)
                            ),
                            VoteData(
                                description = "옆으로 넘긴 다른 투표 01",
                                options = listOf("어쩌구", "저쩌구", "삼번", "사번"),
                                votes = listOf(25, 45, 20, 10)
                            ),
                            VoteData(
                                description = "옆으로 넘긴 다른 투표 02",
                                options = listOf(
                                    "투표 제목과 항목 버튼이 가로 스크롤되고",
                                    "아래 캐러셀 닷은",
                                    "위치 그대로, 강조점만 바뀌도록."
                                ),
                                votes = listOf(40, 35, 25)
                            )
                        )
                    )
                )
            }
        }
        GradationTopAppBar(
            onLeftClick = {},
            onRightClick = {},
        )
    }
}

@Preview
@Composable
private fun GroupRoomScreenPreview() {
    ThipTheme {
        GroupRoomScreen()
    }
}