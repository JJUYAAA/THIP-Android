package com.texthip.thip.ui.navigator.navigations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.texthip.thip.ui.feed.screen.FeedScreen
import com.texthip.thip.ui.navigator.routes.MainTabRoutes

// Feed
fun NavGraphBuilder.feedNavigation(navController: NavHostController) {
    composable<MainTabRoutes.Feed> {
        FeedScreen(
            navController = navController,
            nickname = "ThipUser01",
            userRole = "문학가",
            feeds = emptyList(),
            totalFeedCount = 0,
            selectedTabIndex = 0,
            followerProfileImageUrls = emptyList()
        )
    }
}