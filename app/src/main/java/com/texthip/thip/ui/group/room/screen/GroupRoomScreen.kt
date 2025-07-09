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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.bottomsheet.MenuBottomSheet
import com.texthip.thip.ui.common.topappbar.GradationTopAppBar
import com.texthip.thip.ui.group.room.component.GroupRoomBody
import com.texthip.thip.ui.group.room.component.GroupRoomHeader
import com.texthip.thip.ui.group.room.mock.GroupRoomBodyData
import com.texthip.thip.ui.group.room.mock.GroupRoomHeaderData
import com.texthip.thip.ui.group.room.mock.MenuBottomSheetItem
import com.texthip.thip.ui.group.room.mock.mockVoteData
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupRoomScreen() {
    val scrollState = rememberScrollState()

    var isBottomSheetVisible by remember { mutableStateOf(false) }

    Box(
        if (isBottomSheetVisible) {
            Modifier
                .fillMaxSize()
                .blur(5.dp)
        } else {
            Modifier.fillMaxSize()
        }
    ) {
        Box(modifier = Modifier.verticalScroll(scrollState)) {
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
                        voteList = mockVoteData
                    )
                )
            }
        }

        GradationTopAppBar(
            onLeftClick = {},
            onRightClick = { isBottomSheetVisible = true },
        )
    }

    if (isBottomSheetVisible) {
        MenuBottomSheet(
            items = listOf(
                MenuBottomSheetItem(
                    text = stringResource(R.string.leave_room),
                    color = colors.White,
                    onClick = { }
                ),
                MenuBottomSheetItem(
                    text = stringResource(R.string.report_room),
                    color = colors.Red,
                    onClick = { }
                ),
                MenuBottomSheetItem(
                    text = stringResource(R.string.delete_room),
                    color = colors.Red,
                    onClick = { }
                )
            ),
            onDismiss = {
                isBottomSheetVisible = false
            }
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