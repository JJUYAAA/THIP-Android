package com.texthip.thip.ui.group.note.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.data.model.comments.response.CommentList
import com.texthip.thip.ui.common.CommentActionMode
import com.texthip.thip.ui.common.header.ProfileBarFeed
import com.texthip.thip.ui.feed.component.CommentActionPopup
import com.texthip.thip.ui.group.note.viewmodel.CommentsEvent
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography
import com.texthip.thip.utils.color.hexToColor

@Composable
fun CommentItem(
    modifier: Modifier = Modifier,
    data: CommentList,
    onReplyClick: (String?) -> Unit = { },
    onLikeClick: () -> Unit = {},
    onLongPress: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    actionMode: CommentActionMode,
    isSelected: Boolean = false,
    onDismissPopup: () -> Unit = {},
    onEvent: (CommentsEvent) -> Unit = { _ -> }
) {
    Box {
        if (data.isDeleted) {
            Text(
                text = stringResource(R.string.comment_deleted),
                style = typography.feedcopy_r400_s14_h20,
                color = colors.Grey02,
            )
        } else {
            Column(
                modifier = modifier.pointerInput(Unit) {
                    detectTapGestures(onLongPress = { onLongPress() })
                },
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                data
                ProfileBarFeed(
                    profileImage = data.creatorProfileImageUrl,
                    nickname = data.creatorNickname ?: "",
                    genreName = data.aliasName ?: "",
                    genreColor = hexToColor(data.aliasColor ?: "#FFFFFF"),
                    date = data.postDate ?: "",
                    onClick = onProfileClick
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        data.content?.let {
                            Text(
                                text = it,
                                color = colors.Grey,
                                style = typography.feedcopy_r400_s14_h20,
                            )
                        }
                        Text(
                            modifier = Modifier.clickable(onClick = { onReplyClick(data.creatorNickname) }),
                            text = stringResource(R.string.write_reply),
                            style = typography.menu_sb600_s12,
                            color = colors.Grey02,
                        )
                    }

                    Column(
                        modifier = Modifier.clickable(onClick = onLikeClick),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                    ) {
                        Icon(
                            painter = painterResource(if (data.isLike) R.drawable.ic_heart_center_filled else R.drawable.ic_heart_center),
                            contentDescription = null,
                            tint = Color.Unspecified
                        )
                        Text(
                            text = data.likeCount.toString(),
                            style = typography.navi_m500_s10,
                            color = colors.White,
                        )
                    }
                }
            }
        }
        if (actionMode == CommentActionMode.POPUP && isSelected) {
            CommentActionPopup(
                text = if (data.isWriter) stringResource(R.string.delete) else stringResource(R.string.report),
                textColor = if (data.isWriter) colors.Red else colors.White,
                onClick = {
                    if (data.isWriter) {
                        data.commentId?.let { onEvent(CommentsEvent.DeleteComment(it)) }
                    } else {
                        // TODO: 신고 로직
                    }
                    onDismissPopup()
                },
                onDismissRequest = onDismissPopup
            )
        }
    }
}


@Preview
@Composable
private fun CommentItemPreview() {
    ThipTheme {
        Column(
            modifier = Modifier
                .clickable { /* TODO: Handle click */ }
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CommentItem(
                data = CommentList(
                    commentId = 1,
                    creatorId = 1,
                    creatorNickname = "User1",
                    creatorProfileImageUrl = "https://example.com/image1.jpg",
                    aliasName = "칭호칭호",
                    aliasColor = "#FF5733",
                    content = "This is a comment.",
                    postDate = "2023-10-01T12:00:00Z",
                    isLike = false,
                    likeCount = 10,
                    isDeleted = false,
                    isWriter = false,
                    replyList = emptyList()
                ),
                actionMode = CommentActionMode.POPUP,
            )

            CommentItem(
                data = CommentList(
                    commentId = 1,
                    creatorId = 1,
                    creatorNickname = "User1",
                    creatorProfileImageUrl = "https://example.com/image1.jpg",
                    aliasName = "칭호칭호",
                    aliasColor = "#FF5733",
                    content = "This is a comment.",
                    postDate = "2023-10-01T12:00:00Z",
                    isLike = false,
                    likeCount = 10,
                    isDeleted = false,
                    isWriter = false,
                    replyList = emptyList()
                ),
                actionMode = CommentActionMode.BOTTOM_SHEET,
            )

            CommentItem(
                data = CommentList(
                    commentId = 1,
                    creatorId = 1,
                    creatorNickname = "User1",
                    creatorProfileImageUrl = "https://example.com/image1.jpg",
                    aliasName = "칭호칭호",
                    aliasColor = "#FF5733",
                    content = "This is a comment.",
                    postDate = "2023-10-01T12:00:00Z",
                    isLike = false,
                    likeCount = 10,
                    isDeleted = false,
                    isWriter = false,
                    replyList = emptyList()
                ),
                actionMode = CommentActionMode.POPUP,
            )
        }
    }
}