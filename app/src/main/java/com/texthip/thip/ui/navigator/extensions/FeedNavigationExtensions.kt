package com.texthip.thip.ui.navigator.extensions

import androidx.navigation.NavHostController
import com.texthip.thip.ui.navigator.routes.MainTabRoutes

// Feed 확장 함수
fun NavHostController.navigateToFeed() {
    navigate(MainTabRoutes.Feed)
}
