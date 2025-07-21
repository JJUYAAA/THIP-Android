package com.texthip.thip.ui.feed.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.buttons.ActionBookButton
import com.texthip.thip.ui.common.buttons.OptionChipButton
import com.texthip.thip.ui.common.forms.CommentTextField
import com.texthip.thip.ui.common.header.ProfileBar
import com.texthip.thip.ui.common.topappbar.DefaultTopAppBar
import com.texthip.thip.ui.group.note.component.CommentItem
import com.texthip.thip.ui.group.note.component.ReplyItem
import com.texthip.thip.ui.mypage.mock.FeedItem
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography
import com.texthip.thip.ui.group.note.mock.CommentItem as FeedCommentItem
import com.texthip.thip.ui.group.note.mock.ReplyItem as FeedReplyItem

@Composable
fun FeedCommentScreen(
    modifier: Modifier = Modifier,
    feedItem: FeedItem,
    bookImage: Painter? = null,
    profileImage: Painter? = null,
    currentUserId: Int,
    currentUserName: String,
    currentUserGenre: String,
    currentUserProfileImageUrl: String,
    onLikeClick: () -> Unit = {},
    onCommentInputChange: (String) -> Unit = {},
    onSendClick: () -> Unit = {}
) {
    val commentInput = remember { mutableStateOf("") }
    val replyTo = remember { mutableStateOf<String?>(null) }
    val feed = remember { mutableStateOf(feedItem) }
    val justNow = stringResource(R.string.just_a_moment_ago)
    val commentList = remember {
        mutableStateListOf(
            FeedCommentItem(
                commentId = 1,
                userId = 1,
                nickName = "사용자1",
                genreName = "문학",
                profileImageUrl = "",
                content = "너무 공감돼요!",
                postDate = "1시간 전",
                isWriter = false,
                isLiked = false,
                likeCount = 3,
                replyList = listOf(
                    FeedReplyItem(
                        replyId = 2,
                        userId = 2,
                        nickName = "사용자2",
                        parentNickname = "사용자1",
                        genreName = "문학",
                        profileImageUrl = "",
                        content = "저도 그렇게 느꼈어요.",
                        postDate = "30분 전",
                        isWriter = false,
                        isLiked = false,
                        likeCount = 1
                    )
                )
            )
        )
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        DefaultTopAppBar(
            isRightIconVisible = true,
            isTitleVisible = false,
            onLeftClick = {},
            onRightClick = {},
        )
        LazyColumn(
            modifier = modifier.fillMaxWidth().padding(top = 56.dp),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    ProfileBar(
                        profileImage = profileImage,
                        topText = feedItem.userName,
                        bottomText = feedItem.userRole,
                        showSubscriberInfo = false,
                        hoursAgo = feedItem.timeAgo
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 16.dp)
                    ) {
                        ActionBookButton(
                            bookTitle = feedItem.bookTitle,
                            bookAuthor = feedItem.authName,
                            onClick = {}
                        )
                    }
                    Text(
                        text = feedItem.content,
                        style = typography.feedcopy_r400_s14_h20,
                        color = colors.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )
                    if (bookImage != null) {
                        Image(
                            painter = bookImage,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(480.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                    if (feedItem.tags.isNotEmpty()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            feedItem.tags.forEach { tag ->
                                OptionChipButton(
                                    text = tag,
                                    isFilled = false,
                                    isSelected = false,
                                    onClick = {}
                                )
                            }
                        }
                    }
                    HorizontalDivider(
                        color = colors.DarkGrey02,
                        thickness = 1.dp,
                    )
                    Row(
                        modifier = Modifier.padding(top = 16.dp, bottom = 30.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier.clickable {
                                feed.value = feed.value.copy(
                                    isLiked = !feed.value.isLiked,
                                    likeCount = if (feed.value.isLiked) feed.value.likeCount - 1 else feed.value.likeCount + 1
                                )
                                onLikeClick()
                            },
                            painter = painterResource(if (feedItem.isLiked) R.drawable.ic_heart_filled else R.drawable.ic_heart),
                            contentDescription = null,
                            tint = Color.Unspecified
                        )
                        Text(
                            text = feedItem.likeCount.toString(),
                            style = typography.feedcopy_r400_s14_h20,
                            color = colors.White,
                            modifier = Modifier.padding(start = 5.dp, end = 12.dp)
                        )
                        Icon(
                            painter = painterResource(R.drawable.ic_comment),
                            contentDescription = null,
                            tint = colors.White
                        )
                        Text(
                            text = feedItem.commentCount.toString(),
                            style = typography.feedcopy_r400_s14_h20,
                            color = colors.White,
                            modifier = Modifier.padding(start = 5.dp, end = 12.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        if (feedItem.isLocked) {
                            Icon(
                                painter = painterResource(R.drawable.ic_lock),
                                contentDescription = null,
                                tint = Color.Unspecified
                            )
                        }
                    }
                }
                HorizontalDivider(
                    color = colors.DarkGrey02,
                    thickness = 10.dp
                )
                Spacer(modifier = Modifier.height(40.dp))
            }
            commentList.forEach { commentItem ->
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 24.dp)
                    ) {
                        CommentItem(
                            data = commentItem,
                            onReplyClick = { replyTo.value = it }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        commentItem.replyList.forEach { reply ->
                            ReplyItem(
                                data = reply,
                                onReplyClick = { replyTo.value = it }
                            )
                        }
                    }
                }
            }
        }
        CommentTextField(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            input = commentInput.value,
            hint =stringResource(R.string.feed_reply_to, feedItem.userName),
            onInputChange = {
                commentInput.value = it
                onCommentInputChange(it)
            },
            onSendClick = {
                if (commentInput.value.isNotBlank()) {
                    val replyTargetNickname = replyTo.value
                    if (replyTargetNickname == null) {

                        commentList.add(
                            FeedCommentItem(
                                commentId = commentList.size + 1,
                                userId = currentUserId,
                                nickName = currentUserName,
                                genreName = currentUserGenre,
                                profileImageUrl = currentUserProfileImageUrl,
                                content = commentInput.value,
                                postDate = justNow,
                                isWriter = true,
                                isLiked = false,
                                likeCount = 0,
                                replyList = emptyList()
                            )
                        )
                    }else {
                        // 대댓글
                        val parentIndex = commentList.indexOfFirst { it.nickName == replyTargetNickname }
                        if (parentIndex != -1) {
                            val parentComment = commentList[parentIndex]
                            val newReply = FeedReplyItem(
                                replyId = parentComment.replyList.size + 1,
                                userId = currentUserId,
                                nickName = currentUserName,
                                parentNickname = replyTargetNickname,
                                genreName = currentUserGenre,
                                profileImageUrl = currentUserProfileImageUrl,
                                content = commentInput.value,
                                postDate = justNow,
                                isWriter = true,
                                isLiked = false,
                                likeCount = 0
                            )
                            val updatedReplies = parentComment.replyList + newReply
                            commentList[parentIndex] = parentComment.copy(replyList = updatedReplies)
                        }
                    }
                    commentInput.value = ""
                    replyTo.value = null
                    onSendClick()
                }
            },
            replyTo = replyTo.value,
            onCancelReply = { replyTo.value = null }
        )

    }

}

@Preview
@Composable
private fun FeedCommentScreenPrev() {
    FeedCommentScreen(
        feedItem = FeedItem(
            id = 1,
            userProfileImage = R.drawable.character_literature,
            userName = "문학소녀",
            userRole = "문학 칭호",
            bookTitle = "채식주의자",
            authName = "한강",
            timeAgo = "1시간 전",
            content = "이 책은 인간의 본성과 억압에 대한 깊은 성찰을 담고 있어요. 인상 깊은 문장이 많았어요.",
            likeCount = 12,
            commentCount = 3,
            isLiked = true,
            isSaved = false,
            isLocked = true,
            imageUrl = R.drawable.bookcover_sample,
            tags = listOf("에세이", "문학", "힐링")
        ),
        bookImage = painterResource(R.drawable.bookcover_sample),
        profileImage = painterResource(R.drawable.character_literature),
        onLikeClick = {},
        onCommentInputChange = {},
        onSendClick = {},
        currentUserId = 999,
        currentUserName = "나",
        currentUserGenre = "장르",
        currentUserProfileImageUrl = ""
    )
}