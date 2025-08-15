package com.texthip.thip.ui.navigator.navigations

import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.texthip.thip.ui.mypage.screen.DeleteAccountScreen
import com.texthip.thip.ui.mypage.screen.EditProfileScreen
import com.texthip.thip.ui.mypage.screen.MyPageScreen
import com.texthip.thip.ui.mypage.screen.NotificationScreen
import com.texthip.thip.ui.mypage.screen.SavedScreen
import com.texthip.thip.ui.navigator.extensions.navigateToEditProfile
import com.texthip.thip.ui.navigator.extensions.navigateToLeaveThipScreen
import com.texthip.thip.ui.navigator.extensions.navigateToNotificationSettings
import com.texthip.thip.ui.navigator.extensions.navigateToSavedFeeds
import com.texthip.thip.ui.navigator.routes.MainTabRoutes
import com.texthip.thip.ui.navigator.routes.MyPageRoutes

// MyPage
fun NavGraphBuilder.myPageNavigation(navController: NavHostController) {
    composable<MainTabRoutes.MyPage> {
        MyPageScreen(
            navController = navController,
            onNavigateToEditProfile = { navController.navigateToEditProfile() },
            onNavigateToSavedFeeds = { navController.navigateToSavedFeeds() },
            onNavigateToNotificationSettings = { navController.navigateToNotificationSettings() },
            onDeleteAccount = { navController.navigateToLeaveThipScreen() }
        )
    }

    composable<MyPageRoutes.Edit> {
        EditProfileScreen()
    }
    composable<MyPageRoutes.Save> {
        SavedScreen()
    }
    composable<MyPageRoutes.NotificationEdit> {
        NotificationScreen()
    }
    composable<MyPageRoutes.LeaveThip> {
        DeleteAccountScreen()
    }
}