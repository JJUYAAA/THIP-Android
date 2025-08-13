package com.texthip.thip.ui.group.note.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.data.model.comments.response.CommentList
import com.texthip.thip.ui.common.modal.drawVerticalScrollbar
import com.texthip.thip.ui.theme.ThipTheme

@Composable
fun CommentList(
    modifier: Modifier = Modifier,
    commentList: List<CommentList>,
    onSendReply: (String, Int?, String?) -> Unit,
    onReplyClick: (commentId: Int, nickname: String) -> Unit
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(482.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .drawVerticalScrollbar(scrollState),
        ) {
            commentList.forEach { commentItem ->
                CommentSection(
                    commentItem = commentItem,
                    onSendReply = onSendReply,
                    onReplyClick = onReplyClick
                )
            }
        }
    }
}

@Composable
fun CommentSection(
    commentItem: CommentList,
    onSendReply: (String, Int?, String?) -> Unit = { _, _, _ -> },
    onReplyClick: (commentId: Int, nickname: String) -> Unit
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
                onReplyClick = { onReplyClick(commentItem.commentId, commentItem.creatorNickname) }
            )

            commentItem.replyList.forEach { reply ->
                ReplyItem(
                    data = reply,
                    onReplyClick = { onReplyClick(commentItem.commentId, reply.creatorNickname) }
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
            CommentList(
                commentList = listOf(
                    CommentList(
                        commentId = 1,
                        creatorId = 1,
                        creatorNickname = "User1",
                        creatorProfileImageUrl = "https://example.com/image1.jpg",
                        aliasName= "칭호칭호",
                        aliasColor = "#A0F8E8",
                        content = "This is a comment.",
                        postDate = "2023-10-01",
                        isLike = false,
                        likeCount = 10,
                        isDeleted = false,
                        replyList = emptyList()
                    )
                ),
                onSendReply = { _, _, _ -> },
                onReplyClick = { commentId, nickname ->
                    // Handle reply click
                }
            )
        }
    }
}