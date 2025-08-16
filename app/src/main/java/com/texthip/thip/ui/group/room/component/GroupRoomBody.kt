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
import com.texthip.thip.data.model.rooms.response.CurrentVote
import com.texthip.thip.data.model.rooms.response.VoteItem
import com.texthip.thip.ui.common.buttons.ActionBookButton
import com.texthip.thip.ui.common.cards.CardChat
import com.texthip.thip.ui.common.cards.CardNote
import com.texthip.thip.ui.common.cards.CardVote
import kotlin.math.roundToInt

@Composable
fun GroupRoomBody(
    modifier: Modifier = Modifier,
    bookTitle: String,
    authorName: String,
    isbn: String,
    currentPage: Int,
    userPercentage: Double,
    currentVotes: List<CurrentVote>,
    onNavigateToBookDetail: (isbn: String) -> Unit = {},
    onNavigateToNote: () -> Unit = {},
    onNavigateToChat: () -> Unit = {},
    onVoteClick: (CurrentVote) -> Unit = {}
) {
    Column(
        modifier = modifier.padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        ActionBookButton(
            bookTitle = bookTitle,
            bookAuthor = authorName
        ) {
            onNavigateToBookDetail(isbn)
        }

        CardNote(
            currentPage = currentPage,
            percentage = userPercentage.roundToInt(),
        ) {
            onNavigateToNote()
        }

        CardChat(
            title = stringResource(R.string.group_room_chat),
            subtitle = stringResource(R.string.group_room_chat_description)
        ) {
            onNavigateToChat()
        }

        CardVote(
            voteData = currentVotes,
            onVoteClick = onVoteClick
        )
    }
}

@Preview
@Composable
private fun GroupRoomBodyPreview() {
    GroupRoomBody(
        bookTitle = "책 제목",
        authorName = "저자 이름",
        isbn = "1234567890",
        currentPage = 100,
        userPercentage = 50.0,
        currentVotes = listOf(
            CurrentVote(
                content = "3연에 나오는 심장은 무엇을 의미하는 걸까요?",
                page = 12,
                isOverview = false,
                voteItems = listOf(
                    VoteItem("사랑"),
                    VoteItem("희생"),
                    VoteItem("고통"),
                )
            ),
            CurrentVote(
                content = "가장 인상 깊었던 구절은 무엇인가요?",
                page = 25,
                isOverview = false,
                voteItems = listOf(
                    VoteItem("1연 1행"),
                    VoteItem("2연 3행"),
                )
            )
        )
    )
}