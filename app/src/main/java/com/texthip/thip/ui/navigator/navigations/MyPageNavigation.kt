package com.texthip.thip.ui.navigator.navigations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.texthip.thip.ui.mypage.screen.DeleteAccountScreen
import com.texthip.thip.ui.mypage.screen.EditProfileScreen
import com.texthip.thip.ui.mypage.screen.MyPageScreen
import com.texthip.thip.ui.mypage.screen.MypageCustomerServiceScreen
import com.texthip.thip.ui.mypage.screen.MypageSaveScreen
import com.texthip.thip.ui.mypage.screen.NotificationScreen
import com.texthip.thip.ui.navigator.extensions.navigateToCustomerService
import com.texthip.thip.ui.navigator.extensions.navigateToEditProfile
import com.texthip.thip.ui.navigator.extensions.navigateToLeaveThipScreen
import com.texthip.thip.ui.navigator.extensions.navigateToNotificationSettings
import com.texthip.thip.ui.navigator.extensions.navigateToSavedFeeds
import com.texthip.thip.ui.navigator.routes.MainTabRoutes
import com.texthip.thip.ui.navigator.routes.MyPageRoutes

// MyPage
fun NavGraphBuilder.myPageNavigation(
    navController: NavHostController,
    onNavigateToLogin: () -> Unit
) {
    composable<MainTabRoutes.MyPage> {
        MyPageScreen(
            navController = navController,
            onNavigateToEditProfile = { navController.navigateToEditProfile() },
            onNavigateToSavedFeeds = { navController.navigateToSavedFeeds() },
            onNavigateToNotificationSettings = { navController.navigateToNotificationSettings() },
            onCustomerService = {navController.navigateToCustomerService()},
            onDeleteAccount = { navController.navigateToLeaveThipScreen() },
            onNavigateToLogin = onNavigateToLogin
        )
    }

    composable<MyPageRoutes.Edit> {
        EditProfileScreen(
            onNavigateBack = { navController.popBackStack() }
        )
    }
    composable<MyPageRoutes.Save> {
        MypageSaveScreen(
            onNavigateBack = { navController.popBackStack() }
        )
    }
    composable<MyPageRoutes.NotificationEdit> {
        NotificationScreen(
            onNavigateBack = { navController.popBackStack() }
        )
    }
    composable<MyPageRoutes.LeaveThip> {
        DeleteAccountScreen(
            onNavigateBack = { navController.popBackStack() }
        )
    }
    composable<MyPageRoutes.CustomerService> {
        MypageCustomerServiceScreen (
            onNavigateBack = { navController.popBackStack() }
        )
    }
}