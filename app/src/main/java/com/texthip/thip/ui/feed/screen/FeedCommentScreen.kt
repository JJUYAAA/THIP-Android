package com.texthip.thip.ui.feed.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.texthip.thip.R
import com.texthip.thip.ui.common.bottomsheet.MenuBottomSheet
import com.texthip.thip.ui.common.buttons.ActionBookButton
import com.texthip.thip.ui.common.buttons.OptionChipButton
import com.texthip.thip.ui.common.forms.CommentTextField
import com.texthip.thip.ui.common.header.ProfileBar
import com.texthip.thip.ui.common.modal.DialogPopup
import com.texthip.thip.ui.common.topappbar.DefaultTopAppBar
import com.texthip.thip.ui.feed.component.ImageViewerModal
import com.texthip.thip.ui.feed.viewmodel.FeedDetailViewModel
import com.texthip.thip.ui.group.note.mock.mockCommentList
import com.texthip.thip.ui.group.room.mock.MenuBottomSheetItem
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography
import com.texthip.thip.ui.group.note.mock.CommentItem as FeedCommentItem
import com.texthip.thip.ui.group.note.mock.ReplyItem as FeedReplyItem

@Composable
fun FeedCommentScreen(
    modifier: Modifier = Modifier,
    feedId: Int,
    onNavigateBack: () -> Unit = {},
    currentUserId: Int = 1,
    currentUserName: String = "현재사용자",
    currentUserGenre: String = "문학",
    currentUserProfileImageUrl: String = "",
    onLikeClick: () -> Unit = {},
    onCommentInputChange: (String) -> Unit = {},
    onSendClick: () -> Unit = {},
    commentList: SnapshotStateList<FeedCommentItem>? = null,
    viewModel: FeedDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(feedId) {
        viewModel.loadFeedDetail(feedId)
    }
    
    // 로딩 상태 처리
    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = colors.White,
                modifier = Modifier.size(48.dp)
            )
        }
        return
    }
    
    // 에러 상태 처리
    if (uiState.error != null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "오류가 발생했습니다",
                    style = typography.smalltitle_sb600_s18_h24,
                    color = colors.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = uiState.error!!,
                    style = typography.copy_r400_s14,
                    color = colors.Grey
                )
            }
        }
        return
    }
    
    // 피드 데이터가 없으면 리턴
    val feedDetail = uiState.feedDetail ?: return
    val CommentList = commentList ?: remember { mutableStateListOf<FeedCommentItem>() }
    var isBottomSheetVisible by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    val commentInput = remember { mutableStateOf("") }
    val replyTo = remember { mutableStateOf<String?>(null) }
    val feed = remember { mutableStateOf(feedDetail) }
    val justNow = stringResource(R.string.just_a_moment_ago)

    val images = feedDetail.contentUrls
    var showImageViewer by remember { mutableStateOf(false) }
    var selectedImageIndex by remember { mutableStateOf(0) }

    var selectedComment by remember { mutableStateOf<FeedCommentItem?>(null) }
    var selectedReply by remember { mutableStateOf<FeedReplyItem?>(null) }

    Box(
        modifier = if (isBottomSheetVisible || showDialog) {
            Modifier
                .fillMaxSize()
                .blur(5.dp)
        } else {
            Modifier.fillMaxSize()
        }
            //바깥 터치 시 선택 해제 되도록
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    selectedComment = null
                    selectedReply = null
                })
            }
    ) {
        DefaultTopAppBar(
            isRightIconVisible = true,
            isTitleVisible = false,
            onLeftClick = onNavigateBack,
            onRightClick = { isBottomSheetVisible = true },
        )

        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 56.dp),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            // 상단 피드
            item {
                Column {
                    ProfileBar(
                        modifier = Modifier.padding(20.dp),
                        profileImage = feedDetail.creatorProfileImageUrl ?: "",
                        topText = feedDetail.creatorNickname,
                        bottomText = feedDetail.aliasName,
                        showSubscriberInfo = false,
                        hoursAgo = feedDetail.postDate
                    )
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp, horizontal = 20.dp)
                    ) {
                        ActionBookButton(
                            bookTitle = feedDetail.bookTitle,
                            bookAuthor = feedDetail.bookAuthor,
                            onClick = {}
                        )
                    }
                    Text(
                        text = feedDetail.contentBody,
                        style = typography.feedcopy_r400_s14_h20,
                        color = colors.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp, start = 20.dp, end = 20.dp)
                    )
                    if (images.isNotEmpty()) {
                        LazyRow(
                            Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, bottom = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            itemsIndexed(images.take(3)) { index, imageUrl ->
                                AsyncImage(
                                    model = imageUrl,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(end = 16.dp)
                                        .size(200.dp)
                                        .clickable {
                                            selectedImageIndex = index
                                            showImageViewer = true
                                        },
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                    if (feedDetail.tagList.isNotEmpty()) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp, start = 20.dp, end = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            feedDetail.tagList.forEach { tag ->
                                OptionChipButton(
                                    text = "#$tag",
                                    isFilled = false,
                                    isSelected = false,
                                    onClick = {})
                            }
                        }
                    }
                    HorizontalDivider(color = colors.DarkGrey03, thickness = 10.dp)
                }
            }
            //댓글이 없는 경우
            if (CommentList.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
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
            } else {
                CommentList.forEachIndexed { index, commentItem ->
                    item {
                        Spacer(modifier = Modifier.height(40.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                        ) {
                            // 댓글 아이템
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .pointerInput(Unit) {
                                        detectTapGestures(
                                            onLongPress = {
                                                selectedComment = commentItem
                                                selectedReply = null
                                            }
                                        )
                                    }
                            ) {
//                                CommentItem(
//                                    data = commentItem,
//                                    onReplyClick = { replyTo.value = it }
//                                )
                            }
                            if (selectedComment == commentItem) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    val isMyComment = commentItem.userId == currentUserId
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                Color.Transparent,
                                                RoundedCornerShape(12.dp)
                                            )
                                            .border(
                                                width = 1.dp,
                                                color = colors.White,
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                            .clickable {
                                                if (isMyComment) {
                                                    //TODO 삭제 로직
                                                } else {
                                                    //TODO 신고 로직
                                                }
                                                selectedComment = null
                                            }
                                            .padding(horizontal = 20.dp, vertical = 12.dp)
                                    ) {
                                        Text(
                                            text = stringResource(if (isMyComment) R.string.delete else R.string.report),
                                            color = if (isMyComment) colors.White else colors.Red,
                                            style = typography.feedcopy_r400_s14_h20
                                        )
                                    }
                                }
                            }
                        }

                        // 대댓글들
                        Spacer(modifier = Modifier.height(24.dp))
                        commentItem.replyList.forEach { reply ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp)
                            ) {
                                // 대댓글 아이템
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .pointerInput(Unit) {
                                            detectTapGestures(
                                                onLongPress = {
                                                    selectedReply = reply
                                                    selectedComment = null
                                                }
                                            )
                                        }
                                ) {
//                                    ReplyItem(data = reply, onReplyClick = { replyTo.value = it })
                                }

                                if (selectedReply == reply) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        val isMyReply = reply.userId == currentUserId
                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    Color.Transparent,
                                                    RoundedCornerShape(12.dp)
                                                )
                                                .border(
                                                    width = 1.dp,
                                                    color = colors.White,
                                                    shape = RoundedCornerShape(12.dp)
                                                )
                                                .clickable {
                                                    if (isMyReply) {
                                                        //TODO 삭제 로직
                                                    } else {
                                                        //TODO 신고 로직
                                                    }
                                                    selectedReply = null
                                                }
                                                .padding(horizontal = 20.dp, vertical = 12.dp)
                                        ) {
                                            Text(
                                                text = stringResource(if (isMyReply) R.string.delete else R.string.report),
                                                color = if (isMyReply) colors.White else colors.Red,
                                                style = typography.feedcopy_r400_s14_h20
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(24.dp))
                            }
                        }


                        if (index == CommentList.lastIndex) {
                            Spacer(modifier = Modifier.height(40.dp))
                        }
                    }
                }
            }
        }

        // 댓글 입력창
        CommentTextField(
            modifier = Modifier.align(Alignment.BottomCenter),
            input = commentInput.value,
            hint = stringResource(R.string.feed_reply_to, feedDetail.creatorNickname),
            onInputChange = {
                commentInput.value = it
                onCommentInputChange(it)
            },
            onSendClick = {
                if (commentInput.value.isNotBlank()) {
                    val replyTargetNickname = replyTo.value
                    if (replyTargetNickname == null) {
                        CommentList.add(
                            FeedCommentItem(
                                commentId = CommentList.size + 1,
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
                        val parentIndex =
                            CommentList.indexOfFirst { it.nickName == replyTargetNickname }
                        if (parentIndex != -1) {
                            val parentComment = CommentList[parentIndex]
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
                            CommentList[parentIndex] =
                                parentComment.copy(replyList = parentComment.replyList + newReply)
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
                    onClick = {}
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
            onDismiss = { isBottomSheetVisible = false }
        )
    }

    if (showDialog) {
        Box(
            Modifier
                .fillMaxSize()
                .clickable { showDialog = false }) {
            Box(Modifier.align(Alignment.Center)) {
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

    if (showImageViewer && images.isNotEmpty()) {
        ImageViewerModal(
            imageUrls = images.take(3),
            initialIndex = selectedImageIndex,
            onDismiss = { showImageViewer = false }
        )
    }
}

@Preview
@Composable
private fun FeedCommentScreenWithMockComments() {
    ThipTheme {
        val commentList = remember {
            mutableStateListOf<FeedCommentItem>().apply {
                addAll(mockCommentList.commentData)
            }
        }
        FeedCommentScreen(
            feedId = 1,
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
private fun FeedCommentScreenPrev() {
    ThipTheme {
        val commentList = remember { mutableStateListOf<FeedCommentItem>() }

        FeedCommentScreen(
            feedId = 1,
            currentUserId = 999,
            currentUserName = "나",
            currentUserGenre = "문학",
            currentUserProfileImageUrl = "",
            commentList = commentList
        )
    }
}
