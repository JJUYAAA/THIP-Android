package com.texthip.thip.ui.group.note.component

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.data.model.rooms.response.PostList
import com.texthip.thip.ui.common.buttons.ActionBarButton
import com.texthip.thip.ui.common.buttons.GroupVoteButton
import com.texthip.thip.ui.common.header.ProfileBar
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun VoteCommentCard(
    modifier: Modifier = Modifier,
    data: PostList,
    onLikeClick: (postId: Int, postType: String) -> Unit = { _, _ -> },
    onVote: (postId: Int, voteItemId: Int, type: Boolean) -> Unit = { _, _, _ -> },
    onCommentClick: () -> Unit = {},
    onLongPress: () -> Unit = {},
) {
    val selectedIndex = data.voteItems.indexOfFirst { it.isVoted }.takeIf { it != -1 }
    val hasVoted = selectedIndex != null

    val isLocked = data.isLocked
    val isWriter = data.isWriter

    val pageText = if (data.isOverview) {
        stringResource(id = R.string.general_review)
    } else {
        data.page.toString() + stringResource(R.string.page)
    }

    Column(
        modifier = modifier
            .blur(if (isLocked) 5.dp else 0.dp)
            .pointerInput(Unit) {
                if (!isLocked) {
                    detectTapGestures(onLongPress = { onLongPress() })
                }
            }
            .padding(start = 20.dp, end = 20.dp, top = 32.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ProfileBar(
            profileImage = data.profileImageUrl,
            topText = data.nickName,
            bottomText = pageText,
            bottomTextColor = colors.Purple,
            showSubscriberInfo = false,
            hoursAgo = data.postDate
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = data.content,
                style = typography.feedcopy_r400_s14_h20,
                color = colors.White,
            )

            GroupVoteButton(
                voteItems = data.voteItems,
                selectedIndex = selectedIndex,
                hasVoted = hasVoted,
                onOptionSelected = { index ->
                    if (!isLocked) {
                        if (index == null) {
                            selectedIndex?.let {
                                val votedItemId = data.voteItems[it].voteItemId
                                onVote(data.postId, votedItemId, false) // type: false (취소)
                            }
                        } else {
                            val votedItemId = data.voteItems[index].voteItemId
                            onVote(data.postId, votedItemId, true) // type: true (투표)
                        }
                    }
                }
            )
        }

        ActionBarButton(
            isLiked = data.isLiked,
            likeCount = data.likeCount,
            commentCount = data.commentCount,
            onLikeClick = {
                if (!isLocked) onLikeClick(data.postId, data.postType)
            },
            onCommentClick = {
                if (!isLocked) onCommentClick()
            }
        )
    }
}

@Preview
@Composable
private fun VoteCommentCardPreview() {
    VoteCommentCard(
        data = PostList(
            postId = 1,
            postType = "group",
            page = 132,
            postDate = "12시간 전",
            userId = 1,
            nickName = "user.01",
            profileImageUrl = "https://example.com/profile.jpg",
            content = "내 생각에 이 부분이 가장 어려운 것 같다. 비유도 난해하고 잘 이해가 가지 않는데 다른 메이트들은 어떻게 읽었나요?",
            likeCount = 123,
            commentCount = 123,
            isLiked = true,
            isWriter = false,
            isLocked = false,
            isOverview = false,
            voteItems = emptyList()
        )
    )
}