package com.texthip.thip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.texthip.thip.ui.navigator.navigations.commonNavigation
import com.texthip.thip.ui.navigator.navigations.feedNavigation
import com.texthip.thip.ui.navigator.navigations.groupNavigation
import com.texthip.thip.ui.navigator.navigations.myPageNavigation
import com.texthip.thip.ui.navigator.navigations.searchNavigation
import com.texthip.thip.ui.navigator.routes.CommonRoutes
import com.texthip.thip.ui.signin.screen.LoginScreen
import com.texthip.thip.ui.signin.screen.SigninNicknameScreen
import com.texthip.thip.ui.signin.screen.SplashScreen
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.texthip.thip.data.manager.TokenManager
import com.texthip.thip.ui.theme.ThipTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ThipTheme {
                RootNavHost()
            }
        }
    }
}
@Composable
fun RootNavHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = CommonRoutes.Splash
    ) {
        // --- 인증 관련 화면들 ---
        composable<CommonRoutes.Splash> {
            SplashScreen(navController = navController)
        }
        composable<CommonRoutes.Login> {
            LoginScreen(navController = navController)
        }
        composable<CommonRoutes.Signup> {
            SigninNicknameScreen(navController = navController)
        }

        // --- 메인 관련 화면들 ---
        feedNavigation(navController)
        groupNavigation(
            navController = navController,
            navigateBack = navController::popBackStack
        )
        searchNavigation(navController)
        myPageNavigation(navController)
        commonNavigation(
            navController = navController,
            navigateBack = navController::popBackStack
        )
    }
}