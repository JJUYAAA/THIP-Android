package com.texthip.thip.ui.navigator.navigations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.texthip.thip.ui.mypage.screen.MyPageScreen
import com.texthip.thip.ui.navigator.routes.MainTabRoutes

// MyPage
fun NavGraphBuilder.myPageNavigation(navController: NavHostController) {
    composable<MainTabRoutes.MyPage> {
        MyPageScreen(
            navController,
            nickname = "ThipUser01",
            badgeText = "문학가"
        )
    }

}