package com.texthip.thip.ui.group.note.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.texthip.thip.ui.common.forms.CommentTextField
import com.texthip.thip.ui.group.note.mock.CommentItem
import com.texthip.thip.ui.group.note.mock.ReplyItem
import com.texthip.thip.ui.group.note.mock.mockComment
import com.texthip.thip.ui.theme.ThipTheme

@Composable
fun CommentSection(
    commentItem: CommentItem,
    onSendReply: (String, Int?, String?) -> Unit
) {
    val scrollState = rememberScrollState()

    var inputText by remember { mutableStateOf("") }
    var replyingTo by remember { mutableStateOf<ReplyItem?>(null) }

    Box {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.Start
        ) {
            CommentItem(
                data = commentItem,
                onReplyClick = {
                    replyingTo = ReplyItem(
                        replyId = -1,
                        userId = commentItem.userId,
                        nickName = commentItem.nickName,
                        parentNickname = "",
                        genreName = commentItem.genreName,
                        profileImageUrl = commentItem.profileImageUrl,
                        content = commentItem.content,
                        postDate = commentItem.postDate,
                        isWriter = commentItem.isWriter,
                        isLiked = commentItem.isLiked,
                        likeCount = commentItem.likeCount
                    )
                }
            )

            commentItem.replyList.forEach { reply ->
                ReplyItem(
                    data = reply,
                    onReplyClick = { replyingTo = reply }
                )
            }
        }

        CommentTextField(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            hint = stringResource(R.string.reply_to),
            input = inputText,
            onInputChange = { inputText = it },
            onSendClick = {
                onSendReply(
                    inputText,
                    replyingTo?.replyId,
                    replyingTo?.nickName
                )
                inputText = ""
                replyingTo = null
            },
            replyTo = replyingTo?.nickName,
            onCancelReply = { replyingTo = null }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CommentSectionPreview() {
    ThipTheme {
        CommentSection(
            commentItem = mockComment,
            onSendReply = { text, replyId, parentNickname ->
                println("전송됨: '$text' → replyId: $replyId, to: @$parentNickname")
            }
        )
    }
}