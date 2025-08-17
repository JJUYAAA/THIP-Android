package com.texthip.thip.ui.feed.screen

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
import androidx.compose.runtime.mutableStateListOf
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
import com.texthip.thip.R
import com.texthip.thip.ui.common.buttons.FloatingButton
import com.texthip.thip.ui.common.header.AuthorHeader
import com.texthip.thip.ui.common.header.HeaderMenuBarTab
import com.texthip.thip.ui.common.topappbar.LogoTopAppBar
import com.texthip.thip.ui.feed.component.FeedSubscribeBarlist
import com.texthip.thip.ui.feed.component.MyFeedCard
import com.texthip.thip.ui.feed.component.MySubscribeBarlist
import com.texthip.thip.ui.feed.mock.MySubscriptionData
import com.texthip.thip.ui.feed.viewmodel.FeedViewModel
import com.texthip.thip.ui.feed.viewmodel.MySubscriptionViewModel
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
    onNavigateToFeedComment: (Int) -> Unit = {},
    nickname: String = "",
    userRole: String = "",
    feeds: List<FeedItem> = emptyList(),
    totalFeedCount: Int = 0,
    selectedTabIndex: Int = 0,
    followerProfileImageUrls: List<String> = emptyList(),
    resultFeedId: Int? = null,
    onResultConsumed: () -> Unit = {},
    feedViewModel: FeedViewModel = hiltViewModel(),
    mySubscriptionViewModel: MySubscriptionViewModel = hiltViewModel()
) {
    val feedUiState by feedViewModel.uiState.collectAsState()
    val feedStateList = remember {
        mutableStateListOf<FeedItem>().apply {
            addAll(feeds)
        }
    }
    val scope = rememberCoroutineScope()
    var showProgressBar by remember { mutableStateOf(false) }
    val progress = remember { Animatable(0f) }
    
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

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            feedViewModel.loadMoreFeeds()
        }
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

    val mySubscriptions = listOf(
        MySubscriptionData(
            profileImageUrl = "https://example.com/image1.jpg",
            nickname = "abcabcabcabc",
            role = "문학가",
            roleColor = colors.SocialScience
        ),
        MySubscriptionData(
            profileImageUrl = "https://example.com/image.jpg",
            nickname = "aaaaaaa",
            role = "작가",
            roleColor = colors.SocialScience
        ),
        MySubscriptionData(
            profileImageUrl = "https://example.com/image1.jpg",
            nickname = "abcabcabcabc",
            role = "문학가",
            roleColor = colors.SocialScience
        ),
        MySubscriptionData(
            profileImageUrl = "https://example.com/image.jpg",
            nickname = "aaaaaaa",
            role = "작가",
            roleColor = colors.SocialScience
        ),
        MySubscriptionData(
            profileImageUrl = "https://example.com/image1.jpg",
            nickname = "abcabcabcabc",
            role = "문학가",
            roleColor = colors.SocialScience
        ),
        MySubscriptionData(
            profileImageUrl = "https://example.com/image.jpg",
            nickname = "aaaaaaa",
            role = "작가",
            roleColor = colors.SocialScience
        ),
        MySubscriptionData(
            profileImageUrl = "https://example.com/image1.jpg",
            nickname = "abcabcabcabc",
            role = "문학가",
            roleColor = colors.SocialScience
        ),
        MySubscriptionData(
            profileImageUrl = "https://example.com/image.jpg",
            nickname = "aaaaaaa",
            role = "작가",
            roleColor = colors.SocialScience
        )
    )
    val subscriptionUiState by mySubscriptionViewModel.uiState.collectAsState()
    
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
                titles = listOf("피드", "내 피드"),
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
                        AuthorHeader(
                            profileImage = null,
                            nickname = nickname,
                            badgeText = userRole,
                            buttonText = "",
                            buttonWidth = 60.dp,
                            showButton = false
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        FeedSubscribeBarlist(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            followerProfileImageUrls = followerProfileImageUrls,
                            onClick = {
                            }
                        )
                        Spacer(modifier = Modifier.height(40.dp))
                        Text(
                            text = stringResource(R.string.whole_num, feedUiState.myFeeds.size),
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
                                id = myFeed.feedId,
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
                                }
                            )
                            Spacer(modifier = Modifier.height(40.dp))
                            if (index != feedUiState.myFeeds.lastIndex) {
                                HorizontalDivider(
                                    color = colors.DarkGrey02,
                                    thickness = 10.dp
                                )
                            }
                        }
                    }
                } else {
                    //피드
                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                        val subscriptionsForBar = feedUiState.recentWriters.map { user ->
                            MySubscriptionData(
                                profileImageUrl = user.profileImageUrl,
                                nickname = user.nickname,
                                role = "",
                                roleColor = colors.White,
                                subscriberCount = 0,
                                isSubscribed = true
                            )
                        }
                        MySubscribeBarlist(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            subscriptions = subscriptionsForBar,
                            onClick = onNavigateToMySubscription
                        )
                    }
                    itemsIndexed(feedUiState.allFeeds, key = { _, item -> item.feedId }) { index, allFeed ->
                        // AllFeedItem을 FeedItem으로 변환
                        val feedItem = FeedItem(
                            id = allFeed.feedId,
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

                        SavedFeedCard(
                            feedItem = feedItem,
                            bottomTextColor = hexToColor(allFeed.aliasColor),
                            onBookmarkClick = {
                                // TODO: API 호출로 북마크 상태 변경
                            },
                            onLikeClick = {
                                // TODO: API 호출로 좋아요 상태 변경
                            },
                            onContentClick = {
                                onNavigateToFeedComment(feedItem.id)
                            }
                        )
                        if (index != feedUiState.allFeeds.lastIndex) {
                            HorizontalDivider(
                                color = colors.DarkGrey02,
                                thickness = 10.dp
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
        val mockFeeds = List(5) {
            FeedItem(
                id = it + 1,
                userProfileImage = "https://example.com/profile$it.jpg",
                userName = "user.$it",
                userRole = "문학 칭호",
                bookTitle = "책 제목 ",
                authName = "한강",
                timeAgo = "1시간 전",
                content = "내용내용내용 입니다. 내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.",
                likeCount = it,
                commentCount = it,
                isLiked = false,
                isSaved = false,
                isLocked = it % 2 == 0,
                imageUrls = listOf("https://example.com/image$it.jpg")
            )
        }
        val mockFollowerImages = listOf(
            "https://example.com/image1.jpg",
            "https://example.com/image2.jpg",
            "https://example.com/image3.jpg",
            "https://example.com/image4.jpg",
            "https://example.com/image5.jpg"
        )
        ThipTheme {
            FeedScreen(
                nickname = "ThipUser01",
                userRole = "문학 칭호",
                selectedTabIndex = 1,
                feeds = mockFeeds,
                totalFeedCount = mockFeeds.size,
                followerProfileImageUrls = mockFollowerImages,
                onNavigateToFeedWrite = { }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FeedScreenWithoutDataPreview() {
    ThipTheme {
        val mockFeeds: List<FeedItem> = emptyList()
        val mockFollowerImages = emptyList<String>()

        ThipTheme {
            FeedScreen(
                nickname = "ThipUser01",
                userRole = "문학 칭호",
                selectedTabIndex = 0,
                feeds = mockFeeds,
                totalFeedCount = mockFeeds.size,
                followerProfileImageUrls = mockFollowerImages,
                onNavigateToFeedWrite = { }
            )
        }
    }
}
