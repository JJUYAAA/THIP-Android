package com.texthip.thip.ui.navigator.navigations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.texthip.thip.ui.feed.screen.FeedScreen
import com.texthip.thip.ui.feed.screen.FeedWriteScreen
import com.texthip.thip.ui.feed.screen.MySubscriptionScreen
import com.texthip.thip.ui.navigator.extensions.navigateToFeedWrite
import com.texthip.thip.ui.navigator.extensions.navigateToMySubscription
import com.texthip.thip.ui.navigator.routes.FeedRoutes
import com.texthip.thip.ui.navigator.routes.MainTabRoutes

// Feed
fun NavGraphBuilder.feedNavigation(navController: NavHostController) {
    composable<MainTabRoutes.Feed> {
        //TODO 추후 view model 적용 예정
        FeedScreen(
            navController = navController,
            nickname = "ThipUser01",
            userRole = "문학가",
            feeds = emptyList(),
            totalFeedCount = 0,
            selectedTabIndex = 0,
            followerProfileImageUrls = emptyList(),
            onNavigateToMySubscription = {
                navController.navigateToMySubscription()
            },
            onNavigateToFeedWrite = {
                navController.navigateToFeedWrite()
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
                // 피드 생성 성공 시 피드 목록으로 돌아가기
                navController.popBackStack()
            }
        )
    }
}