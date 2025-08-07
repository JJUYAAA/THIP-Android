package com.texthip.thip.ui.group.note.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.header.ProfileBarFeed
import com.texthip.thip.ui.group.note.mock.ReplyItem
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun ReplyItem(
    modifier: Modifier = Modifier,
    data: ReplyItem,
    onReplyClick: (String) -> Unit = { }
) {
    var isLiked by remember { mutableStateOf(data.isLiked) }

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_reply),
            contentDescription = null,
            tint = Color.White
        )

        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ProfileBarFeed(
//            profileImage = data.profileImageUrl,
                profileImage = painterResource(R.drawable.character_literature),
                nickname = data.nickName,
                genreName = data.genreName,
                genreColor = colors.SocialScience,
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
                                append(stringResource(R.string.annotation) + data.parentNickname + stringResource(R.string.space_bar))
                            }
                            append(data.content)
                        },
                        color = colors.Grey,
                        style = typography.feedcopy_r400_s14_h20,
                    )
                    Text(
                        modifier = Modifier.clickable(onClick = { onReplyClick(data.nickName) }),
                        text = stringResource(R.string.write_reply),
                        style = typography.menu_sb600_s12,
                        color = colors.Grey02,
                    )
                }

                Column(
                    modifier = Modifier.clickable(onClick = { isLiked = !isLiked }),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    Icon(
                        painter = painterResource(if (isLiked) R.drawable.ic_heart_center_filled else R.drawable.ic_heart_center),
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
                data = ReplyItem(
                    replyId = 1,
                    userId = 1,
                    nickName = "user.01",
                    parentNickname = "사용자태그",
                    genreName = "칭호칭호",
                    profileImageUrl = "https://example.com/profile.jpg",
                    content = "입력하세요. 댓글 내용을 입력하세요오. 댓글 내용을 입력하세요. 댓글 내용을 입력하세요. 댓글 내용을 입력하세요. 댓글 내용을 입력하세요. 댓글 내용을 입력하세요. 댓글 내용을 입력하세요. ",
                    postDate = "12시간 전",
                    isWriter = false,
                    isLiked = true,
                    likeCount = 10,
                )
            )
        }
    }
}