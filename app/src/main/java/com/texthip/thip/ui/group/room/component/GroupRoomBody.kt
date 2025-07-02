package com.texthip.thip.ui.group.room.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.buttons.ActionBookButton
import com.texthip.thip.ui.common.cards.CardChat
import com.texthip.thip.ui.common.cards.CardNote
import com.texthip.thip.ui.common.cards.CardVote
import com.texthip.thip.ui.group.room.mock.GroupRoomBodyData
import com.texthip.thip.ui.group.room.mock.VoteData

@Composable
fun GroupRoomBody(
    modifier: Modifier = Modifier,
    data: GroupRoomBodyData
) {
    Column(
        modifier = modifier.padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        ActionBookButton(
            bookTitle = data.bookTitle,
            bookAuthor = data.bookAuthor
        ) {}

        CardNote(
            currentPage = data.currentPage,
            percentage = data.percentage,
        ) {}

        CardChat(
            title = stringResource(R.string.group_room_chat),
            subtitle = stringResource(R.string.group_room_chat_description)
        ) {}

        CardVote(
            voteData = data.voteList
        )
    }
}

@Preview
@Composable
private fun GroupRoomBodyPreview() {
    val mockVoteData = listOf(
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
            options = listOf("투표 제목과 항목 버튼이 가로 스크롤되고", "아래 캐러셀 닷은", "위치 그대로, 강조점만 바뀌도록."),
            votes = listOf(40, 35, 25)
        )
    )

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