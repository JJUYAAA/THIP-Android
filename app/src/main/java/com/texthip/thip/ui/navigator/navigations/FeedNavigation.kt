package com.texthip.thip.ui.navigator.navigations

import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.texthip.thip.ui.feed.screen.FeedCommentScreen
import com.texthip.thip.ui.feed.screen.FeedScreen
import com.texthip.thip.ui.feed.screen.FeedWriteScreen
import com.texthip.thip.ui.feed.screen.MySubscriptionScreen
import com.texthip.thip.ui.navigator.extensions.navigateToFeedComment
import androidx.navigation.toRoute
import com.texthip.thip.ui.feed.screen.FeedOthersScreen
import com.texthip.thip.ui.feed.viewmodel.FeedWriteViewModel
import com.texthip.thip.ui.navigator.extensions.navigateToFeedWrite
import com.texthip.thip.ui.navigator.extensions.navigateToMySubscription
import com.texthip.thip.ui.navigator.routes.FeedRoutes
import com.texthip.thip.ui.navigator.routes.MainTabRoutes

// Feed
fun NavGraphBuilder.feedNavigation(navController: NavHostController, navigateBack: () -> Unit) {
    composable<MainTabRoutes.Feed> { backStackEntry ->
        val resultFeedId = backStackEntry.savedStateHandle.get<Int>("feedId")

        FeedScreen(
            resultFeedId = resultFeedId,
            onResultConsumed = {
                backStackEntry.savedStateHandle.remove<Int>("feedId")
            },
            onNavigateToMySubscription = {
                navController.navigateToMySubscription()
            },
            onNavigateToFeedWrite = {
                navController.navigateToFeedWrite()
            },
            onNavigateToFeedComment = { feedId ->
                navController.navigateToFeedComment(feedId)
            }
        )
    }
    composable<FeedRoutes.MySubscription> {
        MySubscriptionScreen(
            onNavigateBack = {
                navigateBack()
            },
            onNavigateToUserProfile = { userId ->
                navController.navigate(FeedRoutes.Others(userId))
            }
        )
    }
    composable<FeedRoutes.Write> { backStackEntry ->
        val route = backStackEntry.toRoute<FeedRoutes.Write>()
        val viewModel: FeedWriteViewModel = hiltViewModel()

        LaunchedEffect(route) {
            if (route.isbn != null &&
                route.bookTitle != null &&
                route.bookAuthor != null &&
                route.bookImageUrl != null &&
                route.recordContent != null) {
                viewModel.setPinnedRecord(
                    isbn = route.isbn,
                    bookTitle = route.bookTitle,
                    bookAuthor = route.bookAuthor,
                    bookImageUrl = route.bookImageUrl,
                    recordContent = route.recordContent
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
                navController.popBackStack(MainTabRoutes.Feed, inclusive = false)
            }
        )
    }
    composable<FeedRoutes.Others> { backStackEntry ->
        // 다른 유저의 피드 화면
        FeedOthersScreen(
            onNavigateBack = {
                navigateBack()
            }
        )
    }
    composable<FeedRoutes.Comment> { backStackEntry ->
        val route = backStackEntry.arguments?.let {
            FeedRoutes.Comment(it.getInt("feedId"))
        } ?: return@composable

        FeedCommentScreen(
            feedId = route.feedId,
            onNavigateBack = {
                navController.popBackStack()
            }
        )
    }
}