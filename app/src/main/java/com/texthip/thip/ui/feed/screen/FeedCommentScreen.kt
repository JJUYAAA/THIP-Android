package com.texthip.thip.ui.feed.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.bottomsheet.MenuBottomSheet
import com.texthip.thip.ui.common.buttons.ActionBookButton
import com.texthip.thip.ui.common.buttons.OptionChipButton
import com.texthip.thip.ui.common.forms.CommentTextField
import com.texthip.thip.ui.common.header.ProfileBar
import com.texthip.thip.ui.common.modal.DialogPopup
import com.texthip.thip.ui.common.topappbar.DefaultTopAppBar
import com.texthip.thip.ui.group.note.component.*
import com.texthip.thip.ui.group.note.mock.mockCommentList
import com.texthip.thip.ui.group.room.mock.MenuBottomSheetItem
import com.texthip.thip.ui.mypage.mock.FeedItem
import com.texthip.thip.ui.theme.ThipTheme
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
    onSendClick: () -> Unit = {},
    commentList: SnapshotStateList<FeedCommentItem> = remember { mutableStateListOf() }
) {
    var isBottomSheetVisible by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    val commentInput = remember { mutableStateOf("") }
    val replyTo = remember { mutableStateOf<String?>(null) }
    val feed = remember { mutableStateOf(feedItem) }
    val justNow = stringResource(R.string.just_a_moment_ago)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .blur(if (showDialog) 10.dp else 0.dp)
    ) {
        DefaultTopAppBar(
            isRightIconVisible = true,
            isTitleVisible = false,
            onLeftClick = {},
            onRightClick = { isBottomSheetVisible = true },
        )
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 56.dp),
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
            if (commentList.isEmpty()) {
                item {
                     Column (
                        modifier = Modifier
                            .fillMaxWidth(),
                         verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.no_comments_yet),
                            style = typography.smalltitle_sb600_s18_h24,
                            color = colors.White
                        )
                         Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.no_comment_subtext),
                            style = typography.copy_r400_s14,
                            color = colors.Grey
                        )
                    }
                }
            }else{
                commentList.forEachIndexed{ index, commentItem ->
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 24.dp)
                        ) {
                            CommentItem(
                                data = commentItem,
                                onReplyClick = { replyTo.value = it },

                                )
                            Spacer(modifier = Modifier.height(24.dp))
                            commentItem.replyList.forEach { reply ->
                                ReplyItem(
                                    data = reply,
                                    onReplyClick = { replyTo.value = it }
                                )
                                Spacer(modifier = Modifier.height(24.dp))
                            }
                            if (index == commentList.lastIndex) {
                                Spacer(modifier = Modifier.height(40.dp))
                            }
                        }
                    }
                }
            }
        }
        CommentTextField(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            input = commentInput.value,
            hint = stringResource(R.string.feed_reply_to, feedItem.userName),
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
                    } else {
                        // 대댓글
                        val parentIndex =
                            commentList.indexOfFirst { it.nickName == replyTargetNickname }
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
                            commentList[parentIndex] =
                                parentComment.copy(replyList = updatedReplies)
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
    if (isBottomSheetVisible) {
        MenuBottomSheet(
            items = listOf(
                MenuBottomSheetItem(
                    text = stringResource(R.string.edit_feed),
                    color = colors.White,
                    onClick = { }
                ),
                MenuBottomSheetItem(
                    text = stringResource(R.string.delete_feed),
                    color = colors.Red,
                    onClick = {
                        isBottomSheetVisible = false
                        showDialog = true
                    }
                )
            ),
            onDismiss = {
                isBottomSheetVisible = false
            }
        )
    }
    if (showDialog) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(enabled = true, onClick = { showDialog = false })
        ) {
            Box(
                modifier = Modifier.align(Alignment.Center)
            ) {
                DialogPopup(
                    title = stringResource(R.string.delete_feed_dialog_title),
                    description = stringResource(R.string.delete_feed_dialog_description),
                    onConfirm = {
                        showDialog = false
                        isBottomSheetVisible = false
                    },
                    onCancel = {
                        showDialog = false
                        isBottomSheetVisible = false
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun FeedCommentScreenPrev() {
    ThipTheme {
        val mockFeedItem =  FeedItem(
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
            imageUrls = listOf(R.drawable.bookcover_sample),
            tags = listOf("에세이", "문학", "힐링")
        )
        val commentList = remember { mutableStateListOf<FeedCommentItem>() }

        FeedCommentScreen(
            feedItem = mockFeedItem,
            bookImage = painterResource(R.drawable.bookcover_sample),
            profileImage = painterResource(R.drawable.character_literature),
            currentUserId = 999,
            currentUserName = "나",
            currentUserGenre = "문학",
            currentUserProfileImageUrl = "",
            commentList = commentList
        )
    }
}
@Preview
@Composable
private fun FeedCommentScreenWithMockComments() {
    ThipTheme {
        val mockFeedItem =  FeedItem(
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
            imageUrls = listOf(R.drawable.bookcover_sample),
            tags = listOf("에세이", "문학", "힐링")
        )
        val commentList = remember {
            mutableStateListOf<FeedCommentItem>().apply {
                addAll(mockCommentList.commentData)
            }
        }

        FeedCommentScreen(
            feedItem = mockFeedItem,
            bookImage = painterResource(R.drawable.bookcover_sample),
            profileImage = painterResource(R.drawable.character_literature),
            currentUserId = 999,
            currentUserName = "나",
            currentUserGenre = "문학",
            currentUserProfileImageUrl = "",
            commentList = commentList
        )
    }
}