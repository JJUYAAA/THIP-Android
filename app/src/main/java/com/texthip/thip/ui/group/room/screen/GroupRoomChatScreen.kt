package com.texthip.thip.ui.group.room.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.cards.CardCommentGroup
import com.texthip.thip.ui.common.forms.CommentTextField
import com.texthip.thip.ui.common.topappbar.DefaultTopAppBar
import com.texthip.thip.ui.common.view.CountingBar
import com.texthip.thip.ui.group.room.mock.GroupRoomChatData
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors

@Composable
fun GroupRoomChatScreen() {
    val messages = listOf(
        GroupRoomChatData(
            null,
            "user.01",
            "2024.04.29",
            "공백 포함 글자 입력입니다. 공백 포함 글자 입력입니다. 공백 포함 글자 입력입니다. 공백 포함 글자 입력입니다. 공백 포함 글자 입력입니다."
        ),
        GroupRoomChatData(
            null,
            "user.01",
            "2024.04.28",
            "공백 포함 글자 입력입니다. 공백 포함 글자 입력입니다. 공백 포함 글자 입력입니다. 공백 포함 글자 입력입니다. 공백 포함 글자 입력입니다."
        ),
        GroupRoomChatData(null, "user.01", "2024.04.30", "공백 포함 글자 입력입니다."),
        GroupRoomChatData(null, "user.01", "2024.04.30", "공백 포함 글자 입력입니다. 공백 포함 글자 입력입니다."),
        GroupRoomChatData(
            null,
            "user.01",
            "2024.04.30",
            "공백 포함 글자 입력입니다. 공백 포함 글자 입력입니다. 공백 포함 글자 입력입니다. 공백 포함 글자 입력입니다. 공백 포함 글자 입력입니다."
        ),
    ).sortedByDescending { it.date }

    var input by remember { mutableStateOf("") }
    var replyTo by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        DefaultTopAppBar(
            title = stringResource(R.string.group_room_chat),
            onLeftClick = {},
        )

        LazyColumn(
            reverseLayout = true,
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            itemsIndexed(messages) { index, message ->
                val isNewDate = when {
                    index == 0 -> true
                    messages[index - 1].date != message.date -> true
                    else -> false
                }

                val isBottomItem = index == 0

                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = if (isBottomItem) Modifier.padding(bottom = 20.dp) else Modifier
                ) {
                    if (isNewDate) {
                        HorizontalDivider(
                            color = colors.DarkGrey02,
                            thickness = 10.dp
                        )
                        CountingBar(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            content = message.date
                        )
                    }
                    CardCommentGroup(data = message)
                }
            }
        }
        CommentTextField(
            input = input,
            hint = stringResource(R.string.group_room_chat_hint),
            onInputChange = { input = it },
            onSendClick = {
                input = ""
                replyTo = null
            },
            replyTo = replyTo,
            onCancelReply = { replyTo = null }
        )
    }
}

@Preview
@Composable
private fun GroupRoomChatScreenPreview() {
    ThipTheme {
        GroupRoomChatScreen()
    }
}