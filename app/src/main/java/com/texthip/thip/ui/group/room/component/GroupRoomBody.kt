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
import com.texthip.thip.ui.group.room.mock.mockVoteData

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