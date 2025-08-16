package com.texthip.thip.ui.feed.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.texthip.thip.R
import com.texthip.thip.ui.common.header.AuthorHeader
import com.texthip.thip.ui.common.topappbar.DefaultTopAppBar
import com.texthip.thip.ui.feed.component.FeedSubscribeBarlist
import com.texthip.thip.ui.feed.viewmodel.FeedOthersViewModel
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography
import com.texthip.thip.utils.color.hexToColor

@Composable
fun FeedOthersScreen(
    onNavigateBack: () -> Unit,
    viewModel: FeedOthersViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val userInfo = uiState.userInfo

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            DefaultTopAppBar(
                isRightIconVisible = false,
                isTitleVisible = false,
                onLeftClick = onNavigateBack,
            )

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (userInfo != null) {
                // 스크롤 영역
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(32.dp))
                        AuthorHeader(
                            profileImage = userInfo.profileImageUrl,
                            nickname = userInfo.nickname,
                            badgeText = userInfo.aliasName,
                            badgeTextColor = hexToColor(userInfo.aliasColor),
                            buttonText = if (userInfo.isFollowing) stringResource(R.string.thip_cancel) else stringResource(R.string.thip),
                            // TODO: 띱하기/취소하기 로직 연결
                            onButtonClick = {},
                            modifier = Modifier.padding(bottom = 20.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        FeedSubscribeBarlist(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            followerProfileImageUrls = userInfo.latestFollowerProfileImageUrls,
                            onClick = {}
                        )
                        Spacer(modifier = Modifier.height(40.dp))
                        Text(
                            text = stringResource(R.string.whole_num, userInfo.totalFeedCount),
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
                    if (userInfo.totalFeedCount == 0) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 244.dp),
                                contentAlignment = Alignment.TopCenter
                            ) {
                                Text(
                                    text = stringResource(R.string.empty_feed),
                                    style = typography.smalltitle_sb600_s18_h24,
                                    color = colors.White
                                )
                            }
                        }
                    } else {
                        // TODO: 피드 아이템 리스트 불러오기
//                        itemsIndexed(feedStateList, key = { _, item -> item.id }) { index, feed ->
//                            Spacer(modifier = Modifier.height(if (index == 0) 20.dp else 40.dp))
//                            MyFeedCard(
//                                feedItem = feed,
//                                onLikeClick = {
//                                    val updated = feed.copy(
//                                        isLiked = !feed.isLiked,
//                                        likeCount = if (feed.isLiked) feed.likeCount - 1 else feed.likeCount + 1
//                                    )
//                                    feedStateList[index] = updated
//                                },
//                                onContentClick = {} //TODO FeedCommentScreen으로
//                            )
//                            Spacer(modifier = Modifier.height(40.dp))
//                            if (index != feedStateList.lastIndex) {
//                                HorizontalDivider(
//                                    color = colors.DarkGrey02,
//                                    thickness = 10.dp
//                                )
//                            }
//                        }
                    }
                }
            }
        }
    }
}

//@Preview
//@Composable
//private fun FeedOthersScreenPrev() {
//    ThipTheme {
//        val mockFeeds = List(5) {
//            FeedItem(
//                id = it + 1,
//                userProfileImage = R.drawable.character_literature,
//                userName = "user.$it",
//                userRole = "문학 칭호",
//                bookTitle = "책 제목 ",
//                authName = "한강",
//                timeAgo = "1시간 전",
//                content = "내용내용내용 입니다. 내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.",
//                likeCount = it,
//                commentCount = it,
//                isLiked = false,
//                isSaved = false,
//                isLocked = it % 2 == 0,
//                imageUrls = listOf(R.drawable.img_book_cover_sample)
//            )
//        }
//        val mockFollowerImages = listOf(
//            "https://example.com/image1.jpg",
//            "https://example.com/image2.jpg",
//            "https://example.com/image3.jpg",
//            "https://example.com/image4.jpg",
//            "https://example.com/image5.jpg"
//        )
//        ThipTheme {
//            FeedOthersScreen(
//                nickname = "ThipUser01",
//                userRole = "문학 칭호",
//                feeds = mockFeeds,
//                totalFeedCount = mockFeeds.size,
//                followerProfileImageUrls = mockFollowerImages,
//                onNavigateBack = {}
//            )
//        }
//    }
//}
//
//@Preview
//@Composable
//private fun FeedOthersScreenWithoutFeedPrev() {
//    ThipTheme {
//        val mockFeeds: List<FeedItem> = emptyList()
//        val mockFollowerImages = emptyList<String>()
//
//        ThipTheme {
//            FeedOthersScreen(
//                nickname = "ThipUser01",
//                userRole = "문학 칭호",
//                feeds = mockFeeds,
//                totalFeedCount = mockFeeds.size,
//                followerProfileImageUrls = mockFollowerImages
//            )
//        }
//    }
//}
