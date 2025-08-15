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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.texthip.thip.ui.mypage.component.SavedFeedCard
import com.texthip.thip.ui.mypage.mock.FeedItem
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun FeedScreen(
    onNavigateToMySubscription: () -> Unit = {},
    onNavigateToFeedWrite: () -> Unit = {},
    nickname: String = "",
    userRole: String = "",
    feeds: List<FeedItem> = emptyList(),
    totalFeedCount: Int = 0,
    selectedTabIndex: Int = 0,
    followerProfileImageUrls: List<String> = emptyList(),
    feedViewModel: FeedViewModel = hiltViewModel()
    resultFeedId: Int? = null,
    onResultConsumed: () -> Unit = {},
    mySubscriptionViewModel: MySubscriptionViewModel = hiltViewModel()
) {
    val feedUiState by feedViewModel.uiState.collectAsState()
    val selectedIndex = rememberSaveable { mutableIntStateOf(selectedTabIndex) }
    val feedStateList = remember {
        mutableStateListOf<FeedItem>().apply {
            addAll(feeds)
        }
    }
    val scope = rememberCoroutineScope()
    
    var showProgressBar by remember { mutableStateOf(false) }
    val progress = remember { Animatable(0f) }
    
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
    val subscriptionUiState by viewModel.uiState.collectAsState()
    Box(modifier = Modifier.fillMaxSize()) {
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
                selectedTabIndex = selectedIndex.value,
                onTabSelected = { selectedIndex.value = it }
            )

            // 스크롤 영역 전체
            LazyColumn(
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
                if (selectedIndex.value == 1) {
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
                            text = stringResource(R.string.whole_num, totalFeedCount),
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

                    if (totalFeedCount == 0) {
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
                        itemsIndexed(feedStateList, key = { _, item -> item.id }) { index, feed ->
                            Spacer(modifier = Modifier.height(if (index == 0) 20.dp else 40.dp))
                            MyFeedCard(
                                feedItem = feed,
                                onLikeClick = {
                                    val updated = feed.copy(
                                        isLiked = !feed.isLiked,
                                        likeCount = if (feed.isLiked) feed.likeCount - 1 else feed.likeCount + 1
                                    )
                                    feedStateList[index] = updated
                                },
                                onContentClick = {} //TODO FeedCommentScreen으로
                            )
                            Spacer(modifier = Modifier.height(40.dp))
                            if (index != feeds.lastIndex) {
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
                    itemsIndexed(feedStateList, key = { _, item -> item.id }) { index, feed ->
                        val profileImage = feed.userProfileImage?.let { painterResource(it) }

                        SavedFeedCard(
                            feedItem = feed,
                            onBookmarkClick = {
                                val updated = feed.copy(isSaved = !feed.isSaved)
                                feedStateList[index] = updated
                            },
                            onLikeClick = {
                                val updated = feed.copy(
                                    isLiked = !feed.isLiked,
                                    likeCount = if (feed.isLiked) feed.likeCount - 1 else feed.likeCount + 1
                                )
                                feedStateList[index] = updated
                            },
                            onContentClick = {} //FeedCommentScreen으로

                        )
                        if (index != feeds.lastIndex) {
                            HorizontalDivider(
                                color = colors.DarkGrey02,
                                thickness = 10.dp
                            )
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
                userProfileImage = R.drawable.character_literature,
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
                imageUrls = listOf(R.drawable.img_book_cover_sample)
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
