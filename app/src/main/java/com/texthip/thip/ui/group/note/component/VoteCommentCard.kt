package com.texthip.thip.ui.group.note.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.buttons.ActionBarButton
import com.texthip.thip.ui.common.buttons.GroupVoteButton
import com.texthip.thip.ui.common.header.ProfileBar
import com.texthip.thip.ui.group.note.mock.GroupNoteVote
import com.texthip.thip.ui.group.note.mock.VoteItem
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun VoteCommentCard(
    modifier: Modifier = Modifier,
    data: GroupNoteVote,
) {
    var isLiked by remember { mutableStateOf(data.isLiked) }
    var selected by remember { mutableStateOf<Int?>(null) }
    var voteItems by remember { mutableStateOf(data.voteItems) }
    val hasVoted = voteItems.any { it.isVoted }

    Column(
        modifier = modifier
            .blur(if (data.isLocked) 5.dp else 0.dp)
            .padding(vertical = 16.dp, horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ProfileBar(
//            profileImage = data.profileImageUrl,
            profileImage = painterResource(R.drawable.character_literature),
            topText = data.nickName,
            bottomText = data.page.toString() + stringResource(R.string.page),
            bottomTextColor = colors.Purple,
            showSubscriberInfo = false,
//            hoursAgo = data.postDate
            hoursAgo = data.postDate
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = data.content,
                style = typography.feedcopy_r400_s14_h20,
                color = colors.White,
            )

            GroupVoteButton(
                voteItems = voteItems,
                selectedIndex = selected,
                hasVoted = hasVoted,
                onOptionSelected = {
                    if (selected == it) {
                        selected = null
                        voteItems = voteItems.map { it.copy(isVoted = false) }
                    } else {
                        selected = it
                        voteItems = voteItems.mapIndexed { index, item ->
                            item.copy(isVoted = index == it)
                        }
                    }
                }
            )
        }

        ActionBarButton(
            isLiked = isLiked,
            likeCount = data.likeCount,
            commentCount = data.commentCount,
            onLikeClick = {
                isLiked = !isLiked
            },
            onCommentClick = { },
        )
    }
}

@Preview
@Composable
private fun VoteCommentCardPreview() {
    VoteCommentCard(
        data = GroupNoteVote(
            postDate = 12,
            page = 12,
            userId = 1,
            nickName = "user.01",
            profileImageUrl = "https://example.com/profile.jpg",
            content = "3연에 나오는 심장은 무엇을 의미하는 걸까요?",
            likeCount = 123,
            commentCount = 123,
            isLiked = true,
            isWriter = false,
            isLocked = false,
            voteId = 1,
            voteItems = listOf(
                VoteItem(1, "김땡땡", 90, false),
                VoteItem(2, "김땡땡", 10, false),
            )
        )
    )
}