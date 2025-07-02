package com.texthip.thip.ui.navigator

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.texthip.thip.ui.bookSearch.screen.BookSearchScreen
import com.texthip.thip.ui.feed.screen.FeedScreen
import com.texthip.thip.ui.group.screen.GroupScreen
import com.texthip.thip.ui.myPage.screen.MyPageScreen

@Composable
fun MainNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.Feed.route) {
        composable(Routes.Feed.route) { FeedScreen(navController) }
        composable(Routes.Group.route) { GroupScreen(navController) }
        composable(Routes.BookSearch.route) { BookSearchScreen(navController) }
        composable(Routes.MyPage.route) {
            MyPageScreen(
                navController,
                nickname = "ThipUser01",
                badgeText = "문학가"
            )
        }
    }
}