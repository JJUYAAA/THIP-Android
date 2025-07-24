package com.texthip.thip.ui.navigator.navigations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.texthip.thip.ui.navigator.routes.Routes
import com.texthip.thip.ui.search.screen.SearchBookScreen

fun NavGraphBuilder.searchNavigation(navController: NavHostController) {
    composable<Routes.Search> {
        SearchBookScreen(navController = navController)
    }
}