package com.texthip.thip.ui.navigator

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.texthip.thip.ui.booksearch.screen.BookSearchScreen
import com.texthip.thip.ui.feed.screen.FeedScreen
import com.texthip.thip.ui.group.screen.GroupScreen
import com.texthip.thip.ui.mypage.screen.MyPageScreen

@Composable
fun MainNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.Feed) {
        composable<Routes.Feed> { FeedScreen(navController) }
        composable<Routes.Group> { GroupScreen(navController) }
        composable<Routes.BookSearch> { BookSearchScreen(navController = navController) }
        composable<Routes.MyPage> {
            MyPageScreen(
                navController,
                nickname = "ThipUser01",
                badgeText = "문학가"
            )
        }
    }
}