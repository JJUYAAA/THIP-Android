package com.texthip.thip.ui.navigator.navigations

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.texthip.thip.ui.navigator.routes.MainTabRoutes
import com.texthip.thip.ui.signin.screen.SignupGenreScreen
import com.texthip.thip.ui.signin.screen.SignupNicknameScreen
import com.texthip.thip.ui.signin.viewmodel.SignupViewModel

fun NavGraphBuilder.signupNavigation(navController: NavHostController) {
    navigation(
        startDestination = "signup_nickname",
        route = "signup_flow"
    ) {
        composable("signup_nickname") { navBackStackEntry ->
            val parentEntry = remember(navBackStackEntry) {
                navController.getBackStackEntry("signup_flow")
            }
            val viewModel: SignupViewModel = hiltViewModel(parentEntry)

            SignupNicknameScreen(
                viewModel = viewModel,
                onNavigateToGenre = {
                    navController.navigate("signup_genre")
                }
            )
        }

        composable("signup_genre"){ navBackStackEntry ->
            val parentEntry = remember(navBackStackEntry) {
                navController.getBackStackEntry("signup_flow")
            }
            val viewModel: SignupViewModel = hiltViewModel(parentEntry)

            SignupGenreScreen(
                viewModel = viewModel,
                onSignupSuccess = {
                    navController.navigate(MainTabRoutes.Feed) {
                        popUpTo("signup_flow") { inclusive = true }
                    }
                }
            )
        }
    }
}