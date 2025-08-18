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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.data.model.comments.response.ReplyList
import com.texthip.thip.ui.common.CommentActionMode
import com.texthip.thip.ui.common.header.ProfileBarFeed
import com.texthip.thip.ui.feed.component.CommentActionPopup
import com.texthip.thip.ui.group.note.viewmodel.CommentsEvent
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography
import com.texthip.thip.utils.color.hexToColor

@Composable
fun ReplyItem(
    modifier: Modifier = Modifier,
    data: ReplyList,
    onReplyClick: () -> Unit = { },
    onLikeClick: () -> Unit = {},
    onLongPress: () -> Unit = {},
    actionMode: CommentActionMode,
    isSelected: Boolean = false,
    onDismissPopup: () -> Unit = {},
    onEvent: (CommentsEvent) -> Unit = { _ -> }
) {
    Box {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_reply),
                contentDescription = null,
                tint = colors.White
            )

            Column(
                modifier = modifier.pointerInput(Unit) {
                    detectTapGestures(onLongPress = { onLongPress() })
                },
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ProfileBarFeed(
                    profileImage = data.creatorProfileImageUrl,
                    nickname = data.creatorNickname,
                    genreName = data.aliasName,
                    genreColor = hexToColor(data.aliasColor),
                    date = data.postDate
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = typography.copy_m500_s14_h20.copy(color = colors.White)
                                        .toSpanStyle()
                                ) {
                                    append(
                                        stringResource(R.string.annotation) + data.parentCommentCreatorNickname + stringResource(
                                            R.string.space_bar
                                        )
                                    )
                                }
                                append(data.content)
                            },
                            color = colors.Grey,
                            style = typography.feedcopy_r400_s14_h20,
                        )
                        Text(
                            modifier = Modifier.clickable(onClick = onReplyClick),
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
                        onEvent(CommentsEvent.DeleteComment(data.commentId))
                    } else {
                        // TODO: 신고 로직
                    }
                    onDismissPopup()
                },
                onDismissRequest = onDismissPopup // 바깥 클릭 시 팝업 닫기
            )
        }
    }
}

@Preview
@Composable
private fun ReplyItemPreview() {
    ThipTheme {
        Column(
            modifier = Modifier
                .clickable { /* TODO: Handle click */ }
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ReplyItem(
                data = ReplyList(
                    commentId = 1,
                    parentCommentCreatorNickname = "User1",
                    creatorId = 2,
                    creatorNickname = "User1",
                    aliasName = "칭호칭호",
                    aliasColor = "#FF5733",
                    creatorProfileImageUrl = "https://example.com/image2.jpg",
                    content = "This is a reply.",
                    postDate = "2023-10-01T12:05:00Z",
                    isLike = false,
                    isWriter = false,
                    likeCount = 5
                ),
                actionMode = CommentActionMode.POPUP,
            )
        }
    }
}