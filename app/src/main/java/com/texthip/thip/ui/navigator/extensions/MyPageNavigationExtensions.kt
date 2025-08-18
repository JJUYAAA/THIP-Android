package com.texthip.thip.ui.navigator.extensions

import androidx.navigation.NavHostController
import com.texthip.thip.ui.navigator.routes.MainTabRoutes
import com.texthip.thip.ui.navigator.routes.MyPageRoutes


// MyPage 관련 네비게이션 확장 함수들

fun NavHostController.navigateToMyPage() {
    navigate(MainTabRoutes.MyPage)
}
fun NavHostController.navigateToEditProfile() {
    navigate(MyPageRoutes.Edit)
}

fun NavHostController.navigateToSavedFeeds() {
    navigate(MyPageRoutes.Save)
}

fun NavHostController.navigateToNotificationSettings() {
    navigate(MyPageRoutes.NotificationEdit)
}

fun NavHostController.navigateToLeaveThipScreen() {
    navigate(MyPageRoutes.LeaveThip)
}

fun NavHostController.navigateToCustomerService() {
    navigate(MyPageRoutes.CustomerService)
}