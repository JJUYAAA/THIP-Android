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

// 유저 프로필(피드)로
fun NavHostController.navigateToUserProfile(userId: Long) {
    navigate(FeedRoutes.Others(userId))
}