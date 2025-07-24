package com.texthip.thip.ui.navigator

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

// 메인 네비게이션
@Composable
fun MainNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.Feed) {
        feedNavigation(navController)
        groupNavigation(navController)
        bookSearchNavigation(navController)
        myPageNavigation(navController)
    }
}