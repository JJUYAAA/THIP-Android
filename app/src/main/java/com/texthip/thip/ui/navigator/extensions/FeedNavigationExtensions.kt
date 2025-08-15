package com.texthip.thip.ui.navigator.extensions

import androidx.navigation.NavHostController
import com.texthip.thip.ui.navigator.routes.FeedRoutes
import com.texthip.thip.ui.navigator.routes.MainTabRoutes

// Feed 확장 함수
fun NavHostController.navigateToFeed() {
    navigate(MainTabRoutes.Feed)
}

// 내 띱 목록으로
fun NavHostController.navigateToMySubscription() {
    navigate(FeedRoutes.MySubscription)
}

// 피드 작성으로
fun NavHostController.navigateToFeedWrite() {
    navigate(FeedRoutes.Write)
}