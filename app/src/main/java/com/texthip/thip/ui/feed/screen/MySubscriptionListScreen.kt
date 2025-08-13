package com.texthip.thip.ui.feed.screen

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.texthip.thip.R
import com.texthip.thip.data.model.users.FollowingDto
import com.texthip.thip.ui.common.header.AuthorHeader
import com.texthip.thip.ui.common.modal.ToastWithDate
import com.texthip.thip.ui.common.topappbar.DefaultTopAppBar
import com.texthip.thip.ui.feed.viewmodel.MySubscriptionUiState
import com.texthip.thip.ui.feed.viewmodel.MySubscriptionViewModel
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography
import com.texthip.thip.utils.color.hexToColor
import kotlinx.coroutines.delay

@Composable
fun MySubscriptionScreen(
    navController: NavController,
    viewModel: MySubscriptionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val lazyListState = rememberLazyListState()
    val context = LocalContext.current

    val isScrolledToEnd by remember {
        derivedStateOf {
            val layoutInfo = lazyListState.layoutInfo
            if (layoutInfo.totalItemsCount == 0) return@derivedStateOf false
            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisibleItemIndex >= layoutInfo.totalItemsCount - 1
        }
    }

    LaunchedEffect(isScrolledToEnd) {
        if (isScrolledToEnd && !uiState.isLoading && !uiState.isLastPage) {
            viewModel.fetchMyFollowings()
        }
    }

    MySubscriptionContent(
        uiState = uiState,
        lazyListState = lazyListState,
        onNavigateBack = { navController.popBackStack() },
        onToggleFollow = { userId, nickname ->
            val followedMessage = context.getString(R.string.toast_thip, nickname)
            val unfollowedMessage = context.getString(R.string.toast_thip_cancel, nickname)
            viewModel.toggleFollow(userId, followedMessage, unfollowedMessage)
        },
        onHideToast = { viewModel.hideToast() }
    )
}
@Composable
fun MySubscriptionContent(
    uiState: MySubscriptionUiState,
    lazyListState: LazyListState,
    onNavigateBack: () -> Unit,
    onToggleFollow: (userId: Int, nickname: String) -> Unit,
    onHideToast: () -> Unit
) {
    LaunchedEffect(uiState.showToast) {
        if (uiState.showToast) {
            delay(2000)
            onHideToast()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (uiState.showToast) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(1f)
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 15.dp, vertical = 15.dp),
            ) {
                ToastWithDate(
                    message = uiState.toastMessage,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Column(
            Modifier
                .background(colors.Black)
                .fillMaxSize()
        ) {
            DefaultTopAppBar(
                onLeftClick = onNavigateBack,
                title = stringResource(R.string.my_thip_list)
            )
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(40.dp))
                Text(
                    text = stringResource(R.string.whole_num, uiState.totalCount),
                    style = typography.menu_m500_s14_h24,
                    color = colors.Grey,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, bottom = 4.dp)
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    color = colors.DarkGrey02,
                    thickness = 1.dp
                )

                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 20.dp)
                ) {
                    itemsIndexed(
                        items = uiState.followings,
                        key = { _, user -> user.userId }
                    ) { index, user ->
                        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                            AuthorHeader(
                                profileImage = user.profileImageUrl,
                                nickname = user.nickname,
                                badgeText = user.aliasName,
                                badgeTextColor = hexToColor(user.aliasColor),
                                buttonText = stringResource(if (user.isFollowing) R.string.thip_cancel else R.string.thip),
                                buttonWidth = 64.dp,
                                profileImageSize = 36.dp,
                                onButtonClick = { onToggleFollow(user.userId, user.nickname) }
                            )

                            if (index < uiState.followings.lastIndex) {
                                Spacer(modifier = Modifier.height(16.dp))
                                HorizontalDivider(
                                    color = colors.DarkGrey02,
                                    thickness = 1.dp
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }

                    if (uiState.isLoading && !uiState.isLastPage) {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Spacer(modifier = Modifier.weight(1f))
                                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}
@Preview
@Composable
private fun MySubscriptionListScreenPrev() {
    val mockUsers = (1..10).map {
        FollowingDto(
            userId = it,
            profileImageUrl = null,
            nickname = "문학소년 $it",
            aliasName = if (it % 3 == 0) "공식 인플루언서" else "글쓰는 탐험가",
            aliasColor = if (it % 3 == 0) "#00C7B2" else "#FFD600",
            isFollowing = true
        )
    }

    ThipTheme {
        MySubscriptionContent(
            uiState = MySubscriptionUiState(
                isLoading = false,
                followings = mockUsers,
                totalCount = 25,
                isLastPage = false,
                showToast = false
            ),
            lazyListState = rememberLazyListState(),
            onNavigateBack = {},
            onToggleFollow = { _, _ -> },
            onHideToast = {}
        )
    }
}