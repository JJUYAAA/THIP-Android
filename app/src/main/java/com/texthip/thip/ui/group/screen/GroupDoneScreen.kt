package com.texthip.thip.ui.group.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.cards.CardItemRoom
import com.texthip.thip.ui.common.topappbar.DefaultTopAppBar
import com.texthip.thip.ui.group.myroom.mock.GroupCardItemRoomData
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun GroupDoneScreen(
    name: String,
    allDataList: List<GroupCardItemRoomData>,
    onCardClick: (GroupCardItemRoomData) -> Unit = {}
) {
    // isRecruiting == false 인 방만 필터링 (혹시 몰라서 넣어둡니다)
    val doneList = remember(allDataList) {
        allDataList.filter { !it.isRecruiting }
    }

    Column(
        Modifier
            .fillMaxSize()
    ) {
        DefaultTopAppBar(
            title = stringResource(R.string.group_done_title),
            onLeftClick = {},
        )
        Column(
            Modifier
                .background(colors.Black)
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(bottom = 20.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp)
            ) {

                item {
                    Text (
                        text = stringResource(R.string.group_done_user_comment, name),
                        color = colors.White,
                        style = typography.menu_r400_s14_h24
                    )
                }

                items(doneList) { item ->
                    CardItemRoom(
                        title = item.title,
                        participants = item.participants,
                        maxParticipants = item.maxParticipants,
                        isRecruiting = item.isRecruiting,
                        imageRes = item.imageRes,
                        onClick = { onCardClick(item) }
                    )
                }
            }
        }
    }
}



@Preview()
@Composable
fun MyGroupListFilterScreenPreview() {
    ThipTheme {
        val dataList = listOf(
            GroupCardItemRoomData(
                title = "모임방 이름입니다. 모임방...",
                participants = 22,
                maxParticipants = 30,
                isRecruiting = false,
                genreIndex = 0
            ),
            GroupCardItemRoomData(
                title = "모임방 이름입니다. 모임방...",
                participants = 22,
                maxParticipants = 30,
                isRecruiting = false,
                genreIndex = 0
            ),
            GroupCardItemRoomData(
                title = "모임방 이름입니다. 모임방...",
                participants = 22,
                maxParticipants = 30,
                isRecruiting = false,
                genreIndex = 0
            ),
            GroupCardItemRoomData(
                title = "모임방 이름입니다. 모임방...",
                participants = 22,
                maxParticipants = 30,
                isRecruiting = false,
                genreIndex = 0
            ),
            GroupCardItemRoomData(
                title = "모임방 이름입니다. 모임방...",
                participants = 22,
                maxParticipants = 30,
                isRecruiting = false,
                genreIndex = 0
            ),
            GroupCardItemRoomData(
                title = "모임방 이름입니다. 모임방...",
                participants = 22,
                maxParticipants = 30,
                isRecruiting = false,
                genreIndex = 0
            )
        )

        GroupDoneScreen(
            name = "rbqks529",
            allDataList = dataList)
    }
}