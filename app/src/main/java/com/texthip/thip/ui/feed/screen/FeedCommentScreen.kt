package com.texthip.thip.ui.feed.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.texthip.thip.R
import com.texthip.thip.ui.common.bottomsheet.MenuBottomSheet
import com.texthip.thip.ui.common.buttons.ActionBarButton
import com.texthip.thip.ui.common.buttons.ActionBookButton
import com.texthip.thip.ui.common.buttons.OptionChipButton
import com.texthip.thip.ui.common.forms.CommentTextField
import com.texthip.thip.ui.common.header.ProfileBar
import com.texthip.thip.ui.common.modal.DialogPopup
import com.texthip.thip.ui.common.modal.ToastWithDate
import com.texthip.thip.ui.common.topappbar.DefaultTopAppBar
import com.texthip.thip.ui.feed.component.ImageViewerModal
import com.texthip.thip.ui.feed.viewmodel.FeedDetailViewModel
import com.texthip.thip.ui.group.note.component.CommentSection
import com.texthip.thip.ui.group.note.viewmodel.CommentsEvent
import com.texthip.thip.ui.group.note.viewmodel.CommentsViewModel
import com.texthip.thip.ui.group.room.mock.MenuBottomSheetItem
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography
import com.texthip.thip.utils.rooms.advancedImePadding
import kotlinx.coroutines.delay

private data class CommentActionTarget(val commentId: Int, val isWriter: Boolean)

@Composable
fun FeedCommentScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    feedId: Long,
    onNavigateBack: () -> Unit = {},
    onNavigateToFeedEdit: (Int) -> Unit = {},
    onNavigateToUserProfile: (userId: Long) -> Unit = {},
    onNavigateToBookDetail: (String) -> Unit = {},
    feedDetailViewModel: FeedDetailViewModel = hiltViewModel(),
    commentsViewModel: CommentsViewModel = hiltViewModel()
) {
    val feedDetailUiState by feedDetailViewModel.uiState.collectAsState()
    val commentsUiState by commentsViewModel.uiState.collectAsState()

    LaunchedEffect(feedDetailUiState.feedDetail) {
        feedDetailUiState.feedDetail?.let { detail ->
            //커스텀객체 타입 인식오류 -> 직렬화가 아닌 잘게 쪼개어 전달
                navController.previousBackStackEntry?.savedStateHandle?.let { handle ->
                handle.set("updated_feed_id", detail.feedId.toLong())
                handle.set("updated_feed_isLiked", detail.isLiked)
                handle.set("updated_feed_likeCount", detail.likeCount)
                handle.set("updated_feed_isSaved", detail.isSaved)
            }
        }
    }

    LaunchedEffect(feedDetailUiState.deleteSuccess) {
        if (feedDetailUiState.deleteSuccess) {
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set("deleted_feed_id", feedId)

            onNavigateBack()
        }
    }
    LaunchedEffect(feedId) {
        feedDetailViewModel.loadFeedDetail(feedId)
        commentsViewModel.initialize(postId = feedId.toLong(), postType = "FEED")
    }

    // 로딩 상태 처리
    if (feedDetailUiState.isLoading) {
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
    if (feedDetailUiState.error != null) {
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
                    text = feedDetailUiState.error!!,
                    style = typography.copy_r400_s14,
                    color = colors.Grey
                )
            }
        }
        return
    }

    // 피드 데이터가 없으면 리턴
    val feedDetail = feedDetailUiState.feedDetail ?: return
    var isPostMenuVisible by remember { mutableStateOf(false) }
    var isCommentMenuVisible by remember { mutableStateOf(false) }
    var selectedCommentForMenu by remember { mutableStateOf<CommentActionTarget?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showToast by remember { mutableStateOf(false) }

    LaunchedEffect(showToast) {
        if (showToast) {
            delay(3000)
            showToast = false
        }
    }

    val images = feedDetail.contentUrls
    var showImageViewer by remember { mutableStateOf(false) }
    var selectedImageIndex by remember { mutableStateOf(0) }

    var commentInput by remember { mutableStateOf("") }
    var replyingToCommentId by remember { mutableStateOf<Int?>(null) }
    var replyingToNickname by remember { mutableStateOf<String?>(null) }

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val listState = rememberLazyListState()

    val isKeyboardVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0

    LaunchedEffect(isKeyboardVisible) {
        if (!isKeyboardVisible) {
            replyingToCommentId = null
            replyingToNickname = null
            focusManager.clearFocus()
        }
    }

    val isOverlayVisible = isPostMenuVisible || isCommentMenuVisible || showDeleteDialog || showImageViewer

    Box(
        modifier = Modifier
            .fillMaxSize()
            .advancedImePadding()
    ) {
        Box(
            modifier = if (isOverlayVisible) {
                Modifier
                    .fillMaxSize()
                    .blur(5.dp)
            } else {
                Modifier.fillMaxSize()
            }
                // 바깥 터치 시 키보드 숨기기
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                }
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                DefaultTopAppBar(
                    isRightIconVisible = true,
                    isTitleVisible = false,
                    onLeftClick = onNavigateBack,
                    onRightClick = { isPostMenuVisible = true },
                )

                LazyColumn(
                    state = listState,
                    modifier = modifier
                        .fillMaxWidth()
                        .weight(1f),
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
                                hoursAgo = feedDetail.postDate,
                                onClick = { onNavigateToUserProfile(feedDetail.creatorId) }
                            )
                            Column(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp, horizontal = 20.dp)
                            ) {
                                ActionBookButton(
                                    bookTitle = feedDetail.bookTitle,
                                    bookAuthor = feedDetail.bookAuthor,
                                    onClick = {
                                        onNavigateToBookDetail(feedDetail.isbn)
                                    }
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
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
                                color = colors.DarkGrey02, thickness = 1.dp
                            )

                            ActionBarButton(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp),
                                isLiked = feedDetail.isLiked,
                                likeCount = feedDetail.likeCount,
                                commentCount = feedDetail.commentCount,
                                isSaveVisible = true,
                                isSaved = feedDetail.isSaved,
                                isPinVisible = false,
                                isLockIcon = feedDetail.isPublic == false,
                                onLikeClick = { feedDetailViewModel.changeFeedLike() },
                                onBookmarkClick = { feedDetailViewModel.changeFeedSave() },
                            )

                            HorizontalDivider(
                                modifier = Modifier.padding(top = 16.dp),
                                color = colors.DarkGrey03,
                                thickness = 10.dp
                            )
                        }
                    }
                    when {
                        commentsUiState.isLoading -> {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 40.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(color = colors.White)
                                }
                            }
                        }
                        // 댓글 없음
                        commentsUiState.comments.isEmpty() -> {
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
                        }

                        else -> {
                            items(
                                items = commentsUiState.comments,
                                key = { comment -> comment.commentId ?: comment.hashCode() }
                            ) { commentItem ->
                                CommentSection(
                                    commentItem = commentItem,
                                    onEvent = commentsViewModel::onEvent,
                                    onReplyClick = { commentId, nickname ->
                                        replyingToCommentId = commentId
                                        replyingToNickname = nickname
                                        focusRequester.requestFocus()
                                    },
                                    onCommentLongPress = { comment ->
                                        selectedCommentForMenu = CommentActionTarget(comment.commentId!!, comment.isWriter)
                                        isCommentMenuVisible = true
                                    },
                                    onReplyLongPress = { reply ->
                                        selectedCommentForMenu = CommentActionTarget(reply.commentId, reply.isWriter)
                                        isCommentMenuVisible = true
                                    },
                                    onProfileClick = onNavigateToUserProfile
                                )
                            }
                        }
                    }
                }

                // 댓글 입력창
                CommentTextField(
                    modifier = Modifier.focusRequester(focusRequester),
                    input = commentInput,
                    hint = stringResource(R.string.reply_to),
                    onInputChange = { commentInput = it },
                    onSendClick = {
                        if (commentInput.isNotBlank()) {
                            commentsViewModel.onEvent(
                                CommentsEvent.CreateComment(
                                    content = commentInput,
                                    parentId = replyingToCommentId
                                )
                            )
                            commentInput = ""
                            replyingToCommentId = null
                            replyingToNickname = null
                            focusManager.clearFocus()
                        }
                    },
                    replyTo = replyingToNickname,
                    onCancelReply = {
                        replyingToCommentId = null
                        replyingToNickname = null
                    }
                )
            }

            // 신고 완료 토스트
            AnimatedVisibility(
                visible = showToast,
                enter = slideInVertically(
                    initialOffsetY = { -it },
                    animationSpec = tween(durationMillis = 1000)
                ),
                exit = slideOutVertically(
                    targetOffsetY = { -it },
                    animationSpec = tween(durationMillis = 1000)
                ),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .zIndex(3f)
            ) {
                ToastWithDate(
                    message = stringResource(R.string.report_complete_feed)
                )
            }
        }

        if (isPostMenuVisible) {
            val menuItems = if (feedDetail.isWriter) {
                // 내 피드인 경우: 수정, 삭제
                listOf(
                    MenuBottomSheetItem(
                        text = stringResource(R.string.edit_feed),
                        color = colors.White,
                        onClick = {
                            isPostMenuVisible = false
                            onNavigateToFeedEdit(feedDetail.feedId)
                        }
                    ),
                    MenuBottomSheetItem(
                        text = stringResource(R.string.delete_feed),
                        color = colors.Red,
                        onClick = {
                            isPostMenuVisible = false
                            showDeleteDialog = true
                        }
                    )
                )
            } else {
                listOf(
                    MenuBottomSheetItem(
                        text = stringResource(R.string.report),
                        color = colors.Red,
                        onClick = {
                            isPostMenuVisible = false
                            showToast = true
                        }
                    )
                )
            }
            MenuBottomSheet(
                items = menuItems,
                onDismiss = { isPostMenuVisible = false }
            )
        }

        if (isCommentMenuVisible && selectedCommentForMenu != null) {
            val comment = selectedCommentForMenu!!
            val menuItems = if (comment.isWriter) {
                listOf(
                    MenuBottomSheetItem(
                        text = stringResource(R.string.delete),
                        color = colors.Red,
                        onClick = {
                            commentsViewModel.onEvent(CommentsEvent.DeleteComment(comment.commentId))
                            isCommentMenuVisible = false
                        }
                    )
                )
            } else {
                // 다른 사람 피드인 경우: 신고만
                listOf(
                    MenuBottomSheetItem(
                        text = stringResource(R.string.report),
                        color = colors.Red,
                        onClick = {
                            showToast = true
                            isCommentMenuVisible = false
                        }
                    )
                )
            }

            MenuBottomSheet(
                items = menuItems,
                onDismiss = { isCommentMenuVisible = false }
            )
        }

        if (showDeleteDialog) {
            Box(
                Modifier
                    .fillMaxSize()
                    .clickable(enabled = false, onClick = {})
            ) {
                Box(Modifier.align(Alignment.Center)) {
                    DialogPopup(
                        title = stringResource(R.string.delete_feed_dialog_title),
                        description = stringResource(R.string.delete_feed_dialog_description),
                        onConfirm = {
                            showDeleteDialog = false
                            feedDetailViewModel.deleteFeed(feedId)
                        },
                        onCancel = {
                            showDeleteDialog = false
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
}
