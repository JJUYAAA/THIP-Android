package com.texthip.thip.ui.group.note.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
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
import com.texthip.thip.ui.common.bottomsheet.CustomBottomSheet
import com.texthip.thip.ui.common.forms.CommentTextField
import com.texthip.thip.ui.group.note.mock.CommentItem
import com.texthip.thip.ui.group.note.mock.ReplyItem
import com.texthip.thip.ui.group.note.mock.mockComment
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun CommentBottomSheet(
    commentResponse: List<CommentItem>,
    onDismiss: () -> Unit,
    onSendReply: (String, Int?, String?) -> Unit
) {
    var inputText by remember { mutableStateOf("") }
    var replyingTo by remember { mutableStateOf<ReplyItem?>(null) }

    CustomBottomSheet(onDismiss = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(0.8f)
            ) {
                Text(
                    text = stringResource(R.string.comments),
                    style = typography.title_b700_s20_h24,
                    color = colors.White,
                    modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp)
                )

                if (commentResponse.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 210.dp), // TODO: 유동적으로 수정 가능할수도
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
                    ) {
                        Text(
                            text = stringResource(R.string.no_comments_yet),
                            style = typography.smalltitle_sb600_s18_h24,
                            color = colors.White
                        )
                        Text(
                            text = stringResource(R.string.no_comment_subtext),
                            style = typography.copy_r400_s14,
                            color = colors.Grey,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                } else {
                    CommentList(
                        commentList = commentResponse,
                        onSendReply = { replyText, commentId, replyTo ->
                            onSendReply(replyText, commentId, replyTo)
                            inputText = ""
                        },
                        onReplyClick = { replyItem ->
                            replyingTo = replyItem
                        }
                    )
                }
            }

            CommentTextField(
                modifier = Modifier.fillMaxWidth(),
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
}

@Preview
@Composable
private fun CommentBottomSheetPreview() {
    ThipTheme {
        var showSheet by remember { mutableStateOf(true) }
        if (showSheet) {
            CommentBottomSheet(
                commentResponse = listOf(mockComment, mockComment, mockComment),
                onDismiss = { showSheet = false },
                onSendReply = { _, _, _ -> }
            )
        }
    }
}