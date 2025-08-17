package com.texthip.thip.ui.navigator.navigations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.texthip.thip.ui.feed.screen.FeedCommentScreen
import com.texthip.thip.ui.feed.screen.FeedScreen
import com.texthip.thip.ui.feed.screen.FeedWriteScreen
import com.texthip.thip.ui.feed.screen.MySubscriptionScreen
import com.texthip.thip.ui.navigator.extensions.navigateToFeedComment
import com.texthip.thip.ui.navigator.extensions.navigateToFeedWrite
import com.texthip.thip.ui.navigator.extensions.navigateToMySubscription
import com.texthip.thip.ui.navigator.routes.FeedRoutes
import com.texthip.thip.ui.navigator.routes.MainTabRoutes

// Feed
fun NavGraphBuilder.feedNavigation(navController: NavHostController) {
    composable<MainTabRoutes.Feed> { backStackEntry ->
        val resultFeedId = backStackEntry.savedStateHandle.get<Int>("feedId")

        FeedScreen(
            nickname = "ThipUser01",
            userRole = "문학가",
            feeds = emptyList(),
            totalFeedCount = 0,
            selectedTabIndex = 0,
            followerProfileImageUrls = emptyList(),
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
        MySubscriptionScreen(navController = navController)
    }
    composable<FeedRoutes.Write> {
        FeedWriteScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
            onFeedCreated = { feedId ->
                // 피드 생성 성공 시 결과를 저장하고 피드 목록으로 돌아가기
                navController.getBackStackEntry(MainTabRoutes.Feed)
                    .savedStateHandle
                    .set("feedId", feedId)
                navController.popBackStack()
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