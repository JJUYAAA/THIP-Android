package com.texthip.thip.ui.group.note.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.data.model.comments.response.CommentList
import com.texthip.thip.data.model.comments.response.ReplyList
import com.texthip.thip.ui.common.CommentActionMode
import com.texthip.thip.ui.group.note.viewmodel.CommentsEvent
import com.texthip.thip.ui.theme.ThipTheme

@Composable
fun CommentSection(
    commentItem: CommentList,
    onReplyClick: (commentId: Int, nickname: String?) -> Unit,
    onEvent: (CommentsEvent) -> Unit = { _ -> },
    onCommentLongPress: (CommentList) -> Unit = { _ -> },
    onReplyLongPress: (ReplyList) -> Unit = { _ -> },
    actionMode: CommentActionMode,
    selectedCommentId: Int? = null,
    onDismissPopup: () -> Unit = {}
) {
    Box {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.Start
        ) {
            CommentItem(
                data = commentItem,
                onReplyClick = {
                    // commentId가 null이 아닐 때만 답글 달기 가능
                    // todo: 수정 가능
                    commentItem.commentId?.let { id ->
                        onReplyClick(id, commentItem.creatorNickname)
                    }
                },
                onLikeClick = {
                    // commentId가 null이 아닐 때만 좋아요 가능
                    // todo: 수정 가능
                    commentItem.commentId?.let { id ->
                        onEvent(CommentsEvent.LikeComment(id))
                    }
                },
                onLongPress = { onCommentLongPress(commentItem) },
                actionMode = actionMode,
                isSelected = selectedCommentId != null && commentItem.commentId == selectedCommentId,
                onDismissPopup = onDismissPopup,
                onEvent = onEvent
            )

            commentItem.replyList.forEach { reply ->
                ReplyItem(
                    data = reply,
                    onReplyClick = {
                        commentItem.commentId?.let { parentId ->
                            onReplyClick(parentId, reply.creatorNickname)
                        }
                    },
                    onLikeClick = {
                        onEvent(CommentsEvent.LikeReply(reply.commentId))
                    },
                    onLongPress = { onReplyLongPress(reply) },
                    actionMode = actionMode,
                    isSelected = selectedCommentId != null && reply.commentId == selectedCommentId,
                    onDismissPopup = onDismissPopup,
                    onEvent = onEvent
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommentSectionPreview() {
    ThipTheme {
        Column {
            CommentSection(
                commentItem =
                    CommentList(
                        commentId = 1,
                        creatorId = 1,
                        creatorNickname = "User1",
                        creatorProfileImageUrl = "https://example.com/image1.jpg",
                        aliasName = "칭호칭호",
                        aliasColor = "#A0F8E8",
                        content = "This is a comment.",
                        postDate = "2023-10-01",
                        isLike = false,
                        likeCount = 10,
                        isDeleted = false,
                        replyList = emptyList(),
                        isWriter = false
                    ),
                onReplyClick = { commentId, nickname ->
                    // Handle reply click
                },
                actionMode = CommentActionMode.POPUP,
            )
        }
    }
}