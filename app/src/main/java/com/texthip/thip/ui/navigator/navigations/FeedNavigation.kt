package com.texthip.thip.ui.navigator.navigations

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.texthip.thip.ui.feed.screen.FeedCommentScreen
import com.texthip.thip.ui.feed.screen.FeedMyScreen
import com.texthip.thip.ui.feed.screen.FeedOthersScreen
import com.texthip.thip.ui.feed.screen.FeedScreen
import com.texthip.thip.ui.feed.screen.FeedWriteScreen
import com.texthip.thip.ui.feed.screen.MySubscriptionScreen
import com.texthip.thip.ui.feed.screen.OthersSubscriptionListScreen
import com.texthip.thip.ui.feed.screen.SearchPeopleScreen
import com.texthip.thip.ui.feed.viewmodel.FeedViewModel
import com.texthip.thip.ui.feed.viewmodel.FeedWriteViewModel
import com.texthip.thip.ui.navigator.extensions.navigateToAlarm
import com.texthip.thip.ui.navigator.extensions.navigateToBookDetail
import com.texthip.thip.ui.navigator.extensions.navigateToFeedComment
import com.texthip.thip.ui.navigator.extensions.navigateToFeedWrite
import com.texthip.thip.ui.navigator.extensions.navigateToMySubscription
import com.texthip.thip.ui.navigator.extensions.navigateToOthersSubscription
import com.texthip.thip.ui.navigator.extensions.navigateToSearchPeople
import com.texthip.thip.ui.navigator.extensions.navigateToUserProfile
import com.texthip.thip.ui.navigator.routes.FeedRoutes
import com.texthip.thip.ui.navigator.routes.MainTabRoutes

// Feed
fun NavGraphBuilder.feedNavigation(
    navController: NavHostController, 
    navigateBack: () -> Unit,
    onFeedTabReselected: Int = 0
) {
    composable<MainTabRoutes.Feed> { backStackEntry ->
        val feedViewModel: FeedViewModel = hiltViewModel(backStackEntry)
        val uiState by feedViewModel.uiState.collectAsState()
        val myUserId = uiState.myFeedInfo?.creatorId

        val onNavigateToUserProfile: (Long) -> Unit = { clickedUserId ->
            if (myUserId != null && myUserId == clickedUserId) {
                // 내 프로필일 경우 FeedMyScreen으로 이동
                navController.navigate(FeedRoutes.My)
            } else {
                // 다른 사람 프로필일 경우 FeedOthersScreen으로 이동
                navController.navigate(FeedRoutes.Others(clickedUserId))
            }
        }

        val resultFeedId = backStackEntry.savedStateHandle.get<Long>("feedId")
        val refreshFeed = backStackEntry.savedStateHandle.get<Boolean>("refreshFeed")

        FeedScreen(
            feedViewModel = feedViewModel,
            navController = navController,
            resultFeedId = resultFeedId,
            refreshFeed = refreshFeed,
            onFeedTabReselected = onFeedTabReselected,
            onResultConsumed = {
                backStackEntry.savedStateHandle.remove<Long>("feedId")
            },
            onRefreshConsumed = {
                backStackEntry.savedStateHandle.remove<Boolean>("refreshFeed")
            },
            onNavigateToMySubscription = {
                navController.navigateToMySubscription()
            },
            onNavigateToFeedWrite = {
                navController.navigateToFeedWrite()
            },
            onNavigateToFeedComment = { feedId ->
                navController.navigateToFeedComment(feedId)
            },
            onNavigateToBookDetail = { isbn ->
                navController.navigateToBookDetail(isbn)
            },
            onNavigateToUserProfile = onNavigateToUserProfile,
            onNavigateToSearchPeople = {
                navController.navigateToSearchPeople()
            },
            onNavigateToNotification = {
                navController.navigateToAlarm()
            },
            onNavigateToOthersSubscription = { userId ->
                navController.navigateToOthersSubscription(userId)
            }
        )
    }
    composable<FeedRoutes.MySubscription> {
        val feedViewModel: FeedViewModel = hiltViewModel(navController.getBackStackEntry(MainTabRoutes.Feed))
        val uiState by feedViewModel.uiState.collectAsState()
        val myUserId = uiState.myFeedInfo?.creatorId

        MySubscriptionScreen(
            onNavigateBack = {
                navigateBack()
            },
            onNavigateToUserProfile = { userId ->
                if (myUserId != null && myUserId == userId) {
                    navController.navigate(FeedRoutes.My)
                } else {
                    navController.navigate(FeedRoutes.Others(userId))
                }
            }
        )
    }
    composable<FeedRoutes.Write> { backStackEntry ->
        val route = backStackEntry.toRoute<FeedRoutes.Write>()
        val viewModel: FeedWriteViewModel = hiltViewModel()

        LaunchedEffect(route) {
            if (
                route.feedId != null &&
                route.editContentBody != null &&
                route.isbn != null &&
                route.bookTitle != null &&
                route.bookAuthor != null
            ) {
                viewModel.setEditData(
                    feedId = route.feedId.toLong(),
                    isbn = route.isbn,
                    bookTitle = route.bookTitle,
                    bookAuthor = route.bookAuthor,
                    bookImageUrl = route.bookImageUrl ?: "",
                    contentBody = route.editContentBody,
                    isPublic = route.editIsPublic ?: true,
                    tagList = route.editTagList ?: emptyList()
                )
            } else if (route.feedId != null) {
                // 수정 모드: 기존 방식 (API 호출)
                viewModel.loadFeedForEdit(route.feedId.toLong())
            } else if (route.isbn != null &&
                route.bookTitle != null &&
                route.bookAuthor != null &&
                route.bookImageUrl != null &&
                route.recordContent != null
            ) {
                // 새 글 작성 모드: 기록장에서 온 데이터 설정
                viewModel.setPinnedRecord(
                    isbn = route.isbn,
                    bookTitle = route.bookTitle,
                    bookAuthor = route.bookAuthor,
                    bookImageUrl = route.bookImageUrl,
                    recordContent = route.recordContent
                )
            } else if (route.isbn != null &&
                route.bookTitle != null &&
                route.bookAuthor != null
            ) {
                // 새 글 작성 모드: 책 정보만 있는 경우 (책 상세 페이지에서 온 경우)
                viewModel.selectBook(
                    com.texthip.thip.ui.group.makeroom.mock.BookData(
                        title = route.bookTitle,
                        imageUrl = route.bookImageUrl ?: "",
                        author = route.bookAuthor,
                        isbn = route.isbn
                    )
                )
            }
        }

        FeedWriteScreen(
            viewModel = viewModel,
            onNavigateBack = { navigateBack() },
            onFeedCreated = { feedId ->
                // 피드 생성 성공 시 결과를 저장하고 피드 목록으로 돌아가기
                navController.getBackStackEntry(MainTabRoutes.Feed)
                    .savedStateHandle["feedId"] = feedId
                navController.getBackStackEntry(MainTabRoutes.Feed)
                    .savedStateHandle["refreshFeed"] = true
                navController.popBackStack(MainTabRoutes.Feed, inclusive = false)
            }
        )
    }
    composable<FeedRoutes.My> {
        val feedViewModel: FeedViewModel = hiltViewModel(navController.getBackStackEntry(MainTabRoutes.Feed))
        FeedMyScreen(
            viewModel = feedViewModel,
            onNavigateBack = navigateBack,
            onNavigateToSubscriptionList = { userId ->
                navController.navigateToOthersSubscription(userId)
            },
            onNavigateToFeedComment = { feedId ->
                navController.navigateToFeedComment(feedId)
            }
        )
    }
    composable<FeedRoutes.Others> { backStackEntry ->
        // 다른 유저의 피드 화면
        FeedOthersScreen(
            onNavigateBack = {
                navigateBack()
            },
            onNavigateToSubscriptionList = { userId ->
                navController.navigateToOthersSubscription(userId)
            },
            onNavigateToFeedComment = { feedId ->
                navController.navigateToFeedComment(feedId)
            }
        )
    }
    composable<FeedRoutes.Comment> { backStackEntry ->
        val route = backStackEntry.toRoute<FeedRoutes.Comment>()
        val feedViewModel: FeedViewModel = hiltViewModel(navController.getBackStackEntry(MainTabRoutes.Feed))
        val uiState by feedViewModel.uiState.collectAsState()
        val myUserId = uiState.myFeedInfo?.creatorId

        FeedCommentScreen(
            feedId = route.feedId,
            navController = navController,
            onNavigateBack = {
                navController.popBackStack()
            },
            onNavigateToFeedEdit = { feedId ->
                navController.navigate(FeedRoutes.Write(feedId = feedId))
            },
            onNavigateToUserProfile = { userId ->
                if (myUserId != null && myUserId == userId) {
                    navController.navigate(FeedRoutes.My)
                } else {
                    navController.navigate(FeedRoutes.Others(userId))
                }
            },
            onNavigateToBookDetail = { isbn ->
                navController.navigateToBookDetail(isbn)
            }
        )
    }

    composable<FeedRoutes.OthersSubscription> {
        OthersSubscriptionListScreen(
            onNavigateBack = navigateBack,
            onProfileClick = { userId, isMyself ->
                if (isMyself) {
                    navController.navigate(FeedRoutes.My)
                } else {
                    navController.navigate(FeedRoutes.Others(userId))
                }
            }
        )
    }
    composable<FeedRoutes.SearchPeople> {
        SearchPeopleScreen(
            onNavigateBack = navigateBack,
            onUserClick = { userId ->
                navController.navigateToUserProfile(userId)
            }
        )
    }
}