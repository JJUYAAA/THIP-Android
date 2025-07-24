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
import com.texthip.thip.ui.common.modal.drawVerticalScrollbar
import com.texthip.thip.ui.group.note.mock.CommentItem
import com.texthip.thip.ui.group.note.mock.ReplyItem
import com.texthip.thip.ui.group.note.mock.mockComment
import com.texthip.thip.ui.theme.ThipTheme

@Composable
fun CommentList(
    modifier: Modifier = Modifier,
    commentList: List<CommentItem>,
    onSendReply: (String, Int?, String?) -> Unit,
    onReplyClick: (ReplyItem) -> Unit
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
    commentItem: CommentItem,
    onSendReply: (String, Int?, String?) -> Unit,
    onReplyClick: (ReplyItem) -> Unit
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
                    onReplyClick(
                        ReplyItem(
                            replyId = commentItem.commentId,
                            userId = commentItem.userId,
                            nickName = commentItem.nickName,
                            parentNickname = "", // 댓글에는 parentNickname이 필요 없음
                            genreName = commentItem.genreName,
                            profileImageUrl = commentItem.profileImageUrl,
                            content = commentItem.content,
                            postDate = commentItem.postDate,
                            isWriter = commentItem.isWriter,
                            isLiked = commentItem.isLiked,
                            likeCount = commentItem.likeCount
                        )
                    )
                }
            )

            commentItem.replyList.forEach { reply ->
                ReplyItem(
                    data = reply,
                    onReplyClick = {
                        onReplyClick(reply)
                    }
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
                    mockComment, mockComment, mockComment
                ),
                onSendReply = { _, _, _ -> },
                onReplyClick = { replyItem ->
                    // Handle reply click
                }
            )
        }
    }
}