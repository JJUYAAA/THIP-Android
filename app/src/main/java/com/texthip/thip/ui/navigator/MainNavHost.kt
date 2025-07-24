package com.texthip.thip.ui.navigator

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.texthip.thip.ui.navigator.data.Routes
import com.texthip.thip.ui.navigator.navigations.feedNavigation
import com.texthip.thip.ui.navigator.navigations.groupNavigation
import com.texthip.thip.ui.navigator.navigations.myPageNavigation
import com.texthip.thip.ui.navigator.navigations.searchNavigation

// 메인 네비게이션
@Composable
fun MainNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.Feed) {
        feedNavigation(navController)
        groupNavigation(navController)
        searchNavigation(navController)
        myPageNavigation(navController)
    }
}