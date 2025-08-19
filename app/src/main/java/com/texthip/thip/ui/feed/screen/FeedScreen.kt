package com.texthip.thip.ui.feed.screen

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.texthip.thip.R
import com.texthip.thip.ui.common.buttons.FloatingButton
import com.texthip.thip.ui.common.header.AuthorHeader
import com.texthip.thip.ui.common.header.HeaderMenuBarTab
import com.texthip.thip.ui.common.topappbar.LogoTopAppBar
import com.texthip.thip.ui.feed.component.FeedSubscribeBarlist
import com.texthip.thip.ui.feed.component.MyFeedCard
import com.texthip.thip.ui.feed.component.MySubscribeBarlist
import com.texthip.thip.ui.feed.viewmodel.FeedViewModel
import com.texthip.thip.ui.mypage.component.SavedFeedCard
import com.texthip.thip.ui.mypage.mock.FeedItem
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography
import com.texthip.thip.utils.color.hexToColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    onNavigateToMySubscription: () -> Unit = {},
    onNavigateToFeedWrite: () -> Unit = {},
    onNavigateToFeedComment: (Long) -> Unit = {},
    onNavigateToBookDetail: (String) -> Unit = {},
    resultFeedId: Long? = null,
    onNavigateToUserProfile: (userId: Long) -> Unit = {},
    onNavigateToSearchPeople: () -> Unit = {},
    onResultConsumed: () -> Unit = {},
    navController: NavHostController,
    feedViewModel: FeedViewModel = hiltViewModel(),
) {
    val feedUiState by feedViewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    var showProgressBar by remember { mutableStateOf(false) }
    val progress = remember { Animatable(0f) }
    
    val feedTabTitles = listOf(stringResource(R.string.feed), stringResource(R.string.my_feed))

    // 무한 스크롤 로직
    val listState = rememberLazyListState()

    // 무한 스크롤 로직
    val shouldLoadMore by remember(feedUiState.canLoadMoreCurrentTab, feedUiState.isLoadingMore) {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

            feedUiState.canLoadMoreCurrentTab &&
                    !feedUiState.isLoadingMore &&
                    feedUiState.currentTabFeeds.isNotEmpty() &&
                    totalItems > 0 &&
                    lastVisibleIndex >= totalItems - 3
        }
    }
    LaunchedEffect(Unit) {
        navController.currentBackStackEntry?.savedStateHandle?.let { handle ->
            handle.getLiveData<Long>("deleted_feed_id").observeForever { deletedId ->
                if (deletedId != null) {
                    feedViewModel.removeDeletedFeed(deletedId)
                    handle.remove<Long>("deleted_feed_id")
                }
            }
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            feedViewModel.loadMoreFeeds()
        }
    }
    
    LaunchedEffect(Unit) {
        feedViewModel.refreshData()
    }

    LaunchedEffect(resultFeedId) {
        if (resultFeedId != null) {
            onResultConsumed()

            showProgressBar = true
            progress.snapTo(0f)
            scope.launch {
                progress.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
                )
                delay(500)
                if (showProgressBar) {
                    showProgressBar = false
                }
            }
        }
    }

    // 초기 로딩 상태 처리
    if (feedUiState.isLoading && feedUiState.currentTabFeeds.isEmpty()) {
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

    Box(modifier = Modifier.fillMaxSize()) {
        PullToRefreshBox(
            isRefreshing = feedUiState.isRefreshing,
            onRefresh = { feedViewModel.refreshCurrentTab() }
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                LogoTopAppBar(
                    leftIcon = painterResource(R.drawable.ic_plusfriend),
                    hasNotification = false,
                    onLeftClick = {},
                    onRightClick = {},
                )
                Spacer(modifier = Modifier.height(32.dp))
                HeaderMenuBarTab(
                    titles = feedTabTitles,
                    selectedTabIndex = feedUiState.selectedTabIndex,
                    onTabSelected = feedViewModel::onTabSelected
                )

                // 스크롤 영역 전체
                LazyColumn(
                    state = listState,
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        AnimatedVisibility(visible = showProgressBar) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 20.dp, end = 20.dp, top = 32.dp),
                            ) {
                                Text(
                                    modifier = Modifier.padding(bottom = 12.dp),
                                    text = if (progress.value < 1.0f) {
                                        stringResource(R.string.posting_in_progress_feed)
                                    } else {
                                        stringResource(R.string.posting_complete_feed)
                                    },
                                    style = typography.view_m500_s14,
                                    color = colors.NeonGreen
                                )

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(color = colors.Grey02) // 트랙(배경) 색상
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(fraction = progress.value)
                                            .fillMaxHeight()
                                            .background(
                                                color = colors.NeonGreen,
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                    )
                                }
                            }
                        }
                    }
                    
                    if (feedUiState.selectedTabIndex == 1) {
                        // 내 피드
                        item {
                            Spacer(modifier = Modifier.height(32.dp))

                            val myFeedInfo = feedUiState.myFeedInfo
                            AuthorHeader(
                                profileImage = myFeedInfo?.profileImageUrl,
                                nickname = myFeedInfo?.nickname ?: "",
                                badgeText = myFeedInfo?.aliasName ?: "",
                                badgeTextColor = myFeedInfo?.aliasColor?.let { hexToColor(it) } ?: colors.NeonGreen,
                                buttonText = "",
                                buttonWidth = 60.dp,
                                showButton = false
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            FeedSubscribeBarlist(
                                modifier = Modifier.padding(horizontal = 20.dp),
                                followerProfileImageUrls = myFeedInfo?.latestFollowerProfileImageUrls ?: emptyList(),
                                onClick = {
                                }
                            )
                            Spacer(modifier = Modifier.height(40.dp))
                            Text(
                                text = stringResource(R.string.whole_num, myFeedInfo?.totalFeedCount ?: 0),
                                style = typography.menu_m500_s14_h24,
                                color = colors.Grey,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp, start = 20.dp)
                            )
                            HorizontalDivider(
                                color = colors.DarkGrey02,
                                thickness = 1.dp,
                                modifier = Modifier.padding(horizontal = 20.dp)
                            )
                        }

                        if (feedUiState.myFeeds.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 244.dp),
                                    contentAlignment = Alignment.TopCenter
                                ) {
                                    Text(
                                        text = stringResource(R.string.create_feed),
                                        style = typography.smalltitle_sb600_s18_h24,
                                        color = colors.White
                                    )
                                }
                            }
                        } else {
                            itemsIndexed(feedUiState.myFeeds, key = { _, item -> item.feedId }) { index, myFeed ->
                                Spacer(modifier = Modifier.height(if (index == 0) 20.dp else 40.dp))

                                // MyFeedItem을 FeedItem으로 변환
                                val feedItem = FeedItem(
                                    id = myFeed.feedId.toLong(),
                                    userProfileImage = null,
                                    userName = "", // 내 피드이므로 고정값
                                    userRole = "", // 내 피드이므로 고정값
                                    bookTitle = myFeed.bookTitle,
                                    authName = myFeed.bookAuthor,
                                    timeAgo = myFeed.postDate,
                                    content = myFeed.contentBody,
                                    likeCount = myFeed.likeCount,
                                    commentCount = myFeed.commentCount,
                                    isLiked = false, // 내 피드는 좋아요 개념 없음
                                    isSaved = false, // 내 피드는 저장 개념 없음
                                    isLocked = !myFeed.isPublic, // isPublic의 반대값
                                    tags = emptyList(),
                                    imageUrls = myFeed.contentUrls
                                )

                                MyFeedCard(
                                    feedItem = feedItem,
                                    onLikeClick = {},
                                    onContentClick = {
                                        onNavigateToFeedComment(feedItem.id)
                                    },
                                    onBookClick = {
                                        onNavigateToBookDetail(myFeed.isbn)
                                    }
                                )
                                Spacer(modifier = Modifier.height(40.dp))
                                if (index != feedUiState.myFeeds.lastIndex) {
                                    HorizontalDivider(
                                        color = colors.DarkGrey02,
                                        thickness = 6.dp
                                    )
                                }
                            }
                        }
                    } else {
                        //피드
                        item {
                            Spacer(modifier = Modifier.height(20.dp))
                            MySubscribeBarlist(
                                modifier = Modifier.padding(horizontal = 20.dp),
                                subscriptions = feedUiState.recentWriters,
                                onClick = onNavigateToMySubscription
                            )
                        }
                        itemsIndexed(feedUiState.allFeeds, key = { _, item -> item.feedId }) { index, allFeed ->
                            // AllFeedItem을 FeedItem으로 변환
                            val feedItem = FeedItem(
                                id = allFeed.feedId.toLong(),
                                userProfileImage = allFeed.creatorProfileImageUrl,
                                userName = allFeed.creatorNickname,
                                userRole = allFeed.aliasName,
                                bookTitle = allFeed.bookTitle,
                                authName = allFeed.bookAuthor,
                                timeAgo = allFeed.postDate,
                                content = allFeed.contentBody,
                                likeCount = allFeed.likeCount,
                                commentCount = allFeed.commentCount,
                                isLiked = allFeed.isLiked,
                                isSaved = allFeed.isSaved,
                                isLocked = false,
                                tags = emptyList(),
                                imageUrls = allFeed.contentUrls
                            )

                            Spacer(modifier = Modifier.height(if (index == 0) 20.dp else 40.dp))

                            SavedFeedCard(
                                feedItem = feedItem,
                                bottomTextColor = hexToColor(allFeed.aliasColor),
                                onBookmarkClick = {
                                    feedViewModel.changeFeedSave(feedItem.id)
                                },
                                onLikeClick = {
                                    feedViewModel.changeFeedLike(feedItem.id)
                                },
                                onContentClick = {
                                    onNavigateToFeedComment(feedItem.id)
                                },
                                onCommentClick = {
                                    onNavigateToFeedComment(feedItem.id)
                                },
                                onBookClick = {
                                    onNavigateToBookDetail(allFeed.isbn)
                                },
                                onProfileClick = {
                                    onNavigateToUserProfile(allFeed.creatorId)
                                }
                            )
                            Spacer(modifier = Modifier.height(40.dp))
                            if (index != feedUiState.allFeeds.lastIndex) {
                                HorizontalDivider(
                                    color = colors.DarkGrey02,
                                    thickness = 6.dp
                                )
                            }
                        }
                    }

                    // 무한 스크롤 로딩 인디케이터
                    if (feedUiState.isLoadingMore) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = colors.White
                                )
                            }
                        }
                    }
                }
            }
        }
        FloatingButton(
            icon = painterResource(id = R.drawable.ic_write),
            onClick = onNavigateToFeedWrite
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FeedScreenPreview() {
    ThipTheme {
        FeedScreen(
            onNavigateToFeedWrite = { },
            onNavigateToBookDetail = { },
            navController = rememberNavController()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FeedScreenWithoutDataPreview() {
    ThipTheme {
        FeedScreen(
            onNavigateToFeedWrite = { },
            onNavigateToBookDetail = { },
            navController = rememberNavController()
        )
    }
}