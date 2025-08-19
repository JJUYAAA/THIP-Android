package com.texthip.thip.ui.navigator.navigations

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.texthip.thip.ui.navigator.extensions.navigateToLogin
import com.texthip.thip.ui.navigator.extensions.navigateToMainAfterSignup
import com.texthip.thip.ui.navigator.extensions.navigateToSignup
import com.texthip.thip.ui.navigator.extensions.navigateToSignupGenre
import com.texthip.thip.ui.navigator.routes.CommonRoutes
import com.texthip.thip.ui.signin.screen.LoginScreen
import com.texthip.thip.ui.signin.screen.SignupGenreScreen
import com.texthip.thip.ui.signin.screen.SignupNicknameScreen
import com.texthip.thip.ui.signin.screen.SplashScreen
import com.texthip.thip.ui.signin.viewmodel.SignupViewModel

fun NavGraphBuilder.authNavigation(navController: NavHostController) {
    // --- 인증 관련 화면들 ---
    composable<CommonRoutes.Splash> {
        SplashScreen(
            onNavigateToLogin = { navController.navigateToLogin() }
        )
    }
    composable<CommonRoutes.Login> {
        LoginScreen(
            onNavigateToSignup = { navController.navigateToSignup() },
            onLoginSuccess = { navController.navigateToMainAfterSignup() }
        )
    }
    navigation<CommonRoutes.SignupFlow>(
        startDestination = CommonRoutes.Signup
    ) {
        composable<CommonRoutes.Signup> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry<CommonRoutes.SignupFlow>()
            }
            val signupViewModel = hiltViewModel<SignupViewModel>(parentEntry)

            SignupNicknameScreen(
                viewModel = signupViewModel,
                onNavigateToGenre = {
                    navController.navigateToSignupGenre()
                }
            )
        }
        composable<CommonRoutes.Genre> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry<CommonRoutes.SignupFlow>()
            }
            val signupViewModel = hiltViewModel<SignupViewModel>(parentEntry)

            SignupGenreScreen(
                viewModel = signupViewModel,
                onSignupSuccess = {
                    navController.navigate(CommonRoutes.Main) {
                        popUpTo(CommonRoutes.Login) {
                            inclusive = true
                        }
                    }
                }
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