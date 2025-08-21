package com.texthip.thip.ui.navigator.navigations

import SplashScreen
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.texthip.thip.ui.navigator.extensions.navigateToMainAfterSignup
import com.texthip.thip.ui.navigator.routes.CommonRoutes
import com.texthip.thip.ui.signin.mock.SignupUserInfo
import com.texthip.thip.ui.signin.screen.LoginScreen
import com.texthip.thip.ui.signin.screen.SignupDoneScreen
import com.texthip.thip.ui.signin.screen.SignupGenreScreen
import com.texthip.thip.ui.signin.screen.SignupNicknameScreen
import com.texthip.thip.ui.signin.screen.TutorialScreen
import com.texthip.thip.ui.signin.viewmodel.SignupViewModel

fun NavGraphBuilder.authNavigation(
    navController: NavHostController,
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToSignup: () -> Unit,
    onNavigateToMainAfterSignup: () -> Unit
) {
    composable<CommonRoutes.Splash> {
        SplashScreen(
            onNavigateToLogin = onNavigateToLogin,
            onNavigateToHome = onNavigateToHome
        )
    }
    composable<CommonRoutes.Login> {
        LoginScreen(
            onNavigateToSignup = onNavigateToSignup,
            onLoginSuccess = onNavigateToMainAfterSignup
        )
    }
    navigation<CommonRoutes.SignupFlow>(
        startDestination = CommonRoutes.SignupScreenRoutes.Nickname
    ) {
        composable<CommonRoutes.SignupScreenRoutes.Nickname> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry<CommonRoutes.SignupFlow>()
            }
            val signupViewModel = hiltViewModel<SignupViewModel>(parentEntry)

            SignupNicknameScreen(
                viewModel = signupViewModel,
                onNavigateToGenre = {
                    navController.navigate(CommonRoutes.SignupScreenRoutes.Genre)
                }
            )
        }
        composable<CommonRoutes.SignupScreenRoutes.Genre> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry<CommonRoutes.SignupFlow>()
            }
            val signupViewModel = hiltViewModel<SignupViewModel>(parentEntry)

            SignupGenreScreen(
                viewModel = signupViewModel,
                onSignupSuccess = {
                    navController.navigate(CommonRoutes.SignupScreenRoutes.Tutorial)
                }
            )
        }
        composable<CommonRoutes.SignupScreenRoutes.Tutorial> {
            TutorialScreen(
                onFinish = {
                    navController.navigate(CommonRoutes.SignupScreenRoutes.Done) {
                        popUpTo<CommonRoutes.SignupScreenRoutes.Tutorial> { inclusive = true }
                    }                }
            )
        }
        composable<CommonRoutes.SignupScreenRoutes.Done> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry<CommonRoutes.SignupFlow>()
            }
            val signupViewModel = hiltViewModel<SignupViewModel>(parentEntry)
            val uiState by signupViewModel.uiState.collectAsStateWithLifecycle()

            val selectedRole = uiState.roleCards.getOrNull(uiState.selectedIndex)

            val userInfo = SignupUserInfo(
                nickname = uiState.nickname,
                profileImage = selectedRole?.imageUrl,
                role = selectedRole?.role ?: ""
            )

            SignupDoneScreen(
                userInfo = userInfo,
                onNavigateToMain = {
                    navController.navigateToMainAfterSignup()
                }
            )
        }
    }
}